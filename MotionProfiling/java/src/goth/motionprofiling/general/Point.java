package goth.motionprofiling.general;
public class Point {

	private double position;
	private double velocity;
	
	public Point(double p, double v) {
		position = p;
		velocity = v;
	}
	
	public String toString() {
		return velocity + ", ";
	}
	
	public double getPos() {
		return position;
	}
	
	public double getVel() {
		return velocity;
	}
}
