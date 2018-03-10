import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.GridWorldModel;
import jm.JMC;
import jm.music.data.*;
import jm.util.*;
import jm.gui.show.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.*;

public class Model implements JMC {
    double bassPosition = 0d;
    int bassIndex = 0;
    double tenorPosition = 0d;
    int tenorIndex = 0;
    double altoPosition = 0d;
    int altoIndex = 0;
    double sopranoPosition = 0d;
    int sopranoIndex = 0;
    
    List<Double> bassPositions = new ArrayList<Double>();
    List<Double> tenorPositions = new ArrayList<Double>();
    List<Double> altoPositions = new ArrayList<Double>();
    List<Double> sopranoPositions = new ArrayList<Double>();
	
    static Score score = new Score("MAC", Parameters.BPM);	//100bpm
    static Part[] parts = new Part[4];
    static Phrase[] phrases = new Phrase[4];
    static ShowScore s;
	
	public Model() {
		//create parts
		Note n1 = new Note(C3, WHOLE_NOTE);
		Note n2 = new Note(C4, WHOLE_NOTE);
		Note n3 = new Note(C5, WHOLE_NOTE);
		parts[0] = new Part("Bass", PIANO, 0);
		phrases[0] = new Phrase("BassPhrase", 0.0);
		phrases[0].addNote(n1);
		bassPosition+=n1.getDuration();
		bassPositions.add(n1.getDuration());
		parts[1] = new Part("Viola", VIOLA, 0);
		phrases[1] = new Phrase("TenorPhrase", 0.0);
		phrases[1].addNote(n2);
		tenorPosition+=n2.getDuration();
		tenorPositions.add(n2.getDuration());
		parts[2] = new Part("Violin 2", VIOLIN, 0);
		phrases[2] = new Phrase("AltoPhrase", 0.0);
		phrases[2].addNote(n2);
		altoPosition+=n2.getDuration();
		altoPositions.add(n2.getDuration());
		parts[3] = new Part("Violin 1", PIANO, 0);
		phrases[3] = new Phrase("SopranoPhrase", 0.0);
		phrases[3].addNote(n3);
		sopranoPosition+=n3.getDuration();
		sopranoPositions.add(n3.getDuration());
		if(Parameters.GUI) openView();
	}
	
	static void refreshView() {
		for(int i=0; i<parts.length; i++) {
			parts[i].removeAllPhrases();
			parts[i].add(phrases[i]);
		}
		score.removeAllParts();
		score.addPartList(parts);
		s.dispose();
		s = new ShowScore(score);
	}
	
	static void openView() {
		for(int i=0; i<parts.length; i++) {
			parts[i].removeAllPhrases();
			parts[i].add(phrases[i]);
		}
		score.removeAllParts();
		score.addPartList(parts);
		s = new ShowScore(score);
	}
	
