import jason.asSyntax.*;
import jason.environment.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the environment of the system, which is a composition.
 * This deals with agent agents and perceptions by using Model.java.
 * @author braem
 */
public class Composition extends Environment {
    
	/**
	 * Associated model of the system
	 */
    static Model model;
    
    @Override
    public void init(String[] args) {
        super.init(args);
        SettingsLoader.load();
        if(!MusicParams.isValidParameters()) {
        	System.err.println("!Invalid Parameters!");
        	System.err.println("Program Terminating in 5 seconds..");
        	try { Thread.sleep(5000); } catch (InterruptedException x) { }
        	System.exit(0);
        }
        if(SystemParams.ACCEPT_USER_INPUT) {
            UserInputWindow inputWindow = new UserInputWindow();
            inputWindow.setVisible(true);
        }
        
        Model.init();
        updatePercepts();
    }

    @Override
    public void stop() {
    	Model.writeToMIDI();
        super.stop();
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
    	boolean result = false;
    	
    	//get ID of agent executing the action
    	int agentID = -1;
        if (agName.equals("bassAgent")) agentID=AgentParams.BASS;
        if (agName.equals("tenorAgent")) agentID=AgentParams.TENOR;
        if (agName.equals("altoAgent")) agentID=AgentParams.ALTO;
        if (agName.equals("sopranoAgent")) agentID=AgentParams.SOPRANO;

		//control the speed of agents
		//try { Thread.sleep(SystemParams.AGENT_DELAY); } catch (InterruptedException x) { }
        
		//place note action
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
    		result = Model.placeNote(agentID, notes, positions, position, false, null);
    	}
    	//wait action
    	else if(action.getFunctor().equals("wait")) {
    		try { Thread.sleep(SystemParams.AGENT_DELAY); } catch (InterruptedException x) { }
    	}
    	else {	//unknown action
    		System.err.println("!Unknown action " + action.getFunctor() + "!");
    		System.err.println("Shutting down in 5 seconds..");
    		try { Thread.sleep(5000); } catch (InterruptedException x) { }
        	System.exit(0);
    	}
    	
    	if(result)
    		updatePercepts();
    	
    	return result;
    }
    
    /**
     * Updates perceptions of ALL agents
     */
    private void updatePercepts() {
    	clearPercepts();

    	updatePercepts(AgentParams.BASS, "bassAgent", Model.bassIndex, Model.bassPosition);
    	if(AgentParams.NUM_AGENTS > 1) {
    		updatePercepts(AgentParams.SOPRANO, "sopranoAgent", Model.sopranoIndex, Model.sopranoPosition);
    	}
    	else if(AgentParams.NUM_AGENTS == 4) {
    		updatePercepts(AgentParams.TENOR, "tenorAgent", Model.tenorIndex, Model.tenorPosition);
    		updatePercepts(AgentParams.ALTO, "altoAgent", Model.altoIndex, Model.altoPosition);
    	}
    }
    
    /**
     * Update perception of a single agent.
     * @param agentID		ID of the agent.
     * @param agentName		Name of the agent.
     * @param index			Index of phrase corresponding to the agent.
     * @param position		Position of the agent.
     */
    private void updatePercepts(int agentID, String agentName, int index, double position) {
    	int note = Model.getPhrase(agentID).getNote(index).getPitch();
    	addPercept(agentName, Literal.parseLiteral("prevNote(" + note + ")"));
    	
    	double pos = position;
    	addPercept(agentName, Literal.parseLiteral("position(" + pos + ")"));
    	
    	List<Integer> pastNotes = Model.getPastNotes(agentID);
    	addPercept(agentName, Literal.parseLiteral("pastNotes(" + pastNotes + ")"));
    	
    	List<Double> pastPositions = Model.getPastPositions(agentID);
    	addPercept(agentName, Literal.parseLiteral("pastPositions(" + pastPositions + ")"));
    }
}
