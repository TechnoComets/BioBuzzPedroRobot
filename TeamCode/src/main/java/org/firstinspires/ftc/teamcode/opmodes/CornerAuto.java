package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.paths.CornerPaths;

/**
 * Autonomous for the CORNER starting position.
 * Paths live in CornerPaths. The setup and main loop live in BaseAuto.
 */
@Autonomous(name = "Corner Auto", group = "Autonomous")
public class CornerAuto extends BaseAuto {

    private CornerPaths paths;

    // Timing (milliseconds), measured from the end of path 1
    private static final double SHOOTER_WARMUP_MS = 5000; // shooter starts right after path 1 and warms up this long
    private static final double FEED_MS = 3000;           // then intake + transfer run, then all three stop together

    @Override
    protected void buildPaths() {
        paths = new CornerPaths();
    }

    @Override
    protected Pose startPose() {
        return paths.start;
    }

    // Steps run top to bottom. The robot holds its position during every waitMs(...).
    @Override
    protected Command autoRoutine() {
        return sequential(
                // ---- Path 1 ----
                follow(robot.follower, paths.path1()),

                // ---- Start warming up the shooter right away (5 s before intake + transfer) ----
                instant(() -> robot.shooter.run()),
                waitMs(SHOOTER_WARMUP_MS),

                // ---- 5 s after path 1 (same time as before): start intake + transfer ----
                instant(() -> {
                    robot.intake.run();
                    robot.transfer.feed();
                }),

                // ---- After 3 s of intake + transfer, stop shooter, intake and transfer together ----
                waitMs(FEED_MS),
                instant(() -> {
                    robot.shooter.stop();
                    robot.intake.stop();
                    robot.transfer.stop();
                }),

                // ---- Park ----
                follow(robot.follower, paths.path2()),

                // ---- Stop immediately after path 2: drive wheels, every mechanism, then end the autonomous ----
                instant(() -> {
                    robot.follower.stop(); // cut the drive motors right away
                    robot.shooter.stop();
                    robot.intake.stop();
                    robot.transfer.stop();
                    requestOpModeStop(); // ends the OpMode; BaseAuto then calls robot.stop()
                })
        );
    }
}
