package goth.motionprofiling.wrappers;

import java.util.ArrayList;
import goth.motionprofiling.general.Constants;

public class PIDController {
	
	private double p, i, d;
	
	public PIDController() {
		p = 0.0;
		i = 0.0;
		d = 0.0;
	}
	
	public PIDController(double p, double i, double d) {
		this.p = p;
		this.i = i;
		this.d = d;
	}
	
	public double getPIDAdj(double error) {
		return error * p;
	}
	
	public double getPIDAdj(ArrayList<Double> error) {
		double currentError = error.get(error.size() - 1);
		double errorSum = 0;
//		for(int i = 1; i < error.size(); i++) {
//			errorSum += error.get(error.size() - 1);
//		}	
		if(error.size() > 5) {
			for(int i = 1; i <= 20; i++) {
				errorSum += error.get(error.size() - 1);
			}	
		}
		double errorROC = 0;
		if(error.size() > 1) {
			errorROC = (error.get(error.size() - 1) - error.get(error.size() - 2)) / Constants.PROFILE_TIME_INTERVAL;
		}
		return (currentError * p) + (errorSum * i) - (errorROC * d);
	}
	
	public double getP() {
		return p;
	}
	
	public double getI() {
		return i;
	}
	
	public double getD() {
		return d;
	}
}