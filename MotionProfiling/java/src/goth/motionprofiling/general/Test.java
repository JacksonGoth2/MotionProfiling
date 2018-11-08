package goth.motionprofiling.general;

import java.text.DecimalFormat;

public class Test {

	public static void main(String[] args) {

		double d = 0.123456789;
		DecimalFormat data = new DecimalFormat("#.#####");
		System.out.println(data.format(d));
	}
}
