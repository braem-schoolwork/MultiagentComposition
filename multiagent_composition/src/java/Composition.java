import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

import jm.JMC;
import jm.music.data.*;
import jm.util.*;

public class Composition extends Environment {
	private Logger logger = Logger.getLogger("MAC"+Composition.class.getName());
    
    static Model model;
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        //make user input window
        if(Parameters.USER_INPUT) {
            UserInputWindow inputWindow = new UserInputWindow();
            inputWindow.setVisible(true);
        }
        model = new Model();
        updatePercepts();
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
    	//write composition to midi
    	Model.writeToMIDI();
        super.stop();
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        
    	boolean result = false;
    	int agentID = -1;
        if (agName.equals("bassAgent")) agentID=Parameters.BASS;
        if (agName.equals("tenorAgent")) agentID=Parameters.TENOR;
        if (agName.equals("altoAgent")) agentID=Parameters.ALTO;
        if (agName.equals("sopranoAgent")) agentID=Parameters.SOPRANO;

		//control the speed of agents
		try { Thread.sleep(Parameters.SLEEP_AMOUNT); } catch (InterruptedException x) { }
        
    	if (action.getFunctor().equals("placeNote")) {
    		List<Integer> notes = new ArrayList<Integer>();
            List<Double> positions = new ArrayList<Double>();
            Term[] terms = action.getTermsArray();
            Double position = Double.parseDouble(terms[2].toString());
            String firstTerm = terms[0].toString();
            String secondTerm = terms[1].toString();
            firstTerm = firstTerm.replace("[", "");
            firstTerm = firstTerm.replace("]", "");
            secondTerm = secondTerm.replace("[", "");
            secondTerm = secondTerm.replace("]", "");
            String[] noteStrs = firstTerm.split(",");
            String[] posStrs = secondTerm.split(",");
            for(String n : noteStrs)
            	notes.add(Integer.parseInt(n));
            for(String p : posStrs)
            	positions.add(Double.parseDouble(p));
    		result = model.placeNote(agentID, notes, positions, position);
    	}
    	else if(action.getFunctor().equals("wait")) {
    		Term[] terms = action.getTermsArray();
    		int waitTime = Integer.parseInt(terms[0].toString());
    		try { Thread.sleep(waitTime); } catch (InterruptedException x) { }
    	}
    	else if(action.getFunctor().equals("getUserInput")) {
    		//
    	}
    	else
    		logger.info("executing: "+action+", but not implemented!");
    	
    	if (result) {
    		updatePercepts();
    	}
    	
    	return result;
    }
    
    private void updatePercepts() {
    	clearPercepts();

    	int note = Model.phrases[Parameters.BASS].getNote(model.bassIndex).getPitch();
    	addPercept("bassAgent", Literal.parseLiteral("prevNote(" + note + ")"));
    	
    	double pos = model.bassPosition;
    	addPercept("bassAgent", Literal.parseLiteral("position(" + pos + ")"));
    	
    	List<Integer> pastNotes = model.getPastNotes(Parameters.BASS);
    	addPercept("bassAgent", Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
    	
    	List<Double> pastPositions = model.getPastPositions(Parameters.BASS);
    	addPercept("bassAgent", Literal.parseLiteral("pastPositions(" + pastPositions + ")"));
    	
    	if(Parameters.NUM_AGENTS > 1) {
    		note = Model.phrases[Parameters.SOPRANO].getNote(model.sopranoIndex).getPitch();
        	addPercept("sopranoAgent", Literal.parseLiteral("prevNote(" + note + ")"));
        	
        	pos = model.sopranoPosition;
        	addPercept("sopranoAgent", Literal.parseLiteral("position(" + pos + ")"));
        	
        	pastNotes = model.getPastNotes(Parameters.SOPRANO);
        	addPercept("sopranoAgent", Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
        	
        	pastPositions = model.getPastPositions(Parameters.SOPRANO);
        	addPercept("sopranoAgent", Literal.parseLiteral("pastPositions(" + pastPositions + ")"));
    	}
    	
    }
}
