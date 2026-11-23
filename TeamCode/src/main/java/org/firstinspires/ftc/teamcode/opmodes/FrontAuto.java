package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.paths.FrontPaths;

/**
 * Autonomous for the FRONT starting position.
 * Paths live in FrontPaths. The setup and main loop live in BaseAuto.
 */
@Autonomous(name = "Front Auto", group = "Autonomous")
public class FrontAuto extends BaseAuto {

    private FrontPaths paths;

    // Timing (milliseconds)
    private static final double SHOOTER_WARMUP_MS = 5000; // shooter spins this long before intake + transfer start
    private static final double SHOOT_MS = 3000;          // shooter + intake + transfer all running (3 seconds)

    // Counts how long the shooter has been warming up
    private final ElapsedTime warmupTimer = new ElapsedTime();

    @Override
    protected void buildPaths() {
        paths = new FrontPaths();
    }

    @Override
    protected Pose startPose() {
        return paths.start;
    }

    // Steps run top to bottom. The robot holds its position during every waitMs(...).
    @Override
    protected Command autoRoutine() {
        return sequential(
                // ---- Start warming up the shooter right away, while driving path 1 ----
                instant(() -> {
                    robot.shooter.run();
                    warmupTimer.reset();
                }),
                follow(robot.follower, paths.path1()),

                // ---- Wait until the shooter has warmed up for 5 s (counted from START) ----
                waitUntil(() -> warmupTimer.milliseconds() >= SHOOTER_WARMUP_MS),

                // ---- Now start intake + transfer to shoot ----
                instant(() -> {
                    robot.intake.run();
                    robot.transfer.feed();
                }),
                waitMs(SHOOT_MS),

                // ---- Stop shooter, intake and transfer at the same time ----
                instant(() -> {
                    robot.shooter.stop();
                    robot.intake.stop();
                    robot.transfer.stop();
                }),

                // ---- Park ----
                follow(robot.follower, paths.path2()),

                // ---- Stop everything at the end of path 2 ----
                instant(() -> robot.stop())
        );
    }
}
