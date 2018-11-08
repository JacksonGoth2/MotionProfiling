package goth.motionprofiling.profiles;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import goth.motionprofiling.general.Constants;
import goth.motionprofiling.general.Point;
import goth.motionprofiling.wrappers.Profile;

public class TrapezoidalProfile implements Profile {

	private double distance;
	private double maxVelocity;
	private double acceleration;
	private ArrayList<Point> profile;
	
	public TrapezoidalProfile(double d, double mV, double a) {
		profile = new ArrayList<Point>();
		distance = d;
		maxVelocity = mV;
		acceleration = a;
	}
	
	public void calculate() {
		profile.clear();
		profile.add(new Point(0, 0));
		
		Point prevPoint = profile.get(0);
		while(profile.get(profile.size() - 1).getPos() < distance / 2) {
			double newVel;
			if(prevPoint.getVel() < maxVelocity) {
				newVel = prevPoint.getVel() + (acceleration * Constants.PROFILE_TIME_INTERVAL);
				if(newVel > maxVelocity) {
					newVel = maxVelocity;
				}
			} else {
				newVel = maxVelocity;
			}
			
			double newPos = prevPoint.getPos() + (prevPoint.getVel() * Constants.PROFILE_TIME_INTERVAL);
			if(newPos > distance / 2) {
				break;
			}
			
			profile.add(new Point(newPos, newVel));
			prevPoint = profile.get(profile.size() - 1);
		}
		
		int sizeHalfProfile = profile.size();
		for(int i = sizeHalfProfile; i > 0; i--) {
			Point reversePoint = profile.get(i - 1);
			double newPos = profile.get(profile.size() - 1).getPos() + (reversePoint.getVel() * Constants.PROFILE_TIME_INTERVAL);
			profile.add(new Point(newPos, reversePoint.getVel()));
		}
	}
	
	public void writeVelocityLogFile(String fileName, ArrayList<Point> profile) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			for(int i = 0; i < profile.size(); i++) {
				pw.write(profile.get(i).getVel() + ",");
			}
			pw.close();
		} catch(FileNotFoundException e) {
			System.out.println("ERROR: Cannot write to "+ fileName + "!");
		} catch(IOException e) {
			System.out.println("ERROR: Cannot read from " + fileName + "!");
		}
	}
	
	public void writePositionLogFile(String fileName, ArrayList<Point> profile) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			for(int i = 0; i < profile.size(); i++) {
				pw.write(profile.get(i).getPos() + ",");
			}
			pw.close();
		} catch(FileNotFoundException e) {
			System.out.println("ERROR: Cannot write to "+ fileName + "!");
		} catch(IOException e) {
			System.out.println("ERROR: Cannot read from " + fileName + "!");
		}
	}
	
	public ArrayList<Point> getProfile() {
		return profile;
	}
	
	public double getVel(double seconds) {
		int profileLength = (int) (profile.size() * Constants.PROFILE_TIME_INTERVAL);
		if(seconds <= profileLength) {
			return profile.get((int) (seconds / Constants.PROFILE_TIME_INTERVAL)).getVel();
		} else {
			return profile.get(profile.size() - 1).getVel();
		}
	}
	
	public double getPos(double seconds) {
		int profileLength = (int) (profile.size() * Constants.PROFILE_TIME_INTERVAL);
		if(seconds <= profileLength) {
			return profile.get((int) (seconds / Constants.PROFILE_TIME_INTERVAL)).getPos();
		} else {
			return profile.get(profile.size() - 1).getPos();
		}
	}
	
	public double getError() {
		return Math.abs(profile.get(profile.size() - 1).getPos() - distance);
	}
	
	public double getGoalDistance() {
		return distance;
	}
	
	public double getMaxVelocity() {
		return maxVelocity;
	}
	
	public double getAcceleration() {
		return acceleration;
	}
}
