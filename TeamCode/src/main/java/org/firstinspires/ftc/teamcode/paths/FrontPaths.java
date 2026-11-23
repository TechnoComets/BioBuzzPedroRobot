package org.firstinspires.ftc.teamcode.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * Poses and paths for the RED FRONT autonomous (FrontAuto).
 *
 * Coordinates are Pedro field coordinates: x, y in inches (0-144), heading in degrees.
 * Each path should start where the previous one ended.
 */
public class FrontPaths {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose start = poseFactory.of(56, 8, 90);
    private final Pose path1 = poseFactory.of(56, 11.434, 90);
    private final Pose point2 = poseFactory.of(7.1811, 96.507, 89.4182);
    private final Pose point2Control1 = poseFactory.of(6.8785, 66.698, 0);

    public Path path1() {
        return Paths.line(start, path1).tangent();
    }

    public Path path2() {
        return Paths.curve(path1, point2Control1, point2).tangent();
    }

    // Add more paths here (path3(), path4(), ...) and call them from FrontAuto.
}
