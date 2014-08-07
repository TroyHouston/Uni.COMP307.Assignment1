package knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Refactor - These methods are faaaar too long.
		
		// Check Arguments
		if (args.length != 2) {
			System.out.println("Requires two file names(in data folder): Training set & Test Set");
			return;
		}

		System.out.println("Training Set: " + args[0] + "\nTest Set: " + args[1]);

		// Read Training data set.

		System.out.print("Loading training set... ");

		ArrayList<Instance> trainingInstances = new ArrayList<Instance>();
		File trainingSet = new File("data" + File.separator + args[0]);
		BufferedReader reader = null;

		// Variable to calculate ranges.
		double max0 = Integer.MIN_VALUE;
		double min0 = Integer.MAX_VALUE;
		double max1 = Integer.MIN_VALUE;
		double min1 = Integer.MAX_VALUE;
		double max2 = Integer.MIN_VALUE;
		double min2 = Integer.MAX_VALUE;
		double max3 = Integer.MIN_VALUE;
		double min3 = Integer.MAX_VALUE;

		try {
			reader = new BufferedReader(new FileReader(trainingSet));
			String text = null;

			while ((text = reader.readLine()) != null && !text.equalsIgnoreCase("")) {
				String values[] = text.split("\\s+");
				trainingInstances.add(new Instance(Float.parseFloat(values[0]), Float.parseFloat(values[1]),
						Float.parseFloat(values[2]), Float.parseFloat(values[3]), values[4], 0));

				// Keep track of current minimum & maximum values.
				// Seems like an ugly way to do this...
				if (Float.parseFloat(values[0]) > max0)
					max0 = Float.parseFloat(values[0]);
				if (Float.parseFloat(values[1]) > max1)
					max1 = Float.parseFloat(values[1]);
				if (Float.parseFloat(values[2]) > max2)
					max2 = Float.parseFloat(values[2]);
				if (Float.parseFloat(values[3]) > max3)
					max3 = Float.parseFloat(values[3]);

				if (Float.parseFloat(values[0]) < min0)
					min0 = Float.parseFloat(values[0]);
				if (Float.parseFloat(values[1]) < min1)
					min1 = Float.parseFloat(values[1]);
				if (Float.parseFloat(values[2]) < min2)
					min2 = Float.parseFloat(values[2]);
				if (Float.parseFloat(values[3]) < min3)
					min3 = Float.parseFloat(values[3]);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Loaded.");

		// List to store minimum & maximum values.
		// Not calculated as ranges yet
		ArrayList<Double> minMaxValues = new ArrayList<Double>();
		minMaxValues.add(min0);
		minMaxValues.add(max0);
		minMaxValues.add(min1);
		minMaxValues.add(max1);
		minMaxValues.add(min2);
		minMaxValues.add(max2);
		minMaxValues.add(min3);
		minMaxValues.add(max3);

		// Read Test data set, don't read in the answers to each instance
		// Create a separate list for answers.

		System.out.print("Loading test set... ");

		ArrayList<Instance> testInstances = new ArrayList<Instance>();
		ArrayList<String> answers = new ArrayList<String>();
		File testSet = new File("data" + File.separator + args[1]);

		try {
			reader = new BufferedReader(new FileReader(testSet));
			String text = null;

			while ((text = reader.readLine()) != null && !text.equalsIgnoreCase("")) {
				String values[] = text.split("\\s+");
				answers.add(values[4]);
				testInstances.add(new Instance(Float.parseFloat(values[0]), Float.parseFloat(values[1]),
						Float.parseFloat(values[2]), Float.parseFloat(values[3]), (String)null, 0));
			}

			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Loaded.");

		getUserInput(trainingInstances, minMaxValues, testInstances, answers);

	}

	private static void getUserInput(ArrayList<Instance> trainingInstances,
			ArrayList<Double> minMaxValues, ArrayList<Instance> testInstances,
			ArrayList<String> answers) {
		
		// Ask user for classification or clustering. 
		System.out.println("\nWhat method(Enter the number 1-2)?\n1. K-Nearest Neighbour\n2. K-Means Clustering");

		int t = 0;

		try {
			t = System.in.read() - 48;
			while (t < 1 || t > 2) {
				t = System.in.read() - 48;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (t ==1)
			System.out.println("K-Nearest Neighbour method chosen.");
		else if (t == 2)
			System.out.println("K-Means Clustering method chosen.");

		// Ask user for a k value.
		// Will take first character entered only.
		System.out.println("What value of k? (1-9)");

		int k = 0;
		try {
			k = System.in.read() - 48;
			while (k < 1 || k > 9) { k = System.in.read() - 48; }
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (t != 2)
			System.out.println("k value: " + k + " accepted.");
		else 
			System.out.println("k value: 3 accepted.");

		initiate(trainingInstances, minMaxValues, testInstances, answers, t, k);
	}

	private static void initiate(ArrayList<Instance> trainingInstances,
			ArrayList<Double> minMaxValues, ArrayList<Instance> testInstances,
			ArrayList<String> answers, int t, int k) {
		
		// K-Nearest Neighbour method implementation.
		if (t == 1) {

			KNNClassifier classifier = new KNNClassifier(trainingInstances);
			ArrayList<String> classifiedTestSet = classifier.classifyTestSet(testInstances, k, minMaxValues);

			int correctCount = 0;
			int total = testInstances.size();

			for (int i = 0 ; i < answers.size(); i++) {
				if (answers.get(i).equalsIgnoreCase(classifiedTestSet.get(i)))
					correctCount++;
			}

			// Calculate accuracy.
			double accuracy = (double)correctCount/total * 100;
			System.out.println("Accuracy attained was " + accuracy + "%.");

		}

		// K-Means Clustering method implementation.
		else if (t == 2) {
			// Pass test set
			// Activate clustering with k value and list of minMaxValues
			KMeansCluster clusterer = new KMeansCluster(testInstances);
			clusterer.cluster(k, minMaxValues);
		}
	}

}
