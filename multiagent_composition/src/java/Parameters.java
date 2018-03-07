
public final class Parameters {
	//agent related
	public static final byte NUM_AGENTS = 2;
	
	public static final byte BASS 		= 0;
	public static final byte TENOR 		= 1;
	public static final byte ALTO		= 2;
	public static final byte SOPRANO 	= 3;
	
	//music related
	public static final int BPM = 100;
	
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
	
	public static final float SOPRANO_PROB_WHOLE 	= 0f;
	public static final float SOPRANO_PROB_HALF 	= 0.05f;
	public static final float SOPRANO_PROB_QUARTER 	= 0.05f;
	public static final float SOPRANO_PROB_8th		= 0.60f;
	public static final float SOPRANO_PROB_16th		= 0.30f;
}
