import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import jm.JMC;
import jm.music.data.*;
import jm.util.*;

public class Composition extends Environment {
	private Logger logger = Logger.getLogger("MAC"+Composition.class.getName());
    
    private static final Literal placeNote = Literal.parseLiteral("placeNote");
    
    static Model model;
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
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
    	
    	if (action.equals(placeNote))
    		result = model.placeNote(agentID, 60);
    	else
    		logger.info("executing: "+action+", but not implemented!");
    	
    	if (result) {
    		updatePercepts();
    		//control the speed of agents
    		if(agentID == Parameters.BASS)
    			try { Thread.sleep(1400); } catch (InterruptedException x) { }
    		else if(agentID == Parameters.TENOR || agentID == Parameters.ALTO)
    			try { Thread.sleep(1000); } catch (InterruptedException x) { }
    		else if(agentID == Parameters.SOPRANO)
    			try { Thread.sleep(800); } catch (InterruptedException x) { }
    	}
    	
    	return result;
    }
    
    private void updatePercepts() {
    	clearPercepts();

    	int note = Model.phrases[Parameters.BASS].getNote(model.bassIndex).getPitch();
    	addPercept("bassAgent", Literal.parseLiteral("prevNote(" + note + ")"));
    	
    	double pos = model.bassPosition;
    	addPercept("bassAgent", Literal.parseLiteral("position(" + pos + ")"));
    	
    	ArrayList<Integer> pastNotes = model.getPastNotes(Parameters.BASS);
    	addPercept("bassAgent", Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
    	
    	if(Parameters.NUM_AGENTS > 1) {
    		note = Model.phrases[Parameters.SOPRANO].getNote(model.sopranoIndex).getPitch();
        	addPercept("sopranoAgent", Literal.parseLiteral("prevNote(" + note + ")"));
        	
        	pos = model.sopranoPosition;
        	addPercept("sopranoAgent", Literal.parseLiteral("position(" + pos + ")"));
        	
        	pastNotes = model.getPastNotes(Parameters.SOPRANO);
        	addPercept("sopranoAgent", Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
    	}
    	
    	if(Parameters.NUM_AGENTS > 2) {
    		note = Model.phrases[Parameters.ALTO].getNote(model.altoIndex).getPitch();
        	addPercept("altoAgent", Literal.parseLiteral("prevNote(" + note + ")"));
        	
        	pos = model.altoPosition;
        	addPercept("altoAgent", Literal.parseLiteral("position(" + pos + ")"));
        	
        	pastNotes = model.getPastNotes(Parameters.ALTO);
        	addPercept("altoAgent", Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
    	}
    	
    	if(Parameters.NUM_AGENTS > 3) {
    		note = Model.phrases[Parameters.TENOR].getNote(model.tenorIndex).getPitch();
        	addPercept("tenorAgent", Literal.parseLiteral("prevNote(" + note + ")"));
        	
        	pos = model.tenorPosition;
        	addPercept("tenorAgent", Literal.parseLiteral("position(" + pos + ")"));
        	
        	pastNotes = model.getPastNotes(Parameters.TENOR);
        	addPercept("tenorAgent", Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
    	}	
    }
}
