package miniWCDNXT;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.Sound;

public class AutoAlignNXT {

	static NXTMotor leftDrive;
	static NXTMotor rightDrive;

	static double shiftDist = -500;
	static int speed = 30; // -100 to 100 percent power
	static int turnCount = 0;
	static double initLeftDist, initRightDist;
	static double leftDist, rightDist;
	static int leftPower, rightPower = 0;

	public static void main(String[] args) {
		System.out.println("Motors Initializing");
		NXTMotor leftDrive = new NXTMotor(MotorPort.C);
		NXTMotor rightDrive = new NXTMotor(MotorPort.A);
		leftDrive.backward();
		rightDrive.backward();
		System.out.println("Motors Initialized");
		
		
	    
		
        Sound.beepSequenceUp();   // make sound when ready.

        
        while(true){
//        	Button.waitForAnyPress(); //BUTTON PRESS
        	turnCount = 0;
        	initLeftDist = leftDrive.getTachoCount();
    	    initRightDist = rightDrive.getTachoCount();
    	    System.out.println("SHIFT initLeftDist: " + initLeftDist);
    	    System.out.println("SHIFT initRightDist: " + initRightDist);

            while(turnCount != 4){

            	leftDist = -(leftDrive.getTachoCount() - initLeftDist);
            	rightDist = -(rightDrive.getTachoCount() - initRightDist);

            	System.out.println(leftPower + ", " + rightPower + "; " + turnCount);
            	
            	execute();
            	leftDrive.setPower(leftPower);
            	rightDrive.setPower(rightPower);
            }
            
            System.out.println("DONE!");
        }
        
        
	}

	static void execute() {

		if (shiftDist > 0) {
			System.out.println("Shifting RIGHT");

			if (turnCount == 0) { // step 0: left wheel back
				if (leftDist  < shiftDist) {
					leftPower = -speed;
				} else {
					leftPower = 0;
					turnCount = 1;
				}
			}

			if (turnCount == 1) { // step 1: right wheel back
				if (rightDist < shiftDist) {
					rightPower = -speed;
				} else {
					rightPower = 0;
					turnCount = 2;
				}
			}

			if (turnCount == 2) { // step 2: left wheel forward
				if (leftDist < 0) {
					leftPower = speed;
				} else {
					leftPower = 0;
					turnCount = 3;
				}
			}

			if (turnCount == 3) { // step 3: right wheel forward
				if (rightDist < 0) {
					rightPower = speed;
				} else {
					rightPower = 0;
					turnCount = 4;
				}
			}

		} else {
			System.out.println("Shifting LEFT");

			if (turnCount == 0) { // step 0: right wheel back
				if (rightDist > shiftDist) {
					rightPower = -speed;
				} else {
					rightPower = 0;
					turnCount = 1;
				}
			}

			if (turnCount == 1) { // step 1: left wheel back
				if (leftDist > shiftDist) {
					leftPower = -speed;
				} else {
					leftPower = 0;
					turnCount = 2;
				}
			}

			if (turnCount == 2) { // step 2: right wheel forward
				if (rightDist < 0) {
					rightPower = speed;
				} else {
					rightPower = 0;
					turnCount = 3;
				}
			}

			if (turnCount == 3) { // step 3: left wheel forward
				if (leftDist < 0) {
					leftPower = speed;
				} else {
					leftPower = 0;
					turnCount = 4;
				}
			}

		}
	}

}
