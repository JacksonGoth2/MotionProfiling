package goth.motionprofiling.part;

public class Wheel {

	private double radius;
	private double circumference;
	
	public Wheel(double r) {
		radius = r;
		circumference = (r * 2) * Math.PI;
	}
	
	public double getDistanceTraveled(double rotations) {
		return rotations * circumference;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getCircumference() {
		return circumference;
	}
}
