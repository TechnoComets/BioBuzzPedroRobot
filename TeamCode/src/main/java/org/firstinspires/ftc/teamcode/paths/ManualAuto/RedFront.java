package org.firstinspires.ftc.teamcode.paths.ManualAuto;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Drive;

/**
 * Autonomous OpMode for Red Front starting position.
 * Sequence:
 * 1. Starts at Red Front position.
 * 2. Moves forward to shooting position.
 * 3. Shoots 4 pollens (using time delays since no PIDF velocity control is available).
 * 4. Moves to and parks in the Red Parking Zone.
 */
@Autonomous(name = "Red Front Auto", group = "Autonomous")
public class RedFront extends LinearOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    // Field positions for Red Alliance (Pedro Pathing Poses in inches / degrees)
    private final Pose startPose = poseFactory.of(20.0, -13.0, 270.0);
    private final Pose shootPose = poseFactory.of(20.0, -45.0, 270.0);
    private final Pose parkPose  = poseFactory.of(60.0, -24.0, 0.0);

    // Timed shooting parameters (milliseconds per pollen shot without PIDF)
    private static final long SHOOT_PREP_TIME_MS = 1500; // Time to spin up shooter motor
    private static final long TIME_PER_POLLEN_MS = 1200; // Time per pollen transfer/feed

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Drive drive = new Drive(hardwareMap);

        robot.setStartPose(startPose);

        telemetry.addData("Status", "Initialized Red Front Auto");
        telemetry.addData("Transfer Available", robot.transfer.isAvailable());
        telemetry.addData("Shooter Available", robot.shooter.isAvailable());
        telemetry.addData("Intake Available", robot.intake.isAvailable());
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // ===================================================================
        // STEP 1: Move Forward to Shooting Position
        // ===================================================================
        telemetry.addData("Status", "Moving forward to shooting position...");
        telemetry.update();

        Path startToShoot = Paths.line(startPose, shootPose).linear(startPose, shootPose);
        robot.follower.follow(startToShoot);

        while (opModeIsActive() && robot.follower.isBusy()) {
            robot.update();
            telemetry.addData("Status", "Driving to shooting pose...");
            telemetry.update();
        }

        drive.stop();
        sleep(300);

        // ===================================================================
        // STEP 2: Shoot 4 Pollens (Time-Based Shooting Without PIDF)
        // ===================================================================
        telemetry.addData("Status", "Shooting 4 Pollens...");
        telemetry.update();

        // Spin up intake / shooter mechanism and wait for motor speed (no PIDF)
        robot.shooter.run();
        robot.intake.run();
        sleep(SHOOT_PREP_TIME_MS);

        // Feed all 4 pollens into shooter with timed delays
        for (int i = 1; i <= 4; i++) {
            if (!opModeIsActive()) break;

            telemetry.addData("Status", "Shooting pollen #" + i + " of 4");
            telemetry.update();

            // Cycle transfer mechanism to feed pollen
            robot.transfer.feed();
            sleep(TIME_PER_POLLEN_MS);

            // Reset transfer mechanism
            robot.transfer.stop();
            sleep(400);
        }

        // Stop transfer & intake & shooter
        robot.intake.stop();
        robot.transfer.stop();
        robot.shooter.stop();
        sleep(500);

        // ===================================================================
        // STEP 3: Move and Park in Red Parking Zone
        // ===================================================================
        telemetry.addData("Status", "Moving to Red Parking Zone...");
        telemetry.update();

        Path shootToPark = Paths.line(shootPose, parkPose).linear(shootPose, parkPose);
        robot.follower.follow(shootToPark);

        while (opModeIsActive() && robot.follower.isBusy()) {
            robot.update();
            telemetry.addData("Status", "Parking...");
            telemetry.update();
        }

        drive.stop();
        robot.stop();

        telemetry.addData("Status", "Autonomous Complete! Parked in Red Zone.");
        telemetry.update();
    }
}
