package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Transfer mechanism: one DC motor that moves pollen from the intake onward through the robot.
 *
 * If the motor is not in the Robot Configuration yet, the transfer simply does nothing
 * (isAvailable() returns false) instead of crashing the whole OpMode.
 */
public class Transfer {
    private final DcMotorEx motor; // null if not configured

    public Transfer(HardwareMap hardwareMap) {
        motor = hardwareMap.tryGet(DcMotorEx.class, Config.TRANSFER_MOTOR_NAME);
        if (motor != null) {
            motor.setDirection(Config.TRANSFER_DIRECTION);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            // BRAKE so pollen stays where it is when the transfer stops instead of sliding back.
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    /** True if the transfer motor was found in the Robot Configuration. */
    public boolean isAvailable() {
        return motor != null;
    }

    /** Move pollen forward, away from the intake. */
    public void feed() {
        setPower(Config.TRANSFER_POWER);
    }

    /** Move pollen back toward the intake (clears jams). */
    public void reverse() {
        setPower(Config.TRANSFER_REVERSE_POWER);
    }

    public void stop() {
        setPower(0);
    }

    /** Manual control. Range -1.0 to 1.0. */
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
