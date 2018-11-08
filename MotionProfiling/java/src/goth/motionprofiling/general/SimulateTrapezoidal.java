package goth.motionprofiling.general;

import java.io.File;
import java.util.Random;
import goth.motionprofiling.part.Motor;
import goth.motionprofiling.profiles.TrapezoidalProfile;

public class SimulateTrapezoidal {

	public static void main(String[] args) {
		
		String pythonWorkspaceLocation = "C:\\Users\\jacks\\python-workspace\\";
		File keyFolder = new File(pythonWorkspaceLocation + "profiles\\keys\\");
		keyFolder.mkdirs();
		
		int numProfiles = 100;
		double randomDistance[] = new double[numProfiles];
		double randomMaxVelocity[] = new double[numProfiles];
		double randomAcceleration[] = new double[numProfiles];
		
		Random generator = new Random();
		for(int i = 0; i < numProfiles; i++) {
			double distance = generator.nextInt(20) + 40;
			randomDistance[i] = distance;
			double maxVelocity = (generator.nextInt(10) / 6) + 1;
			randomMaxVelocity[i] = maxVelocity;
			double acceleration = (generator.nextInt(4) / 16) + 0.1;
			randomAcceleration[i] = acceleration;
		}
		
		double cumulativeErrorScore = 0;
		Motor motor = new Motor(0, 0.01, 0, 0, 0);
		for(int i = 0; i < numProfiles; i++) {
			TrapezoidalProfile profile = new TrapezoidalProfile(randomDistance[i], randomMaxVelocity[i], randomAcceleration[i]);
			cumulativeErrorScore += motor.runProfile(profile, pythonWorkspaceLocation + "profiles\\prof" + i + ".txt");
		}
		double errorScore = cumulativeErrorScore / numProfiles;
		
		System.out.println("Cumulative Error Score: " + cumulativeErrorScore);
		System.out.println("Error Score: " + errorScore);
	}
}
