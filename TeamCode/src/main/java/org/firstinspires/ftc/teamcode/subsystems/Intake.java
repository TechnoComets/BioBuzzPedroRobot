
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Intake mechanism: one motor that pulls game pieces in or pushes them out.
 */
public class Intake {
    private final DcMotorEx motor;

    private boolean intakeOn = false;
    private boolean previousCircle = false;

    public Intake(HardwareMap hardwareMap) {
        motor = hardwareMap.tryGet(DcMotorEx.class, Config.INTAKE_MOTOR_NAME);

        if (motor != null) {
            motor.setDirection(Config.INTAKE_DIRECTION);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public boolean isAvailable() {
        return motor != null;
    }

    /**
     * Toggle intake using the Circle button.
     * Call this repeatedly inside the TeleOp loop.
     */
    public void update(boolean circlePressed) {
        if (circlePressed && !previousCircle) {
            intakeOn = !intakeOn;
        }

        previousCircle = circlePressed;

        if (intakeOn) {
            run();
        } else {
            stop();
        }
    }

    public void run() {
        setPower(Config.INTAKE_POWER);
    }

    public void reverse() {
        setPower(Config.OUTTAKE_POWER);
    }

    public void stop() {
        setPower(0);
    }

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
