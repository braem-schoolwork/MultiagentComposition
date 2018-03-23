
/**
 * Holds the musical parameters of the system.
 * @author braem
 */
public final class MusicParams {
	/**
	 * Beats per minute of the composition
	 */
	public static byte BPM = 100;
	
	/**
	 * Interval ID for a unison interval.
	 */
	public static final byte UNISON = 0;
	/**
	 * Interval ID for a minor 2nd interval.
	 */
	public static final byte m2nd = 1;
	/**
	 * Interval ID for a major 2nd interval.
	 */
	public static final byte M2nd = 2;
	/**
	 * Interval ID for a minor 3rd interval.
	 */
	public static final byte m3rd = 3;
	/**
	 * Interval ID for a major 3rd interval.
	 */
	public static final byte M3rd = 4;
	/**
	 * Interval ID for a perfect 4th interval.
	 */
	public static final byte P4th = 5;
	/**
	 * Interval ID for a tritone interval.
	 * AKA: Augmented 4th or Diminished 5th interval.
	 */
	public static final byte TRITONE = 6;
	/**
	 * Interval ID for a perfect 5th interval.
	 */
	public static final byte P5th = 7;
	/**
	 * Interval ID for a minor 6th interval.
	 */
	public static final byte m6th = 8;
	/**
	 * Interval ID for a major 6th interval.
	 */
	public static final byte M6th = 9;
	/**
	 * Interval ID for a minor 7th interval.
	 */
	public static final byte m7th = 10;
	/**
	 * Interval ID for a major 7th interval.
	 */
	public static final byte M7th = 11;
	/**
	 * Interval ID for an octave interval.
	 */
	public static final byte OCTAVE = 12;
	
	/**
	 * Probability of selecting a note that gives a perfect consonance.
	 */
	public static double PC_PROB = 0.04;
	/**
	 * Probability of selecting a note that gives an imperfect consonance.
	 */
	public static double IC_PROB = 0.9;
	/**
	 * Probability of selecting a note that gives a mild dissonance.
	 */
	public static double MD_PROB = 0.04;
	/**
	 * Probability of selecting a note that gives a sharp dissonance.
	 */
	public static double SD_PROB = 0.02;
	
	/**
	 * Number of Different Durations.
	 * Only whole, half, quarter, eighth, and sixteenth.
	 */
	public static final byte NUM_DURATIONS = 5;
	
	//part ranges
	/**
	 * Lowest note possible in the bass part
	 */
	public static final int BASS_LOWER = 38;
	/**
	 * Highest note possible in the bass part
	 */
	public static final int BASS_UPPER = 60;

	/**
	 * Lowest note possible in the tenor part
	 */
	public static final int TENOR_LOWER = 48;
	/**
	 * Highest note possible in the tenor part
	 */
	public static final int TENOR_UPPER = 67;

	/**
	 * Lowest note possible in the alto part
	 */
	public static final int ALTO_LOWER = 55;
	/**
	 * Highest note possible in the alto part
	 */
	public static final int ALTO_UPPER = 74;

	/**
	 * Lowest note possible in the soprano part
	 */
	public static final int SOPRANO_LOWER = 60;
	/**
	 * Highest note possible in the soprano part
	 */
	public static final int SOPRANO_UPPER = 79;
	
	//probabilities
	/**
	 * Probability of the bass part selecting a whole note.
	 */
	public static double BASS_PROB_WHOLE 		= 0.1;
	/**
	 * Probability of the bass part selecting a half note.
	 */
	public static double BASS_PROB_HALF		= 0.1;
	/**
	 * Probability of the bass part selecting a quarter note.
	 */
	public static double BASS_PROB_QUARTER 	= 0.7;
	/**
	 * Probability of the bass part selecting an 8th note.
	 */
	public static double BASS_PROB_8TH 		= 0.1;
	/**
	 * Probability of the bass part selecting a 16th note.
	 */
	public static double BASS_PROB_16TH		= 0;

