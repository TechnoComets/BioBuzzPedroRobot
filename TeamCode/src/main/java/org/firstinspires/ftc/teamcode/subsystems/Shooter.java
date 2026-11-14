
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Shooter mechanism: one motor that launches game pieces.
 */
public class Shooter {
    private final DcMotorEx motor;

    private boolean shooterOn = false;
    private boolean previousCircle = false;

    public Shooter(HardwareMap hardwareMap) {
        motor = hardwareMap.tryGet(DcMotorEx.class, Config.SHOOTER_MOTOR_NAME);

        if (motor != null) {
            motor.setDirection(Config.SHOOTER_DIRECTION);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /** True if the shooter motor was found in Robot Configuration. */
    public boolean isAvailable() {
        return motor != null;
    }

    /**
     * Toggle shooter using Circle.
     * Call repeatedly inside the TeleOp loop.
     */
    public void update(boolean circlePressed) {
        if (circlePressed && !previousCircle) {
            shooterOn = !shooterOn;
        }

        previousCircle = circlePressed;

        if (shooterOn) {
            run();
        } else {
            stop();
        }
    }

    /** Start the shooter. */
    public void run() {
        setPower(Config.SHOOTER_POWER);
    }

    /** Reverse the shooter. */
    public void reverse() {
        setPower(Config.SHOOTER_OUTTAKE_POWER);
    }

    /** Stop the shooter. */
    public void stop() {
        setPower(0);
    }

    /** Set motor power manually. */
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
