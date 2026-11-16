package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config;

/**
 * ShooterServos Subsystem: Manages the two high-torque gate/hood servos ("ShooterServoLeft" and "ShooterServoRight")
 * that determine and maintain the size of the shooter opening.
 * 
 * Because the force and pressure of the flywheel can push or warp the opening during operation,
 * `update()` is called every loop cycle to constantly re-assert the target position. If anything 
 * (like flywheel pressure) tries to move the servos, the code instantly snaps/holds them back to the set position.
 */
public class ShooterServos {
    private final Servo leftServo;
    private final Servo rightServo;

    // The desired fixed opening position (0.0 to 1.0)
    private double targetPosition = Config.SHOOTER_DEFAULT_OPENING_POSITION;

    public ShooterServos(HardwareMap hardwareMap) {
        leftServo = hardwareMap.tryGet(Servo.class, Config.SHOOTER_LEFT_SERVO_NAME);
        rightServo = hardwareMap.tryGet(Servo.class, Config.SHOOTER_RIGHT_SERVO_NAME);

        // Two servos on opposite sides of the shooter usually face opposite directions. If they
        // do, the same position command makes them fight each other and neither moves.
        // Reverse one in Config so they turn the same way.
        if (leftServo != null && Config.SHOOTER_LEFT_SERVO_REVERSED) {
            leftServo.setDirection(Servo.Direction.REVERSE);
        }
        if (rightServo != null && Config.SHOOTER_RIGHT_SERVO_REVERSED) {
            rightServo.setDirection(Servo.Direction.REVERSE);
        }
        
        applyPosition(targetPosition);
    }

    /** True if at least one shooter servo was found in the Robot Configuration. */
    public boolean isAvailable() {
        return leftServo != null || rightServo != null;
    }

    /** Set the desired opening size position (0.0 to 1.0). */
    public void setPosition(double position) {
        targetPosition = Math.max(0.0, Math.min(1.0, position));
        applyPosition(targetPosition);
    }

    /** 
     * Called automatically every loop cycle to constantly lock the opening size in place 
     * against the flywheel's pressure.
     */
    public void update() {
        applyPosition(targetPosition);
    }

    private void applyPosition(double pos) {
        if (leftServo != null) {
            leftServo.setPosition(pos);
        }
        if (rightServo != null) {
            rightServo.setPosition(pos);
        }
    }

    public double getTargetPosition() {
        return targetPosition;
    }

    /** Position last sent to the left servo, or NaN if it was not found. */
    public double getLeftPosition() {
        return leftServo != null ? leftServo.getPosition() : Double.NaN;
    }

    /** Position last sent to the right servo, or NaN if it was not found. */
    public double getRightPosition() {
        return rightServo != null ? rightServo.getPosition() : Double.NaN;
    }

    public boolean isLeftAvailable() {
        return leftServo != null;
    }

    public boolean isRightAvailable() {
        return rightServo != null;
    }
}
