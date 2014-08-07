package knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Class to implement the K Means Clustering algorithm on a data set.
 * 
 * @author Troy
 *
 */
public class KMeansCluster {

	ArrayList<Instance> dataSet;
	ArrayList<Double> minMaxValues;

	private double sqRange0;
	private double sqRange1;
	private double sqRange2;
	private double sqRange3;

	public KMeansCluster(ArrayList<Instance> dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * Cluster the data set of this KMeansCluster into the given k number of clusters.
	 * Will display the results of the clustering internally.
	 * Currently only returns null. Possibility of further expansion for accuracy counts. 
	 * 
	 * @param k
	 * @param minMaxValues
	 * @return
	 */
	public ArrayList<String> cluster(int k, ArrayList<Double> minMaxValues) {

		// Use the given minimum & maximum values to calculate the ranges.
		calculateRanges(minMaxValues);
		this.minMaxValues = minMaxValues;

		HashMap<Integer, ArrayList<Instance>> groupToInstance = new HashMap<Integer, ArrayList<Instance>>();

		// Generate k random points(initial means)
		// This would be easier to pull 3 random data values.

		Instance mean1 = generateRandomInstance(minMaxValues);
		Instance mean2 = generateRandomInstance(minMaxValues);
		Instance mean3 = generateRandomInstance(minMaxValues);

		// Cap at 100 changes to the mean.
		for (int count = 0; count < 100; count++) {
			groupToInstance = new HashMap<Integer, ArrayList<Instance>>();

			for (int i = 0; i < dataSet.size(); i++) {
				// Calculate the distance to each mean point.
				Instance currentInstance = dataSet.get(i);
				double distance1 = calculateDistance(currentInstance, mean1);
				double distance2 = calculateDistance(currentInstance, mean2);
				double distance3 = calculateDistance(currentInstance, mean3);
				
				// Calculate the closest mean.
				int closestMean = 0;
				if (distance1 < distance2 && distance1 < distance3)
					closestMean = 1;
				else if (distance2 < distance1 && distance2 < distance3)
					closestMean = 2;
				else if (distance3 < distance1 && distance3 < distance2)
					closestMean = 3;

				// Save the closest mean of this instance.
				currentInstance.setCluster(closestMean);

				// Store instance in a Map of ClusterGroup(1..k) --> Instance
				if (groupToInstance.get(closestMean) == null)
					groupToInstance.put(closestMean, new ArrayList<Instance>());

				groupToInstance.get(closestMean).add(currentInstance);
			}

			// Average the points of each cluster to create a new mean.
			Instance newMean1 = averageOf(groupToInstance.get(1));
			Instance newMean2 = averageOf(groupToInstance.get(2));
			Instance newMean3 = averageOf(groupToInstance.get(3));

			// Check if convergence of clusters has been found.
			if (mean1.compareLoc(newMean1) && mean2.compareLoc(newMean2) && mean3.compareLoc(newMean3)) {
				System.out.println("Convergence found with " + count + " of tries. (Is that the right word?)");
				break;
			}
			// Start new loop with the new mean points
			else {
				mean1 = newMean1;
				mean2 = newMean2;
				mean3 = newMean3;
			}
			// Repeat until nothing changes. (Check  if all averages are the same?)
		}

		// Output cluster classification of each instance.
		for (int i = 0; i < dataSet.size(); i++) {
			System.out.println("Instance " + i + " was put into cluster " + dataSet.get(i).getCluster());
		}

		// Count cluster instances.
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		if (groupToInstance.get(0) != null) 	// This should be null.
			count0 = groupToInstance.get(0).size();

		if (groupToInstance.get(1) != null) 
			count1 = groupToInstance.get(1).size();
		if (groupToInstance.get(2) != null) 
			count2 = groupToInstance.get(2).size();
		if (groupToInstance.get(3) != null) 
			count3 = groupToInstance.get(3).size();

		// Print cluster group values.
		System.out.println("\nUnclustered data points(Should be 0): " + count0);
		System.out.println("Cluster 1 data points: " + count1);
		System.out.println("Cluster 2 data points: " + count2);
		System.out.println("Cluster 3 data points: " + count3);

		// Could return the answers.

		return null;
	}

	/**
	 * Calculate the average point of a cluster of data points.
	 * If the cluster is empty, it generates a new random point for the mean of that cluster.
	 * 
	 * @param cluster The list of instances within the cluster
	 * @return The new mean of the given cluster.
	 */
	private Instance averageOf(ArrayList<Instance> cluster) {
		double slA = 0;
		double swA = 0;
		double plA = 0;
		double pwA = 0;
		double count = 0;

		if (cluster == null) {
			// Why would you be trying to average null?
			// Unlucky random start point. Redo this point.
			// Pulling data values as start points would be better.
			return generateRandomInstance(minMaxValues);
		}
		
		// Calculate the average.
		for (Instance i : cluster) {
			slA += i.sl;
			swA += i.sw;
			plA += i.pl;
			pwA += i.pw;
			count++;
		}
		slA = slA / count;
		swA = swA / count;
		plA = plA / count;
		pwA = pwA / count;

		return new Instance(slA, swA, plA, pwA, null, 0);
	}

	/**
	 * Generate a new random instance within the range of the training data points.
	 * 
	 * @param minMaxVal The list of minimum & maximum points from the training set.
	 * @return	The new instance with random data values.
	 */
	private Instance generateRandomInstance(ArrayList<Double> minMaxVal) {
		Random rand = new Random();
		// Generate new random data values.
		double sl = rand.nextDouble() * (minMaxVal.get(1) - minMaxVal.get(0)) + minMaxVal.get(0);
		double sw = rand.nextDouble() * (minMaxVal.get(3) - minMaxVal.get(2)) + minMaxVal.get(2);
		double pl = rand.nextDouble() * (minMaxVal.get(5) - minMaxVal.get(4)) + minMaxVal.get(4);
		double pw = rand.nextDouble() * (minMaxVal.get(7) - minMaxVal.get(6)) + minMaxVal.get(6);

		return new Instance(sl,sw,pl,pw, null, 0);
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
	 * Calculate the data point ranges from the given list.
	 * Stored as class variables. 
	 * 
	 * @param minMaxValues The list of minimum & maximum points from the training set. 
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

}
