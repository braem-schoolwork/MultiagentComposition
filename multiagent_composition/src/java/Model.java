import jm.JMC;
import jm.music.data.*;
import jm.util.*;
import jm.gui.cpn.Notate;
import jm.gui.show.*;
import jm.gui.sketch.SketchScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Model of the System.
 * Contains the action of placing a note in the composition (environment),
 * the functions that perceive the environment, and the composition GUI window.
 * @author braem
 */
public final class Model implements JMC {
	/**
	 * Current position of the bass agent.
	 */
	static double bassPosition = 0d;
	/**
	 * Current index of the bass phrase.
	 */
	static int bassIndex = 0;

	/**
	 * Current position of the tenor agent.
	 */
	static double tenorPosition = 0d;
	/**
	 * Current index of the tenor phrase.
	 */
	static int tenorIndex = 0;
    
	/**
	 * Current position of the alto agent.
	 */
	static double altoPosition = 0d;
	/**
	 * Current index of the alto phrase.
	 */
	static int altoIndex = 0;

	/**
	 * Current position of the soprano agent.
	 */
	static double sopranoPosition = 0d;
	/**
	 * Current index of the soprano phrase.
	 */
	static int sopranoIndex = 0;
    
	/**
	 * Past positions of the bass agent. Unseeable by the agent
	 */
	static private List<Double> bassPositions = new ArrayList<Double>();
	/**
	 * Past positions of the tenor agent. Unseeable by the agent
	 */
	static private List<Double> tenorPositions = new ArrayList<Double>();
	/**
	 * Past positions of the alto agent. Unseeable by the agent
	 */
	static private List<Double> altoPositions = new ArrayList<Double>();
	/**
	 * Past positions of the soprano agent. Unseeable by the agent
	 */
	static private List<Double> sopranoPositions = new ArrayList<Double>();

	/**
	 * Current jMusic composition.
	 */
    private static Score composition = new Score("MAC", MusicParams.BPM);
    /**
     * Parts in the jMusic composition.
     */
    private static Part[] parts = new Part[4];
    /**
     * Phrases in the jMusic parts. Each part contains one phrase in this system.
     * Phrases hold the notes. 
     */
    private static Phrase[] phrases = new Phrase[4];
    
    /**
     * JFrame object for the notation window.
     */
    private static Notate notateWindow;
    /**
     * JFrame object for the midi score window.
     */
    private static ShowScore showWindow;
    /**
     * JFrame object for the sketch window.
     */
    private static SketchScore sketchWindow;
    
    /**
     * Method to initialize the class.
     * Creates the phrases, parts, & score with some initial notes.
     * Opens the Composition view.
     */
    public static void init() {
    	//create parts
		Note n1 = new Note(C3, WHOLE_NOTE);
		Note n2 = new Note(C4, WHOLE_NOTE);
		Note n3 = new Note(C5, WHOLE_NOTE);
		parts[0] = new Part("Bass", PIANO, 0);
		phrases[0] = new Phrase("BassPhrase", 0.0);
		phrases[0].addNote(n1);
		bassPosition+=n1.getDuration();
		bassPositions.add(n1.getDuration());
		parts[1] = new Part("Viola", PIANO, 0);
		phrases[1] = new Phrase("TenorPhrase", 0.0);
		phrases[1].addNote(n2);
		tenorPosition+=n2.getDuration();
		tenorPositions.add(n2.getDuration());
		parts[2] = new Part("Violin 2", PIANO, 0);
		phrases[2] = new Phrase("AltoPhrase", 0.0);
		phrases[2].addNote(n2);
		altoPosition+=n2.getDuration();
		altoPositions.add(n2.getDuration());
		parts[3] = new Part("Violin 1", PIANO, 0);
		phrases[3] = new Phrase("SopranoPhrase", 0.0);
		phrases[3].addNote(n3);
		sopranoPosition+=n3.getDuration();
		sopranoPositions.add(n3.getDuration());
		if(SystemParams.SHOW_COMPOSITION_GUI) openView();
    }
	
