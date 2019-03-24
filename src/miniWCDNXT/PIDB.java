package miniWCDNXT;



public class PIDB {




    //constants
    double kP, kI, kD, kB = 0;
    double tolerance;


    double currentValue, target, error, lastError;
    double P, I, D, power = 0;


    Boolean initialized = false;
    Boolean inTolerance = false;
    
    
    public PIDB(double kP, double kI, double kD, double kB, double tolerance){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kB = kB;
        this.tolerance = tolerance;
    }


    public void loop(double currentValue, double target){
        this.currentValue = currentValue;
        this.target = target;
        if(!initialized){
            lastError = this.target;
            initialized = true;
        }
        error = this.target - this.currentValue;
        if (Math.abs(error) < tolerance){
            inTolerance = true;
        }else{
            inTolerance = false;
        }


        if(Math.signum(lastError) != Math.signum(error)){ //"bounces" back after reaching target, braking
            System.out.println("reset");
            I = -kB * I;
        }
        
        P = kP * error;
        I = I + (kI*error);
        D = kD * (error - lastError) / error;


        power = P + I + D;
        lastError = error;
    }


    public Boolean inTolerance(){
        return inTolerance;
    }


    public double getPower(){
        return power;
    }


    public double getError(){
        return error;
    }


    public void resetPID(){
        P=0;
        I=0;
        D=0;
    }


    public double getP(){
        return P;
    }
    public double getI(){
        return I;
    }
    public double getD(){
        return D;
    }










}