	/**
	 * Probability of the tenor part selecting a whole note.
	 */
	public static double TENOR_PROB_WHOLE 	= 0.05;
	/**
	 * Probability of the tenor part selecting a half note.
	 */
	public static double TENOR_PROB_HALF		= 0.1;
	/**
	 * Probability of the tenor part selecting a quarter note.
	 */
	public static double TENOR_PROB_QUARTER 	= 0.4;
	/**
	 * Probability of the tenor part selecting an 8th note.
	 */
	public static double TENOR_PROB_8TH 		= 0.4;
	/**
	 * Probability of the tenor part selecting a 16th note.
	 */
	public static double TENOR_PROB_16TH 		= 0.05;

	/**
	 * Probability of the alto part selecting a whole note.
	 */
	public static double ALTO_PROB_WHOLE 		= 0.05;
	/**
	 * Probability of the alto part selecting a half note.
	 */
	public static double ALTO_PROB_HALF		= 0.1;
	/**
	 * Probability of the alto part selecting a quarter note.
	 */
	public static double ALTO_PROB_QUARTER 	= 0.4;
	/**
	 * Probability of the alto part selecting an 8th note.
	 */
	public static double ALTO_PROB_8TH 		= 0.4;
	/**
	 * Probability of the alto part selecting a 16th note.
	 */
	public static double ALTO_PROB_16TH		= 0.05;

	/**
	 * Probability of the soprano part selecting a whole note.
	 */
	public static double SOPRANO_PROB_WHOLE 	= 0;
	/**
	 * Probability of the soprano part selecting a half note.
	 */
	public static double SOPRANO_PROB_HALF 	= 0.1;
	/**
	 * Probability of the soprano part selecting a quarter note.
	 */
	public static double SOPRANO_PROB_QUARTER = 0.2;
	/**
	 * Probability of the soprano part selecting an 8th note.
	 */
	public static double SOPRANO_PROB_8TH		= 0.5;
	/**
	 * Probability of the soprano part selecting a 16th note.
	 */
	public static double SOPRANO_PROB_16TH	= 0.2;
	
	/**
	 * Verifies that the parameters set here are valid.
	 * @return	<code>true</code> if parameters are valid.
	 * 			<code>false</code> otherwise.
	 */
	public static boolean isValidParameters() {
		boolean soundingProbs = checkProbability(new double[] 
				{PC_PROB, IC_PROB, MD_PROB, SD_PROB});
		boolean bassProbs = checkProbability(new double[] 
				{BASS_PROB_WHOLE, BASS_PROB_HALF, BASS_PROB_QUARTER,
						BASS_PROB_8TH, BASS_PROB_16TH});
		boolean tenorProbs = checkProbability(new double[] 
				{TENOR_PROB_WHOLE, TENOR_PROB_HALF, TENOR_PROB_QUARTER,
						TENOR_PROB_8TH, TENOR_PROB_16TH});
		boolean altoProbs = checkProbability(new double[] 
				{ALTO_PROB_WHOLE, ALTO_PROB_HALF, ALTO_PROB_QUARTER,
						ALTO_PROB_8TH, ALTO_PROB_16TH});
		boolean sopranoProbs = checkProbability(new double[] 
				{SOPRANO_PROB_WHOLE, SOPRANO_PROB_HALF, SOPRANO_PROB_QUARTER,
						SOPRANO_PROB_8TH, SOPRANO_PROB_16TH});
		return soundingProbs && bassProbs && tenorProbs 
				&& altoProbs && sopranoProbs;
	}
	
	/**
	 * Checks whether all numbers in prob sum to 1.
	 * Uses an error of 0.000001 when equating the sum to 1.
	 * @param probs	Array of probabilities.
	 * @return		<code>true</code> if probabilities sum to 1.
	 * 				<code>false</code> otherwise.
	 */
	private static boolean checkProbability(double[] probs) {
		double error = 0.000001d;
		double sum = 0d;
		for(double p : probs)	sum += p;
		if(sum >= 1-error && sum <= 1+error) return true;
		else return false;
	}
}
