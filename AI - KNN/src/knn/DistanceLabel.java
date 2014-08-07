package knn;

/**
 * Class used to relate a double to a string.
 * 
 * @author Troy
 *
 */
public class DistanceLabel {
	
	final double distance;
	final String classLabel;
	
	public DistanceLabel(double distance, String classLabel) {
		this.distance = distance;
		this.classLabel = classLabel;
	}

}
