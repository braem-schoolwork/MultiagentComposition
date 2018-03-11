
/**
 * Class holding the selection algorithm used in model.java
 * @author braem
 */
public final class ProbabilisticSelectionAlgorithm {

	/**
	 * Runs a selection algorithm based on random numbers and
	 * cumulative probabilities.
	 * @param probs		Array of probabilities.
	 * 
	 * @return	A number from 0 to probs.size indicating the selection region.
	 */
	public static int run(double[] probs) {
		//calculate cumulative probabilities
		for(int i=0; i<probs.length-1; i++) {
			probs[i+1] += probs[i];
		}
		double randomNum = Math.random();
		if(randomNum < probs[0]) return 0;
		for(int i=0; i<probs.length-1; i++) {
			if(randomNum >= probs[i] && randomNum < probs[i+1])
				return i+1;
		}
		return -1;
	}
	
}
