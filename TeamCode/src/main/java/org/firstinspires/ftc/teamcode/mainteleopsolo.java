package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class mainteleopsolo extends LinearOpMode {
    // declare motors and servos
    DcMotor FL, FR, BL, BR;
    DcMotor shooter;
    DcMotor wobbleArm;
    DcMotor collectionMechanism;
    DcMotor neat;
    Servo ringPusher;
    Servo wobbleGoalGrabber;

    public void runOpMode() {
        // initialization
        // hardwaremap
        // drivetrain
        FR = hardwareMap.get(DcMotor.class, "FR");
        FL = hardwareMap.get(DcMotor.class, "FL");
        BR = hardwareMap.get(DcMotor.class, "BR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        // mechanisms
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        collectionMechanism = hardwareMap.get(DcMotor.class, "collectionMechanism");
        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");
        neat = hardwareMap.get(DcMotor.class, "neat");
        // servos
        wobbleGoalGrabber = hardwareMap.get(Servo.class, "wobbleGoalGrabber");
        ringPusher = hardwareMap.get(Servo.class, "ringPusher");
        ringPusher.setPosition(0.95);
        wobbleGoalGrabber.setPosition(0.25);

        double shooterpow = 0.4;
        boolean prevpadright = false;
        boolean prevpadleft = false;
        double drivepow;
        double intakepow;
        boolean prevy = false;
        boolean prevx = false;
        boolean prevb = false;
        boolean intaketoggle = false;
        boolean shootertoggle = false;
        boolean clawtoggle = false;

        // teleop loop starts
        waitForStart();
        while (opModeIsActive()) {
            // slow drive button
            if (gamepad1.right_trigger < 0.1) drivepow = 1;
            else drivepow = 0.125;

            // mecanum stuff
            double r = Math.hypot(gamepad1.left_stick_x, -1 * gamepad1.left_stick_y);
            double robotAngle = Math.atan2(-1 * gamepad1.left_stick_y, gamepad1.left_stick_x) + -20;
            double rightX = gamepad1.right_stick_x;
            final double v1 = (r * Math.cos(robotAngle) + rightX) * drivepow;
            final double v2 = (r * Math.sin(robotAngle) - rightX) * drivepow;
            final double v3 = (r * Math.sin(robotAngle) + rightX) * drivepow;
            final double v4 = (r * Math.cos(robotAngle) - rightX) * drivepow;
            FL.setPower(v1);
            FR.setPower(v2);
            BL.setPower(v3);
            BR.setPower(v4);

            // shooter toggler
            // if b pressed and prevx is false
            if (gamepad1.b && !prevb) shootertoggle = !shootertoggle; // toggle shootertoggle when b is pressed
            prevb = gamepad1.b;

            if (shootertoggle) { // run shooter if shootertoggle is true
                shooter.setPower(-shooterpow);
            } else shooter.setPower(0);

            // change shooter power with dpad left and rigt
            if (gamepad1.dpad_right && !prevpadright) shooterpow += 0.05;
            prevpadright = gamepad1.dpad_right;
            if (gamepad1.dpad_left && !prevpadleft) shooterpow -= 0.05;
            prevpadleft = gamepad1.dpad_left;

            // reverse intake if a is pressed
            if (gamepad1.a) intakepow = -1;
            else intakepow = 1;

            // intake toggler
            if (gamepad1.x && !prevx) intaketoggle = !intaketoggle; // if x pressed and prevx is false
            prevx = gamepad1.x;
            if (intaketoggle) {
                collectionMechanism.setPower(intakepow);
                neat.setPower(intakepow);
            } else {
                collectionMechanism.setPower(0);
                neat.setPower(0);
            }
            // shoot a ring when a is pressed
            if (gamepad1.left_trigger > 0.1) ringPusher.setPosition(0.6);
            else ringPusher.setPosition(0.95);

            // toggle clawtoggle if y pressed
            if (gamepad1.y && !prevy) clawtoggle = !clawtoggle;
            prevy = gamepad1.y;
            // open wobble claw when toggle is true
            if (clawtoggle) wobbleGoalGrabber.setPosition(1);
            else wobbleGoalGrabber.setPosition(0.25);

            // control wobble arm with dpad
            if (gamepad1.dpad_up) wobbleArm.setPower(-0.25);
            else if (gamepad1.dpad_down) wobbleArm.setPower(0.35);
            else wobbleArm.setPower(0);

            telemetry.addData("shooterpow", shooterpow );
            telemetry.update();
        }
    }
}