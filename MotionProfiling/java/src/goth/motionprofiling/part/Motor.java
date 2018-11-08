package goth.motionprofiling.part;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import goth.motionprofiling.general.Constants;
import goth.motionprofiling.general.Point;
import goth.motionprofiling.profiles.TrapezoidalProfile;
import goth.motionprofiling.wrappers.PIDController;

public class Motor extends PIDController {

	private double maxVelocity;
	private double dragConstant;
	
	public Motor(double m, double dr, double p, double i, double d) {
		super(p, i, d);
		maxVelocity = m;
		dragConstant = dr;
	}
	
	public double runProfile(TrapezoidalProfile prof) {
		prof.calculate();
		ArrayList<Point> plannedVelocities = prof.getProfile();
		ArrayList<Point> simulatedVelocities = new ArrayList<Point>();
		ArrayList<Double> error = new ArrayList<Double>();
		
		double prevDrag = 0.0;
		Random generator = new Random();
		simulatedVelocities.add(new Point(0, 0));
		for(int i = 1; i < plannedVelocities.size(); i++) {
			
//			double newDrag = prevDrag;
//			int chooser = generator.nextInt(3);
//			if(chooser == 0) {
//				newDrag = prevDrag;
//			} else if(chooser == 1) {
//				if(prevDrag - dragConstant >= -Constants.DRAG_RANGE) {
//					newDrag = prevDrag - dragConstant;
//				}
//			} else {
//				if(prevDrag + dragConstant <= Constants.DRAG_RANGE) {
//					newDrag = prevDrag + dragConstant;
//				}
//			}
//			prevDrag = newDrag;
			
			double newDrag = prevDrag;
			int chooser = generator.nextInt(1000);
			if(chooser < 750) {
				newDrag = prevDrag;
			} else if(chooser < 875) {
				if(prevDrag - dragConstant >= -Constants.DRAG_RANGE) {
					newDrag = prevDrag - dragConstant;
				}
			} else {
				if(prevDrag + dragConstant <= Constants.DRAG_RANGE) {
					newDrag = prevDrag + dragConstant;
				}
			}
			prevDrag = newDrag;
			
			//double error = plannedVelocities.get(i - 1).getVel() - simulatedVelocities.get(i - 1).getVel();
			error.add(plannedVelocities.get(i - 1).getVel() - simulatedVelocities.get(i - 1).getVel());
			double simulatedVelocity = (plannedVelocities.get(i).getVel() * (1 - newDrag)) + getPIDAdj(error);
			double simulatedPosition = simulatedVelocities.get(i - 1).getPos() + (simulatedVelocity * Constants.PROFILE_TIME_INTERVAL);
			
			simulatedVelocities.add(new Point(simulatedPosition, simulatedVelocity));
		}
		return Math.abs(simulatedVelocities.get(simulatedVelocities.size() - 1).getPos() - prof.getGoalDistance());
	}
	
	public double runProfile(TrapezoidalProfile prof, String outputFileName) {
		prof.calculate();
		ArrayList<Point> plannedVelocities = prof.getProfile();
		ArrayList<Point> simulatedVelocities = new ArrayList<Point>();
		
		double prevDrag = 0.0;
		Random generator = new Random();
		simulatedVelocities.add(new Point(0, 0));
		for(int i = 1; i < plannedVelocities.size(); i++) {
			
			double newDrag = prevDrag;
			if(generator.nextInt(2) == 0) {
				if(prevDrag - dragConstant >= -Constants.DRAG_RANGE) {
					newDrag = prevDrag - dragConstant;
				}
			} else {
				if(prevDrag + dragConstant <= Constants.DRAG_RANGE) {
					newDrag = prevDrag + dragConstant;
				}
			}
			prevDrag = newDrag;
			
			double error = plannedVelocities.get(i - 1).getVel() - simulatedVelocities.get(i - 1).getVel();
			double simulatedVelocity = (plannedVelocities.get(i).getVel() * (1 - newDrag)) + getPIDAdj(error);
			double simulatedPosition = simulatedVelocities.get(i - 1).getPos() + (simulatedVelocity * Constants.PROFILE_TIME_INTERVAL);
			
			simulatedVelocities.add(new Point(simulatedPosition, simulatedVelocity));
		}
		
//		System.out.println(plannedVelocities.get(plannedVelocities.size() - 1).getPos());
//		System.out.println(simulatedVelocities.get(simulatedVelocities.size() - 1).getPos());
		writeSimulatedProfileLog(outputFileName, plannedVelocities, simulatedVelocities);
		//writeKeyLogFile(outputFileName, prof.getGoalDistance(), prof.getMaxVelocity(), prof.getAcceleration());
		return Math.abs(simulatedVelocities.get(simulatedVelocities.size() - 1).getPos() - prof.getGoalDistance());
	}
	
	private void writeSimulatedProfileLog(String fileName, ArrayList<Point> plannedVelocities, ArrayList<Point> simulatedVelocities) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			DecimalFormat dataFormatter = new DecimalFormat("#.#####");
			for(int i = 0; i < plannedVelocities.size(); i++) {
				String positionValues = dataFormatter.format(plannedVelocities.get(i).getPos()) + ":" + dataFormatter.format(simulatedVelocities.get(i).getPos());
				String velocityValues = dataFormatter.format(plannedVelocities.get(i).getVel()) + ":" + dataFormatter.format(simulatedVelocities.get(i).getVel());
				pw.write(positionValues + "-" + velocityValues + ",");
			}
			pw.close();
		} catch(FileNotFoundException e) {
			System.out.println("ERROR: Cannot write to "+ fileName + "!");
		} catch(IOException e) {
			System.out.println("ERROR: Cannot read from " + fileName + "!");
		}
	}
	
//	private static void writeKeyLogFile(String name, double distance, double maxVelocity, double acceleration) {
//		String fileName = name + "-key.txt";
//		try {
//			FileWriter fw = new FileWriter(new File(fileName));
//			BufferedWriter bw = new BufferedWriter(fw);
//			PrintWriter pw = new PrintWriter(bw);
//			pw.write(distance + "," + maxVelocity + "," + acceleration);
//			pw.close();
//		} catch(FileNotFoundException e) {
//			System.out.println("ERROR: Cannot write to "+ fileName + "!");
//		} catch(IOException e) {
//			System.out.println("ERROR: Cannot read from " + fileName + "!");
//		}
//	}
	
	public double getDragConstant() {
		return dragConstant;
	}
	
	public double getMaxVelocity() {
		return maxVelocity;
	}
}
