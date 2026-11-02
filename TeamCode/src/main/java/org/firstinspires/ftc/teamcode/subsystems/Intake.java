package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Intake mechanism: one motor that pulls game pieces in (or pushes them out).
 *
 * If the motor is not in the Robot Configuration yet, the intake simply does nothing
 * (isAvailable() returns false) instead of crashing the whole OpMode.
 */
public class Intake {
    private final DcMotorEx motor; // null if not configured

    public Intake(HardwareMap hardwareMap) {
        motor = hardwareMap.tryGet(DcMotorEx.class, Config.INTAKE_MOTOR_NAME);
        if (motor != null) {
            motor.setDirection(Config.INTAKE_DIRECTION);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /** True if the intake motor was found in the Robot Configuration. */
    public boolean isAvailable() {
        return motor != null;
    }

    /** Pull a game piece in. */
    public void run() {
        setPower(Config.INTAKE_POWER);
    }

    /** Push a game piece out (also useful for clearing jams). */
    public void reverse() {
        setPower(Config.OUTTAKE_POWER);
    }

    public void stop() {
        setPower(0);
    }

    /** Manual control, e.g. from a gamepad trigger. Range -1.0 to 1.0. */
    public void setPower(double power) {
        if (motor != null) {
            motor.setPower(power);
        }
    }

    public double getPower() {
        return motor != null ? motor.getPower() : 0;
    }

    public boolean isRunning() {
        return Math.abs(getPower()) > 0.01;
    }
}
