package org.firstinspires.ftc.teamcode.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * Poses and paths for the autonomous routine.
 *
 * Paste new output from the Pedro visualizer here. MainAuto only asks for path1(), path2(), etc.,
 * so regenerating paths never touches the autonomous logic.
 */
public class AutoPaths {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose start = poseFactory.of(20.8162, 13.2066, 90);
    public final Pose point1 = poseFactory.of(21.2283, 68.2657, 90);
    public final Pose point2 = poseFactory.of(67.3325, 81.8905, 163.9068);
    public final Pose point2Control1 = poseFactory.of(34.7364, 91.2947, 0);

    /** Straight line from the start pose to point1, holding a linear heading change. */
    public Path path1() {
        return Paths.line(start, point1).linear(start, point1);
    }

    /** Curve from point1 to point2 (through a control point), driving in reverse. */
    public Path path2() {
        return Paths.curve(point1, point2Control1, point2).reverseTangent();
    }
}
