
public final class Parameters {
	//agent related
	public static final byte NUM_AGENTS = 2;
	
	public static final byte BASS 		= 0;
	public static final byte TENOR 		= 1;
	public static final byte ALTO		= 2;
	public static final byte SOPRANO 	= 3;
	
	//System
	public static final int SLEEP_AMOUNT = 1000;
	public static final int CATCHUP_AMOUNT = 500;
	public static final boolean GUI = true;
	public static final boolean USER_INPUT = true;
	
	//music related
	public static final int BPM = 100;
	
	public static final int UNISON = 0;
	public static final int m2nd = 1;
	public static final int M2nd = 2;
	public static final int m3rd = 3;
	public static final int M3rd = 4;
	public static final int P4th = 5;
	public static final int TRITONE = 6;
	public static final int P5th = 7;
	public static final int m6th = 8;
	public static final int M6th = 9;
	public static final int m7th = 10;
	public static final int M7th = 11;
	public static final int OCTAVE = 12;
	
	public static final float PC_PROB = 0.1f;
	public static final float IC_PROB = 0.75f;
	public static final float MD_PROB = 0.1f;
	public static final float SD_PROB = 0.05f;
	
	//part ranges
	public static final int BASS_LOWER = 38;
	public static final int BASS_UPPER = 60;
	
	public static final int TENOR_LOWER = 48;
	public static final int TENOR_UPPER = 67;
	
	public static final int ALTO_LOWER = 55;
	public static final int ALTO_UPPER = 74;
	
	public static final int SOPRANO_LOWER = 60;
	public static final int SOPRANO_UPPER = 79;
	
	//probabilities
	public static final float BASS_PROB_WHOLE 	= 0.10f;
	public static final float BASS_PROB_HALF	= 0.10f;
	public static final float BASS_PROB_QUARTER = 0.70f;
	public static final float BASS_PROB_8th 	= 0.10f;
	public static final float BASS_PROB_16th	= 0f;
	
	public static final float TENOR_PROB_WHOLE 		= 0.05f;
	public static final float TENOR_PROB_HALF		= 0.10f;
	public static final float TENOR_PROB_QUARTER 	= 0.40f;
	public static final float TENOR_PROB_8th 		= 0.40f;
	public static final float TENOR_PROB_16th 		= 0.05f;
	
	public static final float ALTO_PROB_WHOLE 	= 0.05f;
	public static final float ALTO_PROB_HALF	= 0.10f;
	public static final float ALTO_PROB_QUARTER = 0.40f;
	public static final float ALTO_PROB_8th 	= 0.40f;
	public static final float ALTO_PROB_16th	= 0.05f;
	
	public static final float SOPRANO_PROB_WHOLE 	= 0.1f;
	public static final float SOPRANO_PROB_HALF 	= 0.1f;
	public static final float SOPRANO_PROB_QUARTER 	= 0.7f;
	public static final float SOPRANO_PROB_8th		= 0.1f;
	public static final float SOPRANO_PROB_16th		= 0f;
}
