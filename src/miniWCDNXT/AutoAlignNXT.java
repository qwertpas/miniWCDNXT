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
	
	static final double shiftDist = 10.0; //desired shift dist
	static final double y = 10.0; //initial distance to target

	static double turnAngle; //calculated angle that robot turns in order to shift, in radians
	static double arcLength; //calculated distance wheel needs to travel in each step
	static double forwardDist; //distance still needed to reach target after shift
	static double initLeftTick, initRightTick;
	static double leftDist, rightDist;
	static double leftPower, rightPower = 0;
	static int turnCount = 0;
	
	static PID leftPID;
	static PID rightPID;


	public static void main(String[] args) {
		System.out.println("Motors Initializing");
		leftDrive = new NXTMotor(MotorPort.C);
		rightDrive = new NXTMotor(MotorPort.A);
		leftDrive.backward();
		rightDrive.backward();
		System.out.println("Motors Initialized");
		
		turnAngle = Math.acos(1 - (shiftDist/wheelbase));
		arcLength = wheelbase * turnAngle;
		forwardDist = y - (wheelbase * Math.sin(turnAngle));
		
		System.out.println("arc:" + arcLength + " d:" + forwardDist + "ang:" + turnAngle);
		
		
		
        Sound.beepSequenceUp();   // make sound when ready.

        
        while(true){
        	
        	Button.waitForAnyPress(); //BUTTON PRESS
        	turnCount = 0;
        	resetEncoders();
        	leftPID = new PID(6.0,0.006,0,0.5);
    		rightPID = new PID(6.0,0.006,0,0.5);

            while(turnCount != 3){

            	updateEncoders();
            	
            	leftPower = limit(leftPower, 1);
            	rightPower = limit(rightPower, 1);


            	//System.out.println(leftDist + "," + rightDist + ";" + turnCount);
            	
            	execute();
            	leftDrive.setPower((int) Math.round(leftPower));
            	rightDrive.setPower((int) Math.round(rightPower));
            }
            
            System.out.println("DONE!");
        }
        
        
	}

	static void execute() {

		if (arcLength > 0) { //SHIFT RIGHT
			if (turnCount == 0) { // step 0: left wheel forward
				leftPID.loop(leftDist, arcLength);
				if (!leftPID.inTolerance()){
					leftPower = leftPID.getPower();
				} else {
					leftPower = 0;
					resetEncoders();
					updateEncoders();
					turnCount = 1;
				}
			}

			if (turnCount == 1) { // step 1: right wheel forward
				rightPID.loop(rightDist, arcLength);
				if (!rightPID.inTolerance()){
					rightPower = rightPID.getPower();
				} else {
					rightPower = 0;
					resetEncoders();
					updateEncoders();
					turnCount = 2;
				}
			}
			
			if (turnCount == 2) { //both wheels forward
				leftPID.loop(leftDist, forwardDist);
				rightPID.loop(rightDist, forwardDist);
				if (!leftPID.inTolerance() || !rightPID.inTolerance()) {
					leftPower = leftPID.getPower();
					rightPower = rightPID.getPower();

				} else {
					leftPower = 0;
					rightPower = 0;
					turnCount = 3;
				}
			}
			
		System.out.println(Math.round(leftPID.getError()) + ";" + Math.round(rightPID.getError()));

			
			

			

		} else {
			System.out.println("Shifting LEFT");
		}
	}
	
	 static void resetEncoders() {
		initLeftTick = leftDrive.getTachoCount();
	    initRightTick = rightDrive.getTachoCount();
	}
	
	static void updateEncoders() {
	    leftDist = -(leftDrive.getTachoCount() - initLeftTick) * inchesPerTick;
    	rightDist = -(rightDrive.getTachoCount() - initRightTick) * inchesPerTick;
	}
	
	public static double limit(double val, double limit) {
		if (val > limit) {
			return limit;
		} else if (val < -limit) {
			return -limit;
		} else {
			return val;
		}
	}

}
