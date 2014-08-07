package knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to implement the K-Nearest Neighbour Classifier algorithm on a data set.
 * 
 * @author Troy
 *
 */
public class KNNClassifier {

	List<Instance> trainingSet;
	final String class1 = "Iris-setosa";
	final String class2 = "Iris-versicolor";
	final String class3 = "Iris-virginica";

	private double sqRange0;
	private double sqRange1;
	private double sqRange2;
	private double sqRange3;

	/**
	 * Creates a new classifier for a K-Nearest Neighbour classification.
	 * Stores the given training set.
	 *
	 * @param trainingSet
	 */
	public KNNClassifier (ArrayList<Instance> trainingSet) {
		this.trainingSet = trainingSet;
	}

	/**
	 * Method used to classify the given test set based on the training set of this class.
	 * Will use the given k value for the number of neighbours.
	 * 
	 * @param testSet 	The data set of which to classify
	 * @param k 		The number of neighbours to compare to
	 * @param minMaxValues	The list of minimum and maximum points from the training data.
	 * @return	The list of answers.
	 */
	public ArrayList<String> classifyTestSet(ArrayList<Instance> testSet, int k, ArrayList<Double> minMaxValues) {

		// Calculate ranges based on the given list.
		calculateRanges(minMaxValues);
		// List to store the answers in.
		ArrayList<String> answers = new ArrayList<String>();

		// Classify each point in the test data.
		for (int i = 0; i < testSet.size(); i++) {
			Instance classificationInstance = testSet.get(i);
			ArrayList<DistanceLabel> distanceLabels = new ArrayList<DistanceLabel>();

			// Calculate the distance to every neighbour from a points
			for (int n = 0; n < trainingSet.size(); n++) {
				double distance = calculateDistance(classificationInstance, trainingSet.get(n));
				String Label = trainingSet.get(n).getClassLabel();
				distanceLabels.add(new DistanceLabel(distance, Label));
			}

			// Sort and choose the closest k neighbours
			Collections.sort(distanceLabels, new distanceComparator());
			String classifiedLabel = countKClass(distanceLabels, k);
			// Add to the list of answers.
			answers.add(classifiedLabel);
			System.out.println(i + " guessed as " + classifiedLabel);
		}
		return answers;
	}
	
	/**
	 * Calculate the class to classify instance as given the distances and k value
	 * 
	 * @param distanceLabels List of neighbour relations
	 * @param k		The number of neighbours to compare to
	 * @return		The classified class
	 */
	private String countKClass(ArrayList<DistanceLabel> distanceLabels, int k) {
		
		String classifiedLabel ="Not classified.";
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		// Total the neighbour counts
		for (int ki = 0; ki < k; ki++) {
			if (distanceLabels.get(ki).classLabel.equalsIgnoreCase(class1))
				count1++;
			else if (distanceLabels.get(ki).classLabel.equalsIgnoreCase(class2))
				count2++;
			else if (distanceLabels.get(ki).classLabel.equalsIgnoreCase(class3))
				count3++;
		}
		
		// Cleaner way to compare. 
		// Distance label class works here too, should really rename it generically.
		ArrayList<DistanceLabel> counts = new ArrayList<DistanceLabel>();
		counts.add(new DistanceLabel(count1, class1));
		counts.add(new DistanceLabel(count2, class2));
		counts.add(new DistanceLabel(count3, class3));
		
		// Re-order the count totals.
		Collections.sort(counts, new kValueComparator());
		// Check if there was a draw.
		if (counts.get(0).distance == counts.get(1).distance)
			// Resolve the draw using one more k value.
			classifiedLabel = countKClass(distanceLabels, k+1);
		else 
			classifiedLabel = counts.get(0).classLabel;
		
		return classifiedLabel;
		
	}

	/**
	 * Calculate the Euclidean distance between two feature vectors.
	 *
	 * @param a	The unseen data.
	 * @param b	The training set data.
	 * @return	The distance between the two vectors, no units.
	 */
	private double calculateDistance(Instance a, Instance b) {

		double distance;

		distance = Math.pow((a.pl - b.pl),2)/sqRange0 + Math.pow((a.pw - b.pw),2)/sqRange1
				+ Math.pow((a.sl - b.sl),2)/sqRange2  + Math.pow((a.sw - b.sw),2)/sqRange3;

		distance = Math.sqrt(distance);

		return distance;
	}
	
	/**
	 * Calculate the class variable ranges based of the given minimum & maximum values 
	 * 
	 * @param minMaxValues		List of minimum & maximum values
	 */
	private void calculateRanges(ArrayList<Double> minMaxValues) {
		sqRange0 = minMaxValues.get(1) - minMaxValues.get(0); 
		sqRange1 = minMaxValues.get(3) - minMaxValues.get(2);
		sqRange2 = minMaxValues.get(5) - minMaxValues.get(4);
		sqRange3 = minMaxValues.get(7) - minMaxValues.get(6);
		
		sqRange0 = Math.pow(sqRange0,2);
		sqRange1 = Math.pow(sqRange1,2);
		sqRange2 = Math.pow(sqRange2,2);
		sqRange3 = Math.pow(sqRange3,2);

	}

	/**
	 * Comparator for the neighbour distance calculations 
	 * 
	 * @author Troy
	 *
	 */
	class distanceComparator implements Comparator<DistanceLabel> {
		@Override
		public int compare(DistanceLabel a, DistanceLabel b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
		}
	}
	
	/**
	 * Comparator for the class counts
	 * 
	 * @author Troy
	 *
	 */
	class kValueComparator implements Comparator<DistanceLabel> {
		@Override
		public int compare(DistanceLabel a, DistanceLabel b) {
			return a.distance < b.distance ? 1 : a.distance == b.distance ? 0 : -1;
		}
	}

}
