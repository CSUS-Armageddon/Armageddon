package myGameEngine.util;

import ray.rml.Vector3;

public class MovementUtils {
	
	private MovementUtils() {} // deny construction
	
	/**
	 * Determine if the separation between object 1 and object 2 is permissible,
	 * as specified by the maxSeparation passed into the method.
	 * 
	 * @param obj1 Object 1
	 * @param obj2 Object 2
	 * @param maxSeparation the maximum allowed separation
	 * @return True if the separation is within the limit specified, or false if outside the max limit.
	 */
	public static boolean validateSeparation(Vector3 obj1, Vector3 obj2, float maxSeparation) {
		final float o1X = obj1.x();
		final float o1Y = obj1.y();
		final float o1Z = obj1.z();
		
		final float o2X = obj2.x();
		final float o2Y = obj2.y();
		final float o2Z = obj2.z();
		
		// use distance formula to calculate separation
		final double distance = Math.sqrt(
									  Math.pow((o1X - o2X), 2) 
									+ Math.pow((o1Y - o2Y), 2) 
									+ Math.pow((o1Z - o2Z), 2));
		
		return (distance > maxSeparation) ? false : true;
	}
	
}