	/**
	 * Main action of the agents; places a note in the composition.
	 * Also used by user to add a note.
	 * @param agentID		ID of the agent calling this action.
	 * @param vertNotes		Notes vertical to the agent.
	 * @param vertPositions	Positions of notes vertical to the agent.
	 * @param position		Position of the agent.
	 * @param isUser		Whether or not the user is adding this note
	 * @param userNote		The note the user wants to add. Unused if isUser is <code>false</code>.
	 * 
	 * @return	<code>true</code> if the agent successfully places the note.
	 * 			<code>false</code> otherwise.
	 */
	synchronized static boolean placeNote(int agentID, List<Integer> vertNotes, List<Double> vertPositions,
			double position, boolean isUser, Note userNote) {
		if(isUser) {
			phrases[agentID].addNote(userNote);
			switch(agentID) {
			case AgentParams.BASS: {
				bassPosition+=userNote.getDuration();
				bassPositions.add(bassPosition);
				bassIndex++;
			} break;
			case AgentParams.SOPRANO: {
				sopranoPosition+=userNote.getDuration();
				sopranoPositions.add(sopranoPosition);
				sopranoIndex++;
			} break;
			}
			System.out.println("User added note");
			if(SystemParams.SHOW_COMPOSITION_GUI) refreshView();
			return true;
		}
		int vertNote = getNoteAtCurrentTime(vertNotes, vertPositions, position);
		
		if(isAhead(vertPositions,position))  {
			System.out.println(agentID + " ahead.. " + position + ", " + vertPositions);
			if(SystemParams.SHOW_COMPOSITION_GUI) refreshView();
			return false;
		}
		double duration = chooseDuration(agentID);
		int nextNote;
		Phrase phr = phrases[agentID];
		switch(agentID) {
		case AgentParams.BASS: {
			bassPosition+=duration; 
			bassPositions.add(bassPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(bassIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			if(nextNote == -1) {
				bassPosition-=duration;
				bassPositions.remove(bassPositions.size()-1);
				return false;
			}
			bassIndex++;
		} break;
		case AgentParams.TENOR: {
			tenorPosition+=duration; 
			tenorPositions.add(tenorPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(tenorIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			tenorIndex++;
		} break;
		case AgentParams.ALTO: {
			altoPosition+=duration; 
			altoPositions.add(altoPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(altoIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			altoIndex++;
		} break;
		case AgentParams.SOPRANO: {
			sopranoPosition+=duration; 
			sopranoPositions.add(sopranoPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(sopranoIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			if(nextNote == -1) {
				sopranoPosition-=duration;
				sopranoPositions.remove(sopranoPositions.size()-1);
				return false;
			}
			sopranoIndex++;
		} break;
		default: nextNote = -1;
		}
		if(nextNote > 0) {
			Note n = new Note(nextNote, duration);
			phrases[agentID].addNote(n);
			if(SystemParams.SHOW_COMPOSITION_GUI) refreshView();
			System.out.println("Agent " + agentID + " selects note "+n);
			return true;
		}
		else {
			System.exit(1);
			return false;
		}
	}
	
	/**
	 * Agent's algorithm to select the next note.
	 * 1. 	Select a sounding to target
	 * 2. 	Find part bounds based on agentID
	 * 3. 	Find a note that fits the target sounding and some other
	 * 	  	music theory rules using the part bounds.
	 * 
	 * @param agentID	ID of the agent executing the algorithm.
	 * @param horiz		The past note.
	 * @param vertical	The note vertical to the agents current position.
	 * @param ahead		Whether or not this agent is ahead of all other agents.
	 * 
	 * @return			The selected note to be placed.
	 */
	static int selectNextNote(int agentID, int horiz, int vertical, boolean ahead) {
		//get lower and upper bounds of the part
		int upperbound, lowerbound;
		int[] partBounds = getPartBounds(agentID);
		lowerbound = partBounds[0];
		upperbound = partBounds[1];
		
		//decide which sounding to target
		Sounding targetSounding;
		double[] probs = {MusicParams.PC_PROB, MusicParams.IC_PROB,
				MusicParams.MD_PROB, MusicParams.SD_PROB};
		int winner_index = ProbabilisticSelectionAlgorithm.run(probs);
		Sounding[] soundings = Sounding.values();
		targetSounding = soundings[winner_index];
		
		Random r = new Random();
		//know horizontal and vertical notes
		if(horiz >= 0 && vertical >= 0) {
			System.out.println("GOOD DECISION :D");
			//get soundings of every note
			ArrayList<Integer> awesomeNotes = new ArrayList<Integer>();
			ArrayList<Integer> okayNotes = new ArrayList<Integer>();
			for(int i=lowerbound; i<upperbound; i++) {
				int pastInterval = (i <= horiz)? 
						getInterval(i, horiz): getInterval(horiz, i);
				int vertInterval = (i <= vertical)?
						getInterval(i, vertical): getInterval(vertical, i);
				Sounding pastIntervalSounding = getSounding(pastInterval);
				Sounding vertIntervalSounding = getSounding(vertInterval);
				int distance = getDistance(i, horiz);
				if(pastIntervalSounding.equals(targetSounding) &&
						vertIntervalSounding.equals(targetSounding) && 
						distance <= 9) {
					awesomeNotes.add(i);
				}
				else if(pastIntervalSounding.equals(targetSounding) && distance <= 9) {
					okayNotes.add(i);
				}
				else if(vertIntervalSounding.equals(targetSounding) && distance <= 9) {
					okayNotes.add(i);
				}
			}
			if(!awesomeNotes.isEmpty()) {
				int randIndex = r.nextInt(awesomeNotes.size());
				return awesomeNotes.get(randIndex);
			}
			else {
				int randIndex = r.nextInt(okayNotes.size());
				return okayNotes.get(randIndex);
			}
		}
		else if(horiz >= 0 && !ahead) {
			System.out.println("Making okay decision");
			ArrayList<Integer> possibleNotes = new ArrayList<Integer>();
			for(int i=lowerbound; i<upperbound; i++) {
				int pastInterval = (i <= horiz)? 
						getInterval(i, horiz): getInterval(horiz, i);
				int distance = getDistance(i, horiz);
				Sounding pastIntervalSounding = getSounding(pastInterval);
				if(pastIntervalSounding.equals(targetSounding) && distance <= 9) {
					possibleNotes.add(i);
				}
			}
			if(!possibleNotes.isEmpty()) {
				int randIndex = r.nextInt(possibleNotes.size());
				return possibleNotes.get(randIndex);
			}
		}
		return -1;
	}
	
	/**
	 * Gets the past notes in an agents part.
	 * This is the perception of the agent of the environment.
	 * Perception length is Parameters.PERCEPT_LENGTH.
	 * Perception length is defined in terms of time instead of array size.
	 * @param agentID	ID of the agent
	 * @return			The list of past notes in the percept length
	 */
	static List<Integer> getPastNotes(int agentID) {
		ArrayList<Integer> pastNotes = new ArrayList<Integer>();
		int index;
		switch(agentID) {
		case AgentParams.BASS: index = bassIndex; break;
		case AgentParams.TENOR: index = tenorIndex;break;
		case AgentParams.ALTO: index = altoIndex; break;
		case AgentParams.SOPRANO: index = sopranoIndex;break;
		default: index = -1;
		}
		double dur = 0d;
		try {
			do {
				Note n = phrases[agentID].getNote(index);
				pastNotes.add(n.getPitch());
				dur += n.getDuration();
				index--;
			} while(dur < SystemParams.WHOLE_NOTE_DURATION-0.001);
		} catch(Exception e) {}
		return pastNotes;
	}
	
	/**
	 * Gets the past positions of notes in an agents part.
	 * This is the perception of the agent of the environment.
	 * Perception length is Parameters.PERCEPT_LENGTH.
	 * Perception length is defined in terms of time instead of array size.
	 * @param agentID	ID of the agent
	 * @return			The list of past positions in the percept length
	 */
	static List<Double> getPastPositions(int agentID) {
		ArrayList<Double> pastPositions = new ArrayList<Double>();
		List<Double> positions;
		int index;
		switch(agentID) {
		case AgentParams.BASS: index = bassIndex; positions = bassPositions; break;
		case AgentParams.TENOR: index = tenorIndex; positions = tenorPositions; break;
		case AgentParams.ALTO: index = altoIndex; positions = altoPositions; break;
		case AgentParams.SOPRANO: index = sopranoIndex; positions = sopranoPositions; break;
		default: index = -1; positions = null;
		}
		double dur = 0d;
		try {
			do {
				Double pos = positions.get(index);
				Note n = phrases[agentID].getNote(index);
				pastPositions.add(pos);
				dur += n.getDuration();
				index--;
			} while(dur < SystemParams.PERCEPT_LENGTH);
		} catch(Exception e) {}
		return pastPositions;
	}

	/**
	 * Writes the composition to a MIDI file.
	 */
	static void writeToMIDI() {
		for(int i=0; i<parts.length; i++) {
			parts[i].removeAllPhrases();
			parts[i].add(phrases[i]);
		}
		composition.removeAllParts();
		composition.addPartList(reverseParts());
		Write.midi(composition, "MAC.mid");
	}

	/**
	 * Helper function to writeToMIDI(). Reversing puts the soprano
	 * part at the top and bass at the bottom in the MIDI file.
	 * @return	Reversed this.parts array.
	 */
	private static Part[] reverseParts() {
		Part[] reversedParts = new Part[4];
		for(int i=parts.length-1, j=0; i>=0; i--, j++) {
			reversedParts[i] = parts[j];
		}
		return reversedParts;
	}
	
	/**
	 * Probabilistically selects a duration for a agentID using algorithm
	 * in ProbabilisticSelectionAlgorithm.java.
	 * @param agentID	ID of the agent running this
	 * @return			selected duration value
	 */
	private static double chooseDuration(int agentID) {
		double[] probs = new double[MusicParams.NUM_DURATIONS];
		if(agentID == AgentParams.BASS) {
			probs[0] = MusicParams.BASS_PROB_WHOLE;
			probs[1] = MusicParams.BASS_PROB_HALF;
			probs[2] = MusicParams.BASS_PROB_QUARTER;
			probs[3] = MusicParams.BASS_PROB_8TH;
			probs[4] = MusicParams.BASS_PROB_16TH;
		}
		else if(agentID == AgentParams.TENOR) {
			probs[0] = MusicParams.TENOR_PROB_WHOLE;
			probs[1] = MusicParams.TENOR_PROB_HALF;
			probs[2] = MusicParams.TENOR_PROB_QUARTER;
			probs[3] = MusicParams.TENOR_PROB_8TH;
			probs[4] = MusicParams.TENOR_PROB_16TH;
		}
		else if (agentID == AgentParams.ALTO) {
			probs[0] = MusicParams.ALTO_PROB_WHOLE;
			probs[1] = MusicParams.ALTO_PROB_HALF;
			probs[2] = MusicParams.ALTO_PROB_QUARTER;
			probs[3] = MusicParams.ALTO_PROB_8TH;
			probs[4] = MusicParams.ALTO_PROB_16TH;
		}
		else {
			probs[0] = MusicParams.SOPRANO_PROB_WHOLE;
			probs[1] = MusicParams.SOPRANO_PROB_HALF;
			probs[2] = MusicParams.SOPRANO_PROB_QUARTER;
			probs[3] = MusicParams.SOPRANO_PROB_8TH;
			probs[4] = MusicParams.SOPRANO_PROB_16TH;
		}
		double[] durations = {WHOLE_NOTE, HALF_NOTE, QUARTER_NOTE, EIGHTH_NOTE, SIXTEENTH_NOTE};
		int winner_index = ProbabilisticSelectionAlgorithm.run(probs);
		return durations[winner_index];
	}

	/**
	 * Gets the note occurring at the agents position.
	 * @param vertNotes		Notes vertical to the agent.
	 * @param vertPositions	Position of notes vertical to the agent.
	 * @param position		Position of the agent.
	 * @return				The note occurring at the position of the agent.
	 */
	private static int getNoteAtCurrentTime(List<Integer> vertNotes, List<Double> vertPositions, double position) {
		if(vertNotes.size() == 1) return vertNotes.get(0);
		if(vertNotes.size() != vertPositions.size()) return -1;
		for(int i=0; i<vertNotes.size()-1; i++) {
			double vertPos1 = vertPositions.get(i);
			double vertPos2 = vertPositions.get(i+1);
			if(position <= vertPos1 && position >= vertPos2) {
				return vertNotes.get(i);
			}
		}
		return -1;
	}
	
	/**
	 * Figures out whether an agent is too far ahead, meaning that the other agents are lagging.
	 * @param vertPositions	Positions of notes vertical to the agent.
	 * @param position		Position of the agent.
	 * @return		<code>true</code> if the agent is ahead of all notes vertical to it.
	 * 				<code>false</code> otherwise.
	 */
	private static boolean isAhead(List<Double> vertPositions, double position) {
		return (position-SystemParams.LAGGING_LENGTH > vertPositions.get(0));
	}
	
	/**
	 * Calculates the interval ID of two notes.
	 * @param lower	Lower note.
	 * @param upper	Upper note.
	 * @return		Interval ID of lower by upper note.
	 */
	private static int getInterval(int lower, int upper) {
		return (upper-lower)%12;
	}
	
	/**
	 * Calculates the distance between two notes.
	 * @param n1	Note 1.
	 * @param n2	Note 2.
	 * @return		Absolute value of the difference of the notes.
	 */
	private static int getDistance(int n1, int n2) {
		return Math.abs(n2-n1);
	}
	
	/**
	 * Gets the associated sounding of an interval ID.
	 * @param intervalID	ID of the interval.
	 * @return				Sounding of the interval ID.
	 */
	private static Sounding getSounding(int intervalID) {
		switch(intervalID) {
		case MusicParams.UNISON: return Sounding.PERFECT_CONSONANCE;
		case MusicParams.m2nd: return Sounding.SHARP_DISSONANCE;
		case MusicParams.M2nd: return Sounding.MILD_DISSONANCE;
		case MusicParams.m3rd: return Sounding.IMPERFECT_CONSONANCE;
		case MusicParams.M3rd: return Sounding.IMPERFECT_CONSONANCE;
		case MusicParams.P4th: return Sounding.PERFECT_CONSONANCE;
		case MusicParams.TRITONE: return Sounding.SHARP_DISSONANCE;
		case MusicParams.P5th: return Sounding.PERFECT_CONSONANCE;
		case MusicParams.m6th: return Sounding.IMPERFECT_CONSONANCE;
		case MusicParams.M6th: return Sounding.IMPERFECT_CONSONANCE;
		case MusicParams.m7th: return Sounding.MILD_DISSONANCE;
		case MusicParams.M7th: return Sounding.SHARP_DISSONANCE;
		default: {
			System.err.println("Bad Interval");
			return null;
		}
		}
	}
	
	/**
	 * Returns the bounds of the part of an agent.
	 * @param agentID	ID of the agent (part ID).
	 * @return		An array of size 2 where the first element is the lower bound
	 * 				and the second element is the upper bound.
	 */
	private static int[] getPartBounds(int agentID) {
		switch(agentID) {
		case AgentParams.BASS: 
			return new int[] {MusicParams.BASS_LOWER, MusicParams.BASS_UPPER};
		case AgentParams.TENOR:
			return new int[] {MusicParams.TENOR_LOWER, MusicParams.TENOR_UPPER};
		case AgentParams.ALTO:
			return new int[] {MusicParams.ALTO_LOWER, MusicParams.ALTO_UPPER};
		case AgentParams.SOPRANO:
			return new int[] {MusicParams.SOPRANO_LOWER, MusicParams.SOPRANO_UPPER};
		default: 
			return null;
		}
	}
	
	/**
	 * Opens the view of the composition.
	 */
	private static void openView() {
		for(int i=0; i<parts.length; i++) {
			parts[i].removeAllPhrases();
			parts[i].add(phrases[i]);
		}
		composition.removeAllParts();
		composition.addPartList(parts);
		switch(SystemParams.VIEW_TYPE) {
		case NOTATION: {
			notateWindow = new Notate(composition, 0, 0);
			notateWindow.setFocusable(false);
			notateWindow.setFocusableWindowState(false);
		} break;
		case MIDISCORE: {
			showWindow = new ShowScore(composition, 0, 0);
			showWindow.setFocusable(false);
			showWindow.setFocusableWindowState(false);
		} break;
		case SKETCH: {
			sketchWindow = new SketchScore(composition, 0, 0);
			sketchWindow.setFocusable(false);
			sketchWindow.setFocusableWindowState(false);
		} break;
		}
	}
	
	/**
	 * Refreshes the view of the composition by 
	 * disposing and re-opening the view.
	 */
	static void refreshView() {
		switch(SystemParams.VIEW_TYPE) {
		case NOTATION: {
			notateWindow.dispose();
			openView();
		} break;
		case MIDISCORE: {
			showWindow.dispose();
			openView();
		} break;
		case SKETCH: {
			sketchWindow.update();
		} break;
		}
	}
	
	/**
	 * Returns the phrase associated to the agent ID.
	 * @param agent		The ID of the agent
	 * @return			The associated phrase of the agent.
	 */
	static Phrase getPhrase(int agent) {
		return phrases[agent];
	}
	
}