package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;

/**
 * Shared setup + main loop for every autonomous.
 *
 * Each Auto (BlueFrontAuto, RedCornerAuto, ...) extends this and only says:
 *   1. where the robot starts      -> startPose()
 *   2. what to do, in order        -> autoRoutine()
 * Everything else (init, telemetry, keeping the follower running) lives here once.
 */
public abstract class BaseAuto extends LinearOpMode {

    protected Robot robot;

    /** Where the robot is placed on the field at the start of the match. */
    protected abstract Pose startPose();

    /** The steps of this autonomous, in order. */
    protected abstract Command autoRoutine();

    /** Called once after the Robot is built, before startPose(). Create your paths here. */
    protected abstract void buildPaths();

    @Override
    public void runOpMode() {
        Scheduler.reset();
        robot = new Robot(hardwareMap);
        buildPaths();
        robot.setStartPose(startPose());

        telemetry.addData("Status", "Initialized " + getClass().getSimpleName());
        telemetry.addData("Intake found", robot.intake.isAvailable());
        telemetry.addData("Limelight found", robot.vision.isAvailable());
        telemetry.addData("Transfer found", robot.transfer.isAvailable());
        telemetry.addData("Shooter found", robot.shooter.isAvailable());
        telemetry.update();

        waitForStart();
        schedule(autoRoutine());

        try {
            while (opModeIsActive()) {
                // Shooter servos (same as TeleOp): D-pad left = lock, up / down = move 0.05 and stay locked
                robot.shooterServos.handleButtons(gamepad1.dpad_left, gamepad1.dpad_up, gamepad1.dpad_down);

                robot.update();
                Scheduler.execute();

                telemetry.addData("x", robot.follower.pose().x());
                telemetry.addData("y", robot.follower.pose().y());
                telemetry.addData("heading", robot.follower.pose().heading());
                telemetry.addData("Shooter servos locked", robot.shooterServos.isLocked());
                telemetry.addData("Shooter servo position", "%.2f", robot.shooterServos.getLockPosition());
                telemetry.addData("Shooter servo last change", robot.shooterServos.getLastChange());

                if (robot.follower.currentPath() != null) {
                    telemetry.addData("Current path distance remaining", robot.follower.distanceToEndpoint());
                    telemetry.addData("Path number", robot.follower.pathIndex());
                }

                telemetry.update();
            }
        } finally {
            robot.stop();
        }
    }
}
