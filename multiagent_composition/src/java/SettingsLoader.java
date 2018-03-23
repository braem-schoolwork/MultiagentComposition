import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Contains the loader function to load settings into the system from file.
 * The settings.txt file has a list of arguments on different lines.
 * The arguments are:
 * 
 * arg0:	Number of agents. Only 1 or 2 is supported.
 * 		Default: 2
 * 
 * arg1: 	Delay of agents, in MS.
 * 		Default: 1000
 * 
 * arg2: 	Percept length, in terms of whole notes.
 * 		Eg. 1 will make the percept length 1 whole note
 * 		Default: 1
 * 
 * arg3: 	Lagging length coefficient: Lagging length = Percept length * lagging length coefficient.
 * 		Lagging length is how far behind an agent needs to be to be considered lagging.
 * 		A larger value means that an agent is more likely going to have to make
 * 		decisions only on their perception, but blocks the other agents less.
 * 		Default: 0.25
 * 
 * arg4: 	BPM
 * 		Default: 100
 * 
 * arg5: 	Bass duration probabilities (comma delimited), from whole note to 16th note.
 * 		Default: 0.1,0.1,0.7,0.1,0
 * 
 * arg6:	Tenor duration probabilities (comma delimited), from whole note to 16th note.
 * 		Default: 0.05,0.1,0.4,0.4,0.05
 * 
 * arg7:	Alto duration probabilities (comma delimited), from whole note to 16th note.
 * 		Default: 0.05,0.1,0.4,0.4,0.05
 * 
 * arg8:	Soprano duration probabilities (comma delimited), from whole note to 16th note.
 * 		Default: 0,0.1,0.2,0.5,0.2
 * 
 * arg9:	Sounding probabilities (probabilities of what type of sounding an agent chooses)
 * 		In the form of perfect consonance probability, imperfect consonance probability,
 * 		mild dissonance probability, sharp dissonance probability.
 * 		Default: 0.04,0.9,0.04,0.02
 * 
 * arg10: 	Whether to accept user input (open the user input window)
 * 		Default: true
 * 
 * arg11: 	Whether to use GUI (true means use GUI)
 * 		Default: true
 * 
 * arg12:	Type of GUI to display (NOTATION, MIDISCORE, or SKETCH)
 * 		Default: MIDISCORE
 * 
 * @author braem
 */
public final class SettingsLoader {
	
	/**
	 * Loads the settings into the system from ./settings.txt.
	 * Uses default settings in the case that settings.txt cannot be found
	 */
	public static void load() {
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get("settings.txt"));
		} catch (IOException e) {
			System.out.println("Could not find settings file, using default settings.");
			return;
		}
		
		byte numAgents, bpm;
		int agentDelay;
		double perceptLength, laggingLength;
		double[] 	bassProbs 	= new double[5],
					tenorProbs 	= new double[5],
					altoProbs 	= new double[5],
					sopProbs 	= new double[5],
					sndingProbs = new double[4];
		boolean acceptUserInput, useGUI;
		ViewType viewType;
		try {
			//load settings
			numAgents 		= Byte.parseByte(lines.get(0));
			agentDelay 		= Integer.parseInt(lines.get(1));
			perceptLength 	= Double.parseDouble(lines.get(2))
					* SystemParams.WHOLE_NOTE_DURATION - 0.001;
			laggingLength 	= Double.parseDouble(lines.get(3))
					* perceptLength;
			bpm 			= Byte.parseByte(lines.get(4));
			
			String[] bassProbsStr 	= lines.get(5).split(",");
			String[] tenorProbsStr 	= lines.get(6).split(",");
			String[] altoProbsStr 	= lines.get(7).split(",");
			String[] sopProbsStr 	= lines.get(8).split(",");
			String[] sndingProbsStr = lines.get(9).split(",");
			for(int i=0; i<5; i++) {
				bassProbs[i] 	= Double.parseDouble(bassProbsStr[i]);
				tenorProbs[i] 	= Double.parseDouble(tenorProbsStr[i]);
				altoProbs[i] 	= Double.parseDouble(altoProbsStr[i]);
				sopProbs[i] 	= Double.parseDouble(sopProbsStr[i]);
				if(i < 4)
					sndingProbs[i]	= Double.parseDouble(sndingProbsStr[i]);
			}
			
			acceptUserInput 	= Boolean.parseBoolean(lines.get(10));
			useGUI 				= Boolean.parseBoolean(lines.get(11));
			viewType 			= ViewType.valueOf(lines.get(12));
		} catch(Exception e) {
			System.out.println("Ill-formed settings file, using default settings.");
			return;
		}
		
		AgentParams.	NUM_AGENTS 		= numAgents;
		SystemParams.	AGENT_DELAY 	= agentDelay;
		SystemParams.	PERCEPT_LENGTH 	= perceptLength;
		SystemParams.	LAGGING_LENGTH 	= laggingLength;
		MusicParams.	BPM 			= bpm;
		
		MusicParams.BASS_PROB_WHOLE 	= bassProbs[0];
		MusicParams.BASS_PROB_HALF 		= bassProbs[1];
		MusicParams.BASS_PROB_QUARTER 	= bassProbs[2];
		MusicParams.BASS_PROB_8TH 		= bassProbs[3];
		MusicParams.BASS_PROB_16TH 		= bassProbs[4];
		
		MusicParams.TENOR_PROB_WHOLE 	= tenorProbs[0];
		MusicParams.TENOR_PROB_HALF 	= tenorProbs[1];
		MusicParams.TENOR_PROB_QUARTER 	= tenorProbs[2];
		MusicParams.TENOR_PROB_8TH 		= tenorProbs[3];
		MusicParams.TENOR_PROB_16TH 	= tenorProbs[4];
		
		MusicParams.ALTO_PROB_WHOLE 	= altoProbs[0];
		MusicParams.ALTO_PROB_HALF 		= altoProbs[1];
		MusicParams.ALTO_PROB_QUARTER 	= altoProbs[2];
		MusicParams.ALTO_PROB_8TH 		= altoProbs[3];
		MusicParams.ALTO_PROB_16TH 		= altoProbs[4];
		
		MusicParams.SOPRANO_PROB_WHOLE 		= sopProbs[0];
		MusicParams.SOPRANO_PROB_HALF 		= sopProbs[1];
		MusicParams.SOPRANO_PROB_QUARTER 	= sopProbs[2];
		MusicParams.SOPRANO_PROB_8TH 		= sopProbs[3];
		MusicParams.SOPRANO_PROB_16TH 		= sopProbs[4];
		
		MusicParams.PC_PROB	= sndingProbs[0];
		MusicParams.IC_PROB	= sndingProbs[1];
		MusicParams.MD_PROB	= sndingProbs[2];
		MusicParams.SD_PROB	= sndingProbs[3];
		
		SystemParams.ACCEPT_USER_INPUT		= acceptUserInput;
		SystemParams.SHOW_COMPOSITION_GUI 	= useGUI;
		SystemParams.VIEW_TYPE				= viewType;
		
		System.out.println("Loaded Settings from settings.txt.");
	}
	
}
