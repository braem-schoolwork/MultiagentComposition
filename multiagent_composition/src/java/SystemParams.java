
/**
 * Holds the system parameters.
 * @author braem
 */
public final class SystemParams {
	/**
	 * The amount of time the agent delays between actions, in milliseconds.
	 */
	public static final int AGENT_DELAY = 1000;
	
	/**
	 * Duration of whole note as dictated by jMusic
	 */
	public static final double WHOLE_NOTE_DURATION = 3.6;
	/**
	 * Length of the perception of the agent.
	 * Measured in jMusic note durations
	 */
	public static final double PERCEPT_LENGTH = WHOLE_NOTE_DURATION - 0.001;
	
	/**
	 * Whether or not to show the Composition in a GUI.
	 * NOTE: GUI may not refresh in time if agent delay is not long enough.
	 */
	public static final boolean SHOW_COMPOSITION_GUI = true;
	/**
	 * Whether or not to select user input.
	 */
	public static final boolean ACCEPT_USER_INPUT = true;
}