	boolean placeNote(int agentID, List<Integer> vertNotes, List<Double> vertPositions, double position) {
		int vertNote = getNoteAtCurrentTime(vertNotes, vertPositions, position);
		//System.out.println(vertNote + ", " + vertNotes);
		double duration = chooseDuration(agentID);
		int nextNote;
		Phrase phr = phrases[agentID];
		switch(agentID) {
		case Parameters.BASS: {
			bassPosition+=duration; 
			bassPositions.add(bassPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(bassIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			bassIndex++;
		} break;
		case Parameters.TENOR: {
			tenorPosition+=duration; 
			tenorPositions.add(tenorPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(tenorIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			tenorIndex++;
		} break;
		case Parameters.ALTO: {
			altoPosition+=duration; 
			altoPositions.add(altoPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(altoIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			altoIndex++;
		} break;
		case Parameters.SOPRANO: {
			sopranoPosition+=duration; 
			sopranoPositions.add(sopranoPosition);
			nextNote = selectNextNote
					(agentID, phr.getNote(sopranoIndex).getPitch(), 
							vertNote, isAhead(vertPositions, position));
			sopranoIndex++;
		} break;
		default: nextNote = -1;
		}
		if(nextNote > 0) {
			Note n = new Note(nextNote, duration);
			phrases[agentID].addNote(n);
			if(Parameters.GUI) refreshView();
			System.out.println("Agent " + agentID + " selects note "+n);
			return true;
		}
		else return false;
	}
	

	/*** HELPERS ***/
	int getNoteAtCurrentTime(List<Integer> vertNotes, List<Double> vertPositions, double position) {
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
	
	boolean isAhead(List<Double> vertPositions, double position) {
		return (position > vertPositions.get(vertPositions.size()-1));
	}
	
	static void writeToMIDI() {
		for(int i=0; i<parts.length; i++) {
			parts[i].removeAllPhrases();
			parts[i].add(phrases[i]);
		}
		score.removeAllParts();
		score.addPartList(parts);
		Write.midi(score, "MAC.mid");
	}
	
	int getInterval(int lower, int upper) {
		return (upper-lower)%12;
	}
	int getDistance(int n1, int n2) {
		return Math.abs(n2-n1);
	}
	Sounding getSounding(int interval) {
		switch(interval) {
		case Parameters.UNISON: return Sounding.PERFECT_CONSONANCE;
		case Parameters.m2nd: return Sounding.SHARP_DISSONANCE;
		case Parameters.M2nd: return Sounding.MILD_DISSONANCE;
		case Parameters.m3rd: return Sounding.IMPERFECT_CONSONANCE;
		case Parameters.M3rd: return Sounding.IMPERFECT_CONSONANCE;
		case Parameters.P4th: return Sounding.PERFECT_CONSONANCE;
		case Parameters.TRITONE: return Sounding.SHARP_DISSONANCE;
		case Parameters.P5th: return Sounding.PERFECT_CONSONANCE;
		case Parameters.m6th: return Sounding.IMPERFECT_CONSONANCE;
		case Parameters.M6th: return Sounding.IMPERFECT_CONSONANCE;
		case Parameters.m7th: return Sounding.MILD_DISSONANCE;
		case Parameters.M7th: return Sounding.SHARP_DISSONANCE;
		default: {
			System.err.println("Bad Interval");
			return null;
		}
		}
	}
	
	int selectNextNote(int agentID, int horiz, int vertical, boolean ahead) {
		//get lower and upper bounds of the part
		int randomNote, upperbound, lowerbound;
		switch(agentID) {
		case Parameters.BASS: 
			upperbound = Parameters.BASS_UPPER;
			lowerbound = Parameters.BASS_LOWER;
			break;
		case Parameters.TENOR:
			upperbound = Parameters.TENOR_UPPER;
			lowerbound = Parameters.TENOR_LOWER;
			break;
		case Parameters.ALTO:
			upperbound = Parameters.ALTO_UPPER;
			lowerbound = Parameters.ALTO_LOWER;
			break;
		default: 
			upperbound = Parameters.SOPRANO_UPPER;
			lowerbound = Parameters.SOPRANO_LOWER;
			break;
		}
		
		//decide which sounding to target
		Sounding targetSounding;
		float probPC, probIC, probMD, probSD;
		probPC = Parameters.PC_PROB;
		probIC = Parameters.IC_PROB;
		probMD = Parameters.MD_PROB;
		probSD = Parameters.SD_PROB;
		//get accumulative probabilities
		probIC += probPC;
		probMD += probIC;
		probSD += probMD;
		double randomNum = Math.random(); //between 0 and 1
		if(randomNum < probPC) {
			targetSounding = Sounding.PERFECT_CONSONANCE;
		}
		else if(randomNum >= probPC && randomNum < probIC) {
			targetSounding = Sounding.IMPERFECT_CONSONANCE;
		}
		else if(randomNum >= probIC && randomNum < probMD) {
			targetSounding = Sounding.MILD_DISSONANCE;
		}
		else if(randomNum >= probMD && randomNum < probSD) {
			targetSounding = Sounding.SHARP_DISSONANCE;
		}
		else { targetSounding = Sounding.IMPERFECT_CONSONANCE; }
		
		Random r = new Random();
		randomNote = r.nextInt(upperbound - lowerbound) + lowerbound + 1;
		//know horizontal and vertical notes
		if(horiz >= 0 && vertical >= 0) {
			//get soundings of every note
			ArrayList<Integer> awesomeNotes = new ArrayList<Integer>();
			ArrayList<Integer> okayNotes = new ArrayList<Integer>();
			for(int i=lowerbound, j=0; i<upperbound; i++, j++) {
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
		//know only horizontal note and agent is leading
		else if(horiz >= 0 && ahead) {
			System.out.println(agentID + " is ahead");
			try {
				Thread.sleep(Parameters.CATCHUP_AMOUNT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//get soundings of every note
			ArrayList<Integer> possibleNotes = new ArrayList<Integer>();
			for(int i=lowerbound, j=0; i<upperbound; i++, j++) {
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
		else if(horiz >= 0 && !ahead) {
			System.out.println(agentID + " is behind");
			return randomNote;
		}
		//know only vertical note
		else if(vertical >= 0){
			System.out.println("this shouldnt execute");
		}
		//dont know either.. just pick a random note. Should not come here
		else {
			return randomNote;
		}
		return -1;
	}
	
	List<Integer> getPastNotes(int agentID) {
		ArrayList<Integer> pastNotes = new ArrayList<Integer>();
		int index;
		switch(agentID) {
		case Parameters.BASS: index = bassIndex; break;
		case Parameters.TENOR: index = tenorIndex;break;
		case Parameters.ALTO: index = altoIndex; break;
		case Parameters.SOPRANO: index = sopranoIndex;break;
		default: index = -1;
		}
		double dur = 0d;
		try {
			do {
				Note n = phrases[agentID].getNote(index);
				pastNotes.add(n.getPitch());
				dur += n.getDuration();
				index--;
			} while(dur < 7.2-0.001);
		} catch(Exception e) {}
		return pastNotes;
	}
	
	List<Double> getPastPositions(int agentID) {
		ArrayList<Double> pastPositions = new ArrayList<Double>();
		List<Double> positions;
		int index;
		switch(agentID) {
		case Parameters.BASS: index = bassIndex; positions = bassPositions; break;
		case Parameters.TENOR: index = tenorIndex; positions = tenorPositions; break;
		case Parameters.ALTO: index = altoIndex; positions = altoPositions; break;
		case Parameters.SOPRANO: index = sopranoIndex; positions = sopranoPositions; break;
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
			} while(dur < 7.2-0.001);
		} catch(Exception e) {}
		return pastPositions;
	}
	
	double chooseDuration(int agentID) {
		float probWhole, probHalf, probQuarter, prob8th, prob16th;
		if(agentID == Parameters.BASS) {
			probWhole = Parameters.BASS_PROB_WHOLE;
			probHalf = Parameters.BASS_PROB_HALF;
			probQuarter = Parameters.BASS_PROB_QUARTER;
			prob8th = Parameters.BASS_PROB_8th;
			prob16th = Parameters.BASS_PROB_16th;
		}
		else if(agentID == Parameters.TENOR) {
			probWhole = Parameters.TENOR_PROB_WHOLE;
			probHalf = Parameters.TENOR_PROB_HALF;
			probQuarter = Parameters.TENOR_PROB_QUARTER;
			prob8th = Parameters.TENOR_PROB_8th;
			prob16th = Parameters.TENOR_PROB_16th;
		}
		else if (agentID == Parameters.ALTO) {
			probWhole = Parameters.ALTO_PROB_WHOLE;
			probHalf = Parameters.ALTO_PROB_HALF;
			probQuarter = Parameters.ALTO_PROB_QUARTER;
			prob8th = Parameters.ALTO_PROB_8th;
			prob16th = Parameters.ALTO_PROB_16th;
		}
		else {
			probWhole = Parameters.SOPRANO_PROB_WHOLE;
			probHalf = Parameters.SOPRANO_PROB_HALF;
			probQuarter = Parameters.SOPRANO_PROB_QUARTER;
			prob8th = Parameters.SOPRANO_PROB_8th;
			prob16th = Parameters.SOPRANO_PROB_16th;
		}
		//get accumulative probabilities
		probHalf += probWhole;
		probQuarter += probHalf;
		prob8th += probQuarter;
		prob16th += prob8th;
		double randomNum = Math.random(); //between 0 and 1
		if(randomNum < probWhole) {
			return WHOLE_NOTE;
		}
		if(randomNum >= probWhole && randomNum < probHalf) {
			return HALF_NOTE;
		}
		if(randomNum >= probHalf && randomNum < probQuarter) {
			return QUARTER_NOTE;
		}
		if(randomNum >= probQuarter && randomNum < prob8th) {
			return EIGHTH_NOTE;
		}
		if(randomNum >= prob8th && randomNum < prob16th) {
			return SIXTEENTH_NOTE;
		}
		else {
			System.err.println("Bad Duration Probability Algorithm");
			return -1;
		}
	}
	
}