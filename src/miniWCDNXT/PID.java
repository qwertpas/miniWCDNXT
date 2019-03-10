package miniWCDNXT;

public class PID {

    //constants
    double kP, kI, kD = 0;
    double tolerance;


    double currentValue, target, error, lastError;
    double P, I, D, power = 0;


    Boolean initialized = false;
    Boolean inTolerance = false;
    
    
    public PID(double kP, double kI, double kD, double tolerance){
        this.kP = kP;
        this.kI = kI;
        this.kI = kD;
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
            I = 0;
            inTolerance = true;
        }else{
            inTolerance = false;
        }
        
        P = kP * error;
        I = kI * (I + error);
        D = kI * (lastError - error);


        power = P + I + D;
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










}