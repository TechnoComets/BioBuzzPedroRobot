/*

package org.firstinspires.ftc.teamcode.paths.ManualAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "BlueFront", group = "Competition")
public class BlueFront extends LinearOpMode {

    private DcMotor leftFront, leftBack;
    private DcMotor rightFront, rightBack;
    private DcMotor intake, transfer;
    private DcMotorEx shooter;

    // Drivetrain measurements
    private static final double WHEEL_DIAMETER_MM = 104.0;

    // Drive power
    private static final double DRIVE_POWER = 0.50;

    // Shooter power
    private static final double SHOOTER_POWER = 0.80;

    // Timing
    private static final long SHOOTER_SPINUP_TIME = 2000;
    private static final long FEED_TIME = 3000;

    @Override
    public void runOpMode() {

        // ==================================
        // INITIALIZE HARDWARE
        // ==================================

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");

        // ==================================
        // MOTOR DIRECTIONS
        // ==================================

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);

        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // ==================================
        // BRAKE SETTINGS
        // ==================================

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // ==================================
        // RESET ENCODERS
        // ==================================

        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Shooter without PID
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        stopAllMotors();

        telemetry.addData("Status", "READY");
        telemetry.addData("Shooter Power", "80%");
        telemetry.addData("Strafe Distance", "30 inches LEFT");
        telemetry.addData("Forward Distance", "50 inches");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // ==================================
        // STEP 1: SPIN UP SHOOTER
        // ==================================

        telemetry.addData("Status", "SPINNING UP SHOOTER");
        telemetry.update();

        shooter.setPower(SHOOTER_POWER);

        sleep(SHOOTER_SPINUP_TIME);

        if (!opModeIsActive()) {
            stopAllMotors();
            return;
        }

        // ==================================
        // STEP 2: SHOOT THREE BALLS
        // ==================================

        telemetry.addData("Status", "SHOOTING BALLS");
        telemetry.update();

        intake.setPower(1.0);
        transfer.setPower(1.0);

        sleep(FEED_TIME);

        intake.setPower(0);
        transfer.setPower(0);

        if (!opModeIsActive()) {
            stopAllMotors();
            return;
        }

        shooter.setPower(0);

        sleep(300);

        // ==================================
        // STEP 3: STRAFE LEFT 30 INCHES
        // ==================================

        telemetry.addData("Status", "STRAFING LEFT 30 INCHES");
        telemetry.update();

        if (!strafeLeftInches(30.0, DRIVE_POWER)) {
            stopAllMotors();
            return;
        }

        // ==================================
        // STEP 4: DRIVE FORWARD 50 INCHES
        // ==================================

        telemetry.addData("Status", "DRIVING FORWARD 50 INCHES");
        telemetry.update();

        if (!driveForwardInches(50.0, DRIVE_POWER)) {
            stopAllMotors();
            return;
        }

        // ==================================
        // STEP 5: AUTONOMOUS COMPLETE
        // ==================================

        stopAllMotors();

        telemetry.addData("Status", "AUTONOMOUS COMPLETE");
        telemetry.update();
    }

    // ==================================
    // DRIVE FORWARD
    // ==================================

    private boolean driveForwardInches(double inches, double power) {

        double ticksPerRev =
                leftFront.getMotorType().getTicksPerRev();

        double wheelCircumference =
                Math.PI * (WHEEL_DIAMETER_MM / 25.4);

        int ticks = (int) Math.round(
                (inches / wheelCircumference) * ticksPerRev
        );

        int lf = leftFront.getCurrentPosition() + ticks;
        int lb = leftBack.getCurrentPosition() + ticks;
        int rf = rightFront.getCurrentPosition() + ticks;
        int rb = rightBack.getCurrentPosition() + ticks;

        return runToPosition(lf, lb, rf, rb, power);
    }

    // ==================================
    // STRAFE LEFT
    // ==================================

    private boolean strafeLeftInches(double inches, double power) {

        double ticksPerRev =
                leftFront.getMotorType().getTicksPerRev();

        double wheelCircumference =
                Math.PI * (WHEEL_DIAMETER_MM / 25.4);

        int ticks = (int) Math.round(
                (inches / wheelCircumference) * ticksPerRev
        );

        int lf = leftFront.getCurrentPosition() - ticks;
        int lb = leftBack.getCurrentPosition() + ticks;
        int rf = rightFront.getCurrentPosition() + ticks;
        int rb = rightBack.getCurrentPosition() - ticks;

        return runToPosition(lf, lb, rf, rb, power);
    }

    // ==================================
    // RUN TO POSITION
    // ==================================

    private boolean runToPosition(
            int lf, int lb, int rf, int rb, double power) {

        if (!opModeIsActive()) return false;

        leftFront.setTargetPosition(lf);
        leftBack.setTargetPosition(lb);
        rightFront.setTargetPosition(rf);
        rightBack.setTargetPosition(rb);

        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        ElapsedTime movementTimer = new ElapsedTime();

        while (opModeIsActive() &&
                (leftFront.isBusy() ||
                        leftBack.isBusy() ||
                        rightFront.isBusy() ||
                        rightBack.isBusy()) &&
                movementTimer.seconds() < 30.0) {

            telemetry.addData("LF", leftFront.getCurrentPosition());
            telemetry.addData("LB", leftBack.getCurrentPosition());
            telemetry.addData("RF", rightFront.getCurrentPosition());
            telemetry.addData("RB", rightBack.getCurrentPosition());
            telemetry.update();

            idle();
        }

        boolean completed =
                opModeIsActive() &&
                        !leftFront.isBusy() &&
                        !leftBack.isBusy() &&
                        !rightFront.isBusy() &&
                        !rightBack.isBusy();

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return completed;
    }

    // ==================================
    // SET DRIVETRAIN MODE
    // ==================================

    private void setDriveMode(DcMotor.RunMode mode) {

        leftFront.setMode(mode);
        leftBack.setMode(mode);
        rightFront.setMode(mode);
        rightBack.setMode(mode);
    }

    // ==================================
    // STOP ALL MOTORS
    // ==================================

    private void stopAllMotors() {

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        intake.setPower(0);
        transfer.setPower(0);
        shooter.setPower(0);
    }
}
*/
