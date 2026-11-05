package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

/**
 * Owns every part of the robot: the Pedro follower (drivetrain + localizer) and all subsystems.
 * Both Autonomous and TeleOp build one Robot, so they always start from identical setup.
 *
 * To add a mechanism: create a class in subsystems/, add a field here, construct it in the
 * constructor, and (if it needs per-loop work) call its update() inside update().
 */
public class Robot {
    public final Follower follower;
    public final Intake intake;
    public final Transfer transfer;
    public final Vision vision;

    public Robot(HardwareMap hardwareMap) {
        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        transfer = new Transfer(hardwareMap);
        vision = new Vision(hardwareMap);
        vision.start();
    }

    /** Tell Pedro where the robot is sitting at the start of the match. */
    public void setStartPose(Pose pose) {
        follower.setPose(pose);
        follower.update();
    }

    /** Call once per loop. */
    public void update() {
        follower.update();
        vision.update();
    }

    /** Call when the OpMode ends. */
    public void stop() {
        intake.stop();
        transfer.stop();
        vision.stop();
    }
}
