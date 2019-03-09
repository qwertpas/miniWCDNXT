package miniWCDNXT;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.Sound;

public class TachoDist {

	static NXTMotor leftDrive;
	static NXTMotor rightDrive;

	static double d = 7; //wheelbase diameter/width in inches
	static double shiftDist = 3;
	static double arcLength; //wheel needs to travel this far each step
	
	static int speed = 30; // -100 to 100 percent power
	static int turnCount = 0;
	static int initLeftDist, initRightDist;
	static int leftDist, rightDist;
	static int leftPower, rightPower = 0;
	
	static final double ticksPerInch = 1000.0/14.3;

	public static void main(String[] args) {
		System.out.println("Motors Initializing");
		NXTMotor leftDrive = new NXTMotor(MotorPort.C);
		NXTMotor rightDrive = new NXTMotor(MotorPort.A);
		leftDrive.backward();
		rightDrive.backward();
		System.out.println("Motors Initialized");
		
		arcLength = d * Math.acos(1 - (shiftDist/d));
	    
		
        Sound.beepSequenceUp();   // make sound when ready.

        
        while(true){
        	Button.waitForAnyPress(); //BUTTON PRESS
        	turnCount = 0;
        	initLeftDist = leftDrive.getTachoCount();
    	    initRightDist = rightDrive.getTachoCount();
    	    System.out.println("SHIFT initLeftDist: " + initLeftDist);
    	    System.out.println("SHIFT initRightDist: " + initRightDist);

            while(turnCount != 1){

            	leftDist = -(leftDrive.getTachoCount() - initLeftDist);
            	rightDist = -(rightDrive.getTachoCount() - initRightDist);

            	
            	execute();
            	System.out.println(leftPower + "," + rightPower + ";" + leftDist + "," + rightDist + ";" + turnCount);

            	leftDrive.setPower(leftPower);
            	rightDrive.setPower(rightPower);
            }
            
            System.out.println("DONE!");
        }
        
        
	}

	static void execute() {
		if((leftDist + rightDist)/2.0 < (20*ticksPerInch)){
			leftPower = speed;
			rightPower = speed;
		}else{
			leftPower = 0;
			rightPower = 0;
			turnCount = 1;
		}
	}

}
