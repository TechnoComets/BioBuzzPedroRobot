package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.paths.BlueCornerPaths;

/**
 * Autonomous for the BLUE CORNER starting position.
 * Paths live in BlueCornerPaths. The setup and main loop live in BaseAuto.
 */
@Autonomous(name = "Blue Corner Auto", group = "Autonomous")
public class BlueCornerAuto extends BaseAuto {

    private BlueCornerPaths paths;

    // Timing (milliseconds), measured from the end of path 1
    private static final double WAIT_BEFORE_SHOOTER_MS = 5000; // robot waits, then shooter starts
    private static final double WAIT_BEFORE_FEED_MS = 0;       // then intake + transfer start (2 s left of the 7 s pause = same moment)
    private static final double FEED_MS = 3000;                // intake + transfer run, then everything stops

    @Override
    protected void buildPaths() {
        paths = new BlueCornerPaths();
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

                // ---- Wait 5 s, then start the shooter ----
                waitMs(WAIT_BEFORE_SHOOTER_MS),
                instant(() -> robot.shooter.run()),

                // ---- With 2 s of the 7 s pause left, start intake + transfer ----
                waitMs(WAIT_BEFORE_FEED_MS),
                instant(() -> {
                    robot.intake.run();
                    robot.transfer.feed();
                }),

                // ---- After 3 s of intake + transfer, stop everything ----
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
