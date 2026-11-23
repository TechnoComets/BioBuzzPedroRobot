package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

/**
 * The one TeleOp. Like the Autos, it only says WHAT the driver can do; hardware lives in
 * Robot and subsystems/.
 *
 * Gamepad 1 controls
 *   Left stick        drive + strafe
 *   Right stick X     turn
 *   Right bumper      slow mode (hold)
 *   Right trigger     intake in
 *   Left trigger      intake out (clears jams)
 *   Left bumper       transfer: move pollen forward from the intake (hold)
 *   Y / Triangle      transfer: reverse (hold, clears jams)
 *   A                 Limelight: AprilTag pipeline
 *   B                 Limelight: color pipeline
 *   X (hold)          auto-aim: turns toward the Limelight target (driver still controls forward/strafe)
 *   D-pad left        shooter servos: lock in place at Config.SHOOTER_SERVO_LOCK_POSITION (limp until pressed)
 *   D-pad up          shooter servos: move +0.05 and stay locked
 *   D-pad down        shooter servos: move -0.05 and stay locked
 *
 * NOTE: robot.update() is NOT called here, because it runs follower.update(), which is
 * Pedro's path-following loop and would fight manual motor power. Only vision is updated.
 * Consequence: Pedro's pose is not tracked during TeleOp.
 */
@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends LinearOpMode {
    private Shooter shooter;

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Drive drive = new Drive(hardwareMap);

        robot.vision.useAprilTags();

        telemetry.addData("Intake found", robot.intake.isAvailable());
        telemetry.addData("Transfer found", robot.transfer.isAvailable());
        telemetry.addData("Limelight found", robot.vision.isAvailable());
        telemetry.addData("Shooter Left found", robot.shooterServos.isLeftAvailable());
        telemetry.addData("Shooter Right found", robot.shooterServos.isRightAvailable());
        telemetry.addLine("Ready. Press START.");
        telemetry.update();
        shooter = new Shooter(hardwareMap);

        waitForStart();

        boolean lastA = false;
        boolean lastB = false;

        try {
            while (opModeIsActive()) {
                robot.vision.update();
                robot.shooterServos.update(); // Continuously enforce shooter servo positions every loop

                // ---- Vision pipeline switching (on button press, not while held) ----
                if (gamepad1.a && !lastA) robot.vision.useAprilTags();
                if (gamepad1.b && !lastB) robot.vision.useColor();
                lastA = gamepad1.a;
                lastB = gamepad1.b;

                // ---- Drive ----
                double forward = -gamepad1.left_stick_y; // stick up is negative
                double strafe = gamepad1.left_stick_x;
                double turn = gamepad1.right_stick_x;

                boolean aiming = gamepad1.x && robot.vision.hasTarget();
                if (aiming) {
                    // tx > 0 means the target is to the right, so turn right (clockwise).
                    // If the robot turns AWAY from the target, flip the sign of AIM_KP in Config.
                    double aimTurn = Config.AIM_KP * robot.vision.getTx();
                    turn = Math.max(-Config.AIM_MAX_TURN, Math.min(Config.AIM_MAX_TURN, aimTurn));
                }

                double scale = gamepad1.right_bumper ? Config.SLOW_MODE_SCALE : 1.0;
                drive.drive(forward, strafe, turn, scale);

                // ---- Intake ----
                if (gamepad1.right_trigger > Config.TRIGGER_DEADZONE) {
                    robot.intake.run();
                } else if (gamepad1.left_trigger > Config.TRIGGER_DEADZONE) {
                    robot.intake.reverse();
                } else {
                    robot.intake.stop();
                }

                // ---- Transfer ----
                if (gamepad1.left_bumper) {
                    robot.transfer.feed();
                } else if (gamepad1.y) {
                    robot.transfer.reverse();
                } else {
                    robot.transfer.stop();
                }

                // ---- Shooter servos: D-pad left = lock, up / down = move 0.05 and stay locked ----
                robot.shooterServos.handleButtons(gamepad1.dpad_left, gamepad1.dpad_up, gamepad1.dpad_down);

                // ----- Shooter ------
                shooter.update(gamepad1.circle);
                // ---- Telemetry ----
                telemetry.addData("Slow mode", gamepad1.right_bumper);
                telemetry.addData("Intake running", robot.intake.isRunning());
                telemetry.addData("Transfer running", robot.transfer.isRunning());
                telemetry.addData("Pipeline", robot.vision.getActivePipeline() == Config.PIPELINE_APRILTAG
                        ? "AprilTag" : "Color");
                telemetry.addData("Target seen", robot.vision.hasTarget());
                telemetry.addData("tx", robot.vision.getTx());
                telemetry.addData("Auto-aim active", aiming);
                telemetry.addData("Shooter Left found", robot.shooterServos.isLeftAvailable());
                telemetry.addData("Shooter Right found", robot.shooterServos.isRightAvailable());
                telemetry.addData("Shooter servos locked", robot.shooterServos.isLocked());
                telemetry.addData("Shooter servo position", "%.2f", robot.shooterServos.getLockPosition());
                telemetry.addData("Shooter servo last change", robot.shooterServos.getLastChange());
                telemetry.addData("Shooter Motor Speed", robot.shooter.getPower());
                telemetry.update();
            }
        } finally {
            drive.stop();
            robot.stop();
        }
    }
}
