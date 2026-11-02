package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Shared names and tunable numbers for the robot, in one place.
 *
 * Rename a device or tweak a number here instead of hunting through subsystems.
 *
 * NOTE: Drivetrain motor names and the Pinpoint localizer settings live in
 * pedro/Constants.java (Pedro's own config). They are intentionally not duplicated here.
 */
public final class Config {
    private Config() {}

    // ---------------- Hardware names (must match the Robot Configuration on the Control Hub) ----------------
    public static final String INTAKE_MOTOR_NAME = "intake";
    public static final String LIMELIGHT_NAME = "limelight";

    // ---------------- Intake ----------------
    public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double INTAKE_POWER = 1.0;      // pulling a game piece in
    public static final double OUTTAKE_POWER = -0.6;    // pushing a game piece out / clearing a jam

    // ---------------- Limelight ----------------
    // Pipelines are built in the Limelight web interface (http://limelight.local:5801).
    // These numbers must match the pipeline slots you configure there.
    public static final int PIPELINE_APRILTAG = 0;
    public static final int PIPELINE_COLOR = 1;

    public static final int LIMELIGHT_POLL_RATE_HZ = 100;
    // Ignore Limelight results older than this (milliseconds).
    public static final long VISION_MAX_STALENESS_MS = 100;
}
