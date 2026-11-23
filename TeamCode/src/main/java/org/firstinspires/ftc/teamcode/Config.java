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
    public static final String SHOOTER_MOTOR_NAME = "shooter";
    public static final String LIMELIGHT_NAME = "limelight";

    // ---------------- Intake ----------------
    public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.FORWARD;

    // Intake toggle
    public static final boolean INTAKE_TOGGLE_ENABLED = true;
    public static final double INTAKE_POWER = 1.0;      // pulling a game piece in
    public static final double OUTTAKE_POWER = -0.6;    // pushing a game piece out / clearing a jam

    // ---------------- Transfer ----------------
    // If feed() moves pollen back toward the intake, flip this to REVERSE.
    public static final DcMotorSimple.Direction TRANSFER_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double TRANSFER_POWER = 0.8;            // moving pollen forward from the intake
    public static final double TRANSFER_REVERSE_POWER = -0.5;   // backing pollen out / clearing a jam

    // ---------------- Shooter ----------------
    public static final DcMotorSimple.Direction SHOOTER_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double SHOOTER_POWER = 0.7;
    public static final double SHOOTER_OUTTAKE_POWER = -1.0;

    // ---------------- Limelight ----------------
    public static final int PIPELINE_APRILTAG = 0;
    public static final int PIPELINE_COLOR = 1;

    public static final int LIMELIGHT_POLL_RATE_HZ = 100;
    public static final long VISION_MAX_STALENESS_MS = 100;

    // ---------------- Shooter Opening Servos (Hood/Gate) ----------------
    public static final String SHOOTER_LEFT_SERVO_NAME = "ShooterServoLeft";
    public static final String SHOOTER_RIGHT_SERVO_NAME = "ShooterServoRight";
    // If the two servos face opposite directions (mirrored), set ONE of these to true.
    public static final boolean SHOOTER_LEFT_SERVO_REVERSED = false;
    public static final boolean SHOOTER_RIGHT_SERVO_REVERSED = true;
    // Servos stay limp until D-pad left is pressed (Auto and TeleOp), then lock and hold here (0.0 to 1.0).
    // Normal servos can't report their angle, so this one number is where "locked" is. Tune it on the robot.
    public static final double SHOOTER_SERVO_LOCK_POSITION = 0.55;
    // D-pad up / down moves the locked position by this much per press (servos stay locked).
    // If up moves the wrong way, make this negative.
    public static final double SHOOTER_SERVO_STEP = 0.05;
}
