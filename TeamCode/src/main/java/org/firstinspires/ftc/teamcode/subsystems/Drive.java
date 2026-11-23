package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Manual mecanum drive for TeleOp.
 *
 * Drives the four wheel motors directly (same names and directions as pedro/Constants.java),
 * so it does not depend on Pedro's TeleOp API. Do NOT use this at the same time as a Pedro
 * path-follow: only one of them should command the motors.
 */
public class Drive {
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;

    public Drive(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, Config.FRONT_LEFT_NAME);
        frontRight = hardwareMap.get(DcMotorEx.class, Config.FRONT_RIGHT_NAME);
        backLeft = hardwareMap.get(DcMotorEx.class, Config.BACK_LEFT_NAME);
        backRight = hardwareMap.get(DcMotorEx.class, Config.BACK_RIGHT_NAME);

        frontLeft.setDirection(Config.FRONT_LEFT_DIRECTION);
        frontRight.setDirection(Config.FRONT_RIGHT_DIRECTION);
        backLeft.setDirection(Config.BACK_LEFT_DIRECTION);
        backRight.setDirection(Config.BACK_RIGHT_DIRECTION);

        for (DcMotorEx m : new DcMotorEx[]{frontLeft, frontRight, backLeft, backRight}) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    /**
     * @param forward  +1 = forward
     * @param strafe   +1 = right
     * @param turn     +1 = clockwise (turn right)
     * @param scale    overall speed multiplier (1.0 = full, e.g. 0.4 for slow mode)
     */
    public void drive(double forward, double strafe, double turn, double scale) {
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;

        // Keep the ratios between wheels when any value would exceed 1.0
        double max = Math.max(1.0, Math.max(Math.max(Math.abs(fl), Math.abs(fr)),
                Math.max(Math.abs(bl), Math.abs(br))));

        frontLeft.setPower(fl / max * scale);
        frontRight.setPower(fr / max * scale);
        backLeft.setPower(bl / max * scale);
        backRight.setPower(br / max * scale);
    }

    public void stop() {
        drive(0, 0, 0, 0);
    }
}
