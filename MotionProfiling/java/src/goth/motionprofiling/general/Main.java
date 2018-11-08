package goth.motionprofiling.general;

import goth.motionprofiling.part.Motor;
import goth.motionprofiling.profiles.TrapezoidalProfile;

public class Main {

	public static void main(String[] args) {
		
		double distance = 35;
		double maxVel = 2;
		double acceleration = 0.5;
		
		TrapezoidalProfile profile = new TrapezoidalProfile(distance, maxVel, acceleration);
		
		double bestP = Double.MAX_VALUE;
		double bestCurrentP = 0.0;
//		TrapezoidalProfile profile = new TrapezoidalProfile(distance, maxVel, acceleration);
		for(double currentP = 0.75; currentP <= 1.25; currentP += 0.01) {
			double cumulativeErrorScore = 0;
			Motor motor = new Motor(0, 0.0005, currentP, 0, 0);
			for(int i = 0; i < 100; i++) {
				cumulativeErrorScore += motor.runProfile(profile);
			}
			double errorScore = cumulativeErrorScore / 100;
			if(errorScore < bestP) {
				bestP = errorScore;
				bestCurrentP = currentP;
			}
		}
//		System.out.println(bestP);
//		System.out.println(bestCurrentP);
		
		double bestD = Double.MAX_VALUE;
		double bestCurrentD = 0.0;
//		TrapezoidalProfile profile = new TrapezoidalProfile(distance, maxVel, acceleration);
		for(double currentD = 0.0; currentD <= 0.007; currentD += 0.00001) {
			double cumulativeErrorScore = 0;
			Motor motor = new Motor(0, 0.0005, bestCurrentP, 0, currentD);
			for(int i = 0; i < 100; i++) {
				cumulativeErrorScore += motor.runProfile(profile);
			}
			double errorScore = cumulativeErrorScore / 100;
			if(errorScore < bestD) {
				bestD = errorScore;
				bestCurrentD = currentD;
			}
		}
//		System.out.println(bestD);
//		System.out.println(bestCurrentD);
		
		double bestI = Double.MAX_VALUE;
		double bestCurrentI = 0.0;
//		TrapezoidalProfile profile = new TrapezoidalProfile(distance, maxVel, acceleration);
		for(double currentI = 0.0; currentI <= 0.01; currentI += 0.0001) {
			double cumulativeErrorScore = 0;
			Motor motor = new Motor(0, 0.0005, bestCurrentP, currentI, bestCurrentD);
			for(int i = 0; i < 100; i++) {
				cumulativeErrorScore += motor.runProfile(profile);
			}
			double errorScore = cumulativeErrorScore / 100;
			if(errorScore < bestI) {
				bestI = errorScore;
				bestCurrentI = currentI;
			}
		}
//		System.out.println(bestI);
//		System.out.println(bestCurrentI);
		
		double cumulativeError = 0;
//		TrapezoidalProfile profile = new TrapezoidalProfile(distance, maxVel, acceleration);
		Motor motor = new Motor(0, 0.0005, bestCurrentP, bestCurrentI, bestCurrentD);
		for(int i = 0; i < 1000; i++) {
			cumulativeError += motor.runProfile(profile);
		}
		double averageError = cumulativeError / 1000;
		System.out.println("P: " + bestCurrentP + " I: " + bestCurrentI + " D: " + bestCurrentD);
		System.out.println("Error: " + averageError);
	}
}
