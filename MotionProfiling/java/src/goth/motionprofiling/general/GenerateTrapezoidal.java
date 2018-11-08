package goth.motionprofiling.general;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import goth.motionprofiling.profiles.TrapezoidalProfile;

public class GenerateTrapezoidal {

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
			double distance = generator.nextInt(20) + 20;
			randomDistance[i] = distance;
			double maxVelocity = (generator.nextInt(10) / 2) + 1;
			randomMaxVelocity[i] = maxVelocity;
			double acceleration = (generator.nextInt(4) / 8) + 0.1;
			randomAcceleration[i] = acceleration;
		}
		
		double cumulativeErrorScore = 0;
		for(int i = 0; i < numProfiles; i++) {
			TrapezoidalProfile profile = new TrapezoidalProfile(randomDistance[i], randomMaxVelocity[i], randomAcceleration[i]);
			profile.calculate();
			cumulativeErrorScore += profile.getError();
			profile.writeVelocityLogFile(pythonWorkspaceLocation + "profiles\\prof" + i + "-velocity.txt", profile.getProfile());
			profile.writePositionLogFile(pythonWorkspaceLocation + "profiles\\prof" + i + "-position.txt", profile.getProfile());
			writeKeyLogFile(pythonWorkspaceLocation + "profiles\\keys\\prof" + i + "-key.txt", randomDistance[i], randomMaxVelocity[i], randomAcceleration[i]);
		}
		double errorScore = cumulativeErrorScore / numProfiles;
		
		System.out.println("Cumulative Error Score: " + cumulativeErrorScore);
		System.out.println("Error Score: " + errorScore);
	}
	
	private static void writeKeyLogFile(String fileName, double distance, double maxVelocity, double acceleration) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.write(distance + "," + maxVelocity + "," + acceleration);
			pw.close();
		} catch(FileNotFoundException e) {
			System.out.println("ERROR: Cannot write to "+ fileName + "!");
		} catch(IOException e) {
			System.out.println("ERROR: Cannot read from " + fileName + "!");
		}
	}
}
