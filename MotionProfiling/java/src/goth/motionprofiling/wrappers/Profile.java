package goth.motionprofiling.wrappers;

import java.util.ArrayList;

import goth.motionprofiling.general.Point;

public interface Profile {

	public void calculate();
	public void writeVelocityLogFile(String fileName, ArrayList<Point> profile);
	
	public double getVel(double seconds);
	public double getPos(double seconds);
	public double getError();
}
