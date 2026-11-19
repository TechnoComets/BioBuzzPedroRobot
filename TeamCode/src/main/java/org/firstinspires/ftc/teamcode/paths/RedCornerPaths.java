package org.firstinspires.ftc.teamcode.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * Poses and paths for the RED CORNER autonomous (RedCornerAuto).
 *
 * Coordinates are Pedro field coordinates: x, y in inches (0-144), heading in degrees.
 * TODO: the numbers below are PLACEHOLDERS. Replace them with real ones from the Pedro visualizer.
 * Each path should start where the previous one ended.
 */
public class RedCornerPaths {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose start = poseFactory.of(120, 126, 35);   // PLACEHOLDER
    private final Pose shoot = poseFactory.of(88, 90, 45);  // PLACEHOLDER
    private final Pose park = poseFactory.of(104, 110, 90);    // PLACEHOLDER

    /** Start -> shooting spot. */
    public Path path1() {
        return Paths.line(start, shoot).linear(start, shoot);
    }

    /** Shooting spot -> park. */
    public Path path2() {
        return Paths.line(shoot, park).linear(shoot, park);
    }

    // Add more paths here (path3(), path4(), ...) and call them from RedCornerAuto.
}
