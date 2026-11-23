package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config;

/**
 * ShooterServos Subsystem: the two gate/hood servos ("ShooterServoLeft" and "ShooterServoRight").
 *
 * At start the servos are left alone: no position is ever sent, so they get no signal and stay limp.
 * Calling lock() (D-pad left, in Auto and TeleOp) sends Config.SHOOTER_SERVO_LOCK_POSITION and
 * keeps re-sending it every loop in update(), so the servos hold firm and do not move.
 *
 * These are normal servos with no feedback wire, so the code cannot read where they actually are.
 * That is why locking needs one position number from Config.
 */
public class ShooterServos {
    private final Servo leftServo;
    private final Servo rightServo;

    private boolean locked = false;
    private double lockPosition = Config.SHOOTER_SERVO_LOCK_POSITION;
    private String lastChange = "none yet (press D-pad up / down)";

    // Last button states, so one press = one action (not repeated every loop while held)
    private boolean lastLock = false;
    private boolean lastUp = false;
    private boolean lastDown = false;

    public ShooterServos(HardwareMap hardwareMap) {
        leftServo = hardwareMap.tryGet(Servo.class, Config.SHOOTER_LEFT_SERVO_NAME);
        rightServo = hardwareMap.tryGet(Servo.class, Config.SHOOTER_RIGHT_SERVO_NAME);

        // Two servos on opposite sides of the shooter usually face opposite directions. If they
        // do, the same position command makes them fight each other.
        // Reverse one in Config so they turn the same way.
        if (leftServo != null && Config.SHOOTER_LEFT_SERVO_REVERSED) {
            leftServo.setDirection(Servo.Direction.REVERSE);
        }
        if (rightServo != null && Config.SHOOTER_RIGHT_SERVO_REVERSED) {
            rightServo.setDirection(Servo.Direction.REVERSE);
        }
        // Intentionally NO setPosition() here: the servos stay unpowered until lock() is called.
    }

    /** True if at least one shooter servo was found in the Robot Configuration. */
    public boolean isAvailable() {
        return leftServo != null || rightServo != null;
    }

    /** Lock both servos at the current lock position. They stay locked until the OpMode ends. */
    public void lock() {
        locked = true;
        applyPosition(lockPosition);
    }

    /**
     * Move the lock position by `delta` (e.g. +0.05 or -0.05) and hold there.
     * The servos stay locked: they move to the new spot and hold it.
     */
    public void nudge(double delta) {
        double oldPosition = lockPosition;
        lockPosition = Math.max(0.0, Math.min(1.0, lockPosition + delta));
        lock();

        String direction = delta > 0 ? "UP" : "DOWN";
        if (lockPosition == oldPosition) {
            lastChange = String.format("%s pressed: already at limit %.2f", direction, lockPosition);
        } else {
            lastChange = String.format("%s: %.2f -> %.2f", direction, oldPosition, lockPosition);
        }
    }

    /** Describes the most recent D-pad up/down change, for telemetry. */
    public String getLastChange() {
        return lastChange;
    }

    /**
     * Gamepad controls, shared by Auto and TeleOp. Call once per loop.
     *   lockButton -> lock in place
     *   upButton   -> move +SHOOTER_SERVO_STEP and stay locked
     *   downButton -> move -SHOOTER_SERVO_STEP and stay locked
     * Each press acts once, even if the button is held.
     */
    public void handleButtons(boolean lockButton, boolean upButton, boolean downButton) {
        if (lockButton && !lastLock) lock();
        if (upButton && !lastUp) nudge(Config.SHOOTER_SERVO_STEP);
        if (downButton && !lastDown) nudge(-Config.SHOOTER_SERVO_STEP);
        lastLock = lockButton;
        lastUp = upButton;
        lastDown = downButton;
    }

    public boolean isLocked() {
        return locked;
    }

    /** Position the servos are locked at (0.0 to 1.0). */
    public double getLockPosition() {
        return lockPosition;
    }

    /** Called every loop. While locked, keeps re-sending the lock position so the servos hold. */
    public void update() {
        if (locked) {
            applyPosition(lockPosition);
        }
    }

    private void applyPosition(double pos) {
        if (leftServo != null) {
            leftServo.setPosition(pos);
        }
        if (rightServo != null) {
            rightServo.setPosition(pos);
        }
    }

    public boolean isLeftAvailable() {
        return leftServo != null;
    }

    public boolean isRightAvailable() {
        return rightServo != null;
    }
}
