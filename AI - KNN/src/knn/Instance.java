package knn;

/**
 * Class to represent an Instance of the iris data.
 * 
 * @author Troy
 *
 */
public class Instance {

	// Values to store.
	final double sl;
	final double sw;
	final double pl;
	final double pw;
	final String classLabel;
	// 0 = no cluster.
	private int cluster;

	/**
	 * Constructs a new Instance using the given values.
	 * 
	 * @param classLabel
	 * @param sl
	 * @param sw
	 * @param pl
	 * @param pw
	 */
	public Instance(double sl, double sw, double pl, double pw, String classLabel, int cluster) {

		this.sl = sl;
		this.sw = sw;
		this.pl = pl;
		this.pw = pw;
		this.classLabel = classLabel;
		this.cluster = cluster;
	}

	/**
	 * @return the classLabel
	 */
	public String getClassLabel() {
		return classLabel;
	}

	/**
	 * @return the cluster
	 */
	public int getCluster() {
		return cluster;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}

	/**
	 * Compares the location of two instances, if they are the same returns TRUE.
	 * 
	 * @param other Instance to compare to.
	 * @return true if location is the same.
	 */
	public boolean compareLoc(Instance other){

		if (this.sl == other.sl && this.sw == other.sw && this.pl == other.pl && this.pw == other.pw)
			return true;
		else 
			return false;
	}

}
