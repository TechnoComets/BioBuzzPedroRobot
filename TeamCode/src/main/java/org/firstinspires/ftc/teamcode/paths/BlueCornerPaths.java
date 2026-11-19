package org.firstinspires.ftc.teamcode.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * Poses and paths for the BLUE CORNER autonomous (BlueCornerAuto).
 *
 * Coordinates are Pedro field coordinates: x, y in inches (0-144), heading in degrees.
 * TODO: the numbers below are PLACEHOLDERS. Replace them with real ones from the Pedro visualizer.
 * Each path should start where the previous one ended.
 */
public class BlueCornerPaths {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose start = poseFactory.of(81.6018, 8.0672, 90);
    private final Pose point1 = poseFactory.of(81.7989, 19.0264, 90);
    private final Pose point2Start = poseFactory.of(81.7989, 19.0264, 0);
    private final Pose point2 = poseFactory.of(133.56, 28.9333, 0);

    public Path path1() {
        return Paths.line(start, point1).linear(start, point1);
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }

    // Add more paths here (path3(), path4(), ...) and call them from BlueCornerAuto.
}
