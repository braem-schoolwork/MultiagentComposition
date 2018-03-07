import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.GridWorldModel;
import jm.JMC;
import jm.music.data.*;
import jm.util.*;

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
	
    static Score score = new Score("MAC", Parameters.BPM);	//100bpm
    static Part[] parts = new Part[4];
    static Phrase[] phrases = new Phrase[4];
	
	public Model() {
		//create parts
		parts[0] = new Part("Bass", CELLO, 0);
		phrases[0] = new Phrase("BassPhrase", 0.0);
		phrases[0].addNote(C3, WHOLE_NOTE);
		parts[1] = new Part("Viola", VIOLA, 0);
		phrases[1] = new Phrase("TenorPhrase", 0.0);
		phrases[1].addNote(C4, WHOLE_NOTE);
		parts[2] = new Part("Violin 2", VIOLIN, 0);
		phrases[2] = new Phrase("AltoPhrase", 0.0);
		phrases[2].addNote(C4, WHOLE_NOTE);
		parts[3] = new Part("Violin 1", VIOLIN, 0);
		phrases[3] = new Phrase("SopranoPhrase", 0.0);
		phrases[3].addNote(C5, WHOLE_NOTE);
	}
	
	boolean placeNote(int agentID, int verticalNote) {
		double duration = chooseDuration(agentID);
		int nextNote;
		Phrase phr = phrases[agentID];
		switch(agentID) {
		case Parameters.BASS: {
			bassPosition+=duration; 
			nextNote = selectNextNote
					(agentID, phr.getNote(bassIndex).getPitch(), verticalNote);
			bassIndex++;
		} break;
		case Parameters.TENOR: {
			bassPosition+=duration; 
			nextNote = selectNextNote
					(agentID, phr.getNote(tenorIndex).getPitch(), verticalNote);
			tenorIndex++;
		} break;
		case Parameters.ALTO: {
			bassPosition+=duration; 
			nextNote = selectNextNote
					(agentID, phr.getNote(altoIndex).getPitch(), verticalNote);
			altoIndex++;
		} break;
		case Parameters.SOPRANO: {
			bassPosition+=duration; 
			nextNote = selectNextNote
					(agentID, phr.getNote(sopranoIndex).getPitch(), verticalNote);
			sopranoIndex++;
		} break;
		default: nextNote = -1;
		}
		Note n = new Note(nextNote, duration);
		phrases[agentID].addNote(n);
		System.out.println("Pitch: " + n.getPitch() + ", " + n.getDuration());
		return true;
	}
	

	/*** HELPERS ***/
	static void writeToMIDI() {
		for(int i=0; i<parts.length; i++) {
			parts[i].add(phrases[i]);
		}
		score.addPartList(parts);
		Write.midi(score, "MAC.mid");
	}
	
	int selectNextNote(int agentID, int horiz, int vertical) {
		Random r = new Random();
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
		randomNote = r.nextInt(upperbound - lowerbound) + lowerbound + 1;
		return randomNote;
		/*
		//know horizontal and vertical notes
		if(horiz >= 0 && vertical >= 0) {
			
		}
		//know only horizontal note
		else if(horiz >= 0) {
			
		}
		//know only vertical note
		else if(vertical >= 0){
			
		}
		//dont know either.. just pick a random note
		else {
		}
		*/
	}
	
	ArrayList<Integer> getPastNotes(int agentID) {
		ArrayList<Integer> pastNotes = new ArrayList<Integer>();
		int index;
		switch(agentID) {
		case Parameters.BASS: index = bassIndex; break;
		case Parameters.TENOR: index = tenorIndex; break;
		case Parameters.ALTO: index = altoIndex; break;
		case Parameters.SOPRANO: index = sopranoIndex; break;
		default: index = -1;
		}
		double dur = 0d;
		do {
			Note n = phrases[agentID].getNote(index);
			pastNotes.add(n.getPitch());
			dur += n.getDuration();
			index--;
		} while(dur < 3.6);
		return pastNotes;
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
			System.exit(1);
			return -1;
		}
	}
	
}