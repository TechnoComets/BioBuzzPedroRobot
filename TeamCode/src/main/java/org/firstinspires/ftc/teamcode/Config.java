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

    // ---------------- Drivetrain (used by TeleOp's Drive subsystem) ----------------
    // These MUST match pedro/Constants.java (drivetrainConfig) so TeleOp drives exactly like the tuned auto.
    // If you change a name or direction in Constants.java, change it here too.
    public static final String FRONT_LEFT_NAME = "leftFront";
    public static final String FRONT_RIGHT_NAME = "rightFront";
    public static final String BACK_LEFT_NAME = "leftBack";
    public static final String BACK_RIGHT_NAME = "rightBack";
    public static final DcMotorSimple.Direction FRONT_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static final DcMotorSimple.Direction FRONT_RIGHT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final DcMotorSimple.Direction BACK_LEFT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final DcMotorSimple.Direction BACK_RIGHT_DIRECTION = DcMotorSimple.Direction.REVERSE;

    // ---------------- TeleOp tuning ----------------
    public static final double SLOW_MODE_SCALE = 0.4;     // hold right bumper
    public static final double TRIGGER_DEADZONE = 0.1;
    public static final double AIM_KP = 0.02;              // turn power per degree of Limelight tx (hold X)
    public static final double AIM_MAX_TURN = 0.35;

    // ---------------- Hardware names (must match the Robot Configuration on the Control Hub) ----------------
    public static final String INTAKE_MOTOR_NAME = "intake";
    public static final String TRANSFER_MOTOR_NAME = "transfer";
    public static final String LIMELIGHT_NAME = "limelight";

    // ---------------- Intake ----------------
    public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double INTAKE_POWER = 1.0;      // pulling a game piece in
    public static final double OUTTAKE_POWER = -0.6;    // pushing a game piece out / clearing a jam

    // ---------------- Transfer ----------------
    // If feed() moves pollen back toward the intake, flip this to REVERSE.
    public static final DcMotorSimple.Direction TRANSFER_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double TRANSFER_POWER = 0.8;          // moving pollen forward from the intake
    public static final double TRANSFER_REVERSE_POWER = -0.5; // backing pollen out / clearing a jam

    // ---------------- Limelight ----------------
    // Pipelines are built in the Limelight web interface (http://limelight.local:5801).
    // These numbers must match the pipeline slots you configure there.
    public static final int PIPELINE_APRILTAG = 0;
    public static final int PIPELINE_COLOR = 1;

    public static final int LIMELIGHT_POLL_RATE_HZ = 100;
    // Ignore Limelight results older than this (milliseconds).
    public static final long VISION_MAX_STALENESS_MS = 100;
}
