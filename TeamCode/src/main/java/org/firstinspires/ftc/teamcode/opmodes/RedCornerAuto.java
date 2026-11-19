package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.paths.RedCornerPaths;

/**
 * Autonomous for the RED CORNER starting position.
 * Paths live in RedCornerPaths. The setup and main loop live in BaseAuto.
 */
@Autonomous(name = "Red Corner Auto", group = "Autonomous")
public class RedCornerAuto extends BaseAuto {

    private RedCornerPaths paths;

    // Timing (milliseconds)
    private static final double SHOOT_MS = 2000; // shooter + intake + transfer

    @Override
    protected void buildPaths() {
        paths = new RedCornerPaths();
    }

    @Override
    protected Pose startPose() {
        return paths.start;
    }

    // Steps run top to bottom. The robot holds its position during every waitMs(...).
    @Override
    protected Command autoRoutine() {
        return sequential(
                // ---- Drive to the shooting spot and shoot ----
                follow(robot.follower, paths.path1()),
                instant(() -> {
                    robot.shooter.run();
                    robot.intake.run();
                    robot.transfer.feed();
                }),
                waitMs(SHOOT_MS),
                instant(() -> {
                    robot.shooter.stop();
                    robot.intake.stop();
                    robot.transfer.stop();
                }),

                // ---- Park ----
                follow(robot.follower, paths.path2())
        );
    }
}
