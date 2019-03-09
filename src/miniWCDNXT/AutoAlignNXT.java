package miniWCDNXT;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.Sound;

public class AutoAlignNXT {

	static NXTMotor leftDrive;
	static NXTMotor rightDrive;
	
	static final double ticksPerInch = 1000.0/14.3; //tested on LEGO technic balloon wheels
	static final double inchesPerTick = 14.3/1000.0; //tested on LEGO technic balloon wheels
	static final double wheelbase = 6.5; //wheelbase diameter/width in inches
	
	static final double shiftDist = 3.0; //desired shift dist
	static final double y = 10.0; //initial distance to target
	static final int speed = 30; // -100 to 100 percent power

	static double turnAngle; //calculated angle that robot turns in order to shift, in radians
	static double arcLength; //calculated distance wheel needs to travel in each step
	static double forwardDist; //distance still needed to reach target after shift
	static double initLeftTick, initRightTick;
	static double leftDist, rightDist;
	static int leftPower, rightPower = 0;
	static int turnCount = 0;


	public static void main(String[] args) {
		System.out.println("Motors Initializing");
		NXTMotor leftDrive = new NXTMotor(MotorPort.C);
		NXTMotor rightDrive = new NXTMotor(MotorPort.A);
		leftDrive.backward();
		rightDrive.backward();
		System.out.println("Motors Initialized");
		
		turnAngle = Math.acos(1 - (shiftDist/wheelbase));
		arcLength = wheelbase * turnAngle;
		forwardDist = y - (turnAngle * Math.sin(turnAngle));
		
		System.out.println("arc:" + arcLength + " d:" + forwardDist);

	    
		
        Sound.beepSequenceUp();   // make sound when ready.

        
        while(true){
        	Button.waitForAnyPress(); //BUTTON PRESS
        	turnCount = 0;
        	initLeftTick = leftDrive.getTachoCount();
    	    initRightTick = rightDrive.getTachoCount();
    	    System.out.println("SHIFT initLeftDist: " + initLeftTick);
    	    System.out.println("SHIFT initRightDist: " + initRightTick);

            while(turnCount != 4){

            	leftDist = -(leftDrive.getTachoCount() - initLeftTick) * inchesPerTick;
            	rightDist = -(rightDrive.getTachoCount() - initRightTick) * inchesPerTick;

            	System.out.println(leftDist + "," + rightDist + ";" + turnCount);
            	
            	execute();
            	leftDrive.setPower(leftPower);
            	rightDrive.setPower(rightPower);
            }
            
            System.out.println("DONE!");
        }
        
        
	}

	static void execute() {

		if (arcLength > 0) {
			//System.out.println("Shifting RIGHT");

			if (turnCount == 0) { // step 0: left wheel forward
				if (Math.abs(leftDist)  < arcLength) {
					leftPower = speed;
				} else {
					leftPower = 0;
					turnCount = 1;
				}
			}

			if (turnCount == 1) { // step 1: right wheel forward
				if (Math.abs(rightDist) < arcLength) {
					rightPower = speed;
				} else {
					rightPower = 0;
					turnCount = 2;
				}
			}
			
			

			

		} else {
			System.out.println("Shifting LEFT");

			if (turnCount == 0) { // step 0: right wheel back
				if (rightDist > arcLength) {
					rightPower = -speed;
				} else {
					rightPower = 0;
					turnCount = 1;
				}
			}

			if (turnCount == 1) { // step 1: left wheel back
				if (leftDist > arcLength) {
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
