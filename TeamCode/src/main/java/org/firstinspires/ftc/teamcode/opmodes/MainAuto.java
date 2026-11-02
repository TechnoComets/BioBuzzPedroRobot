package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.paths.AutoPaths;

/**
 * The one autonomous. Keep this file short: it only says WHAT to do, in order.
 * Paths live in AutoPaths, hardware lives in Robot and subsystems/.
 */
@Autonomous(name = "Main Auto", group = "Autonomous")
public class MainAuto extends LinearOpMode {

    private Robot robot;
    private AutoPaths paths;

    // Autonomous routine. Add new steps here (more follow(...) calls, intake actions, etc.).
    private Command autoRoutine() {
        return sequential(
                follow(robot.follower, paths.path1()),
                follow(robot.follower, paths.path2())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        robot = new Robot(hardwareMap);
        paths = new AutoPaths();
        robot.setStartPose(paths.start);

        telemetry.addData("Intake found", robot.intake.isAvailable());
        telemetry.addData("Limelight found", robot.vision.isAvailable());
        telemetry.update();

        waitForStart();
        schedule(autoRoutine());

        try {
            while (opModeIsActive()) {
                robot.update();
                Scheduler.execute();

                telemetry.addData("x", robot.follower.pose().x());
                telemetry.addData("y", robot.follower.pose().y());
                telemetry.addData("heading", robot.follower.pose().heading());

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
