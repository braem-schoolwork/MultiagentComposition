import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.List;
import java.util.logging.*;


public class Composition extends Environment {
	private Logger logger = Logger.getLogger("MAC"+Composition.class.getName());
    
    private static final Literal placeNote = Literal.parseLiteral("placeNote");
    
    Model model;
    
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
        super.stop();
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        
    	boolean result = false;
    	
    	if (action.equals(placeNote))
    		result = model.placeNote();
    	else
    		logger.info("executing: "+action+", but not implemented!");
    	
    	if (result) {
    		updatePercepts();
    		try { Thread.sleep(800); } catch (InterruptedException x) { }
    	}
    	
    	return result;
    }
    
    private void updatePercepts() {
    	clearPercepts();
    	
    	/*
    	Location loc = model.getAgPos(0);
    	addPercept(Literal.parseLiteral("position(" + loc.x + ", " + loc.y +")"));
    	
    	List<Location> dirts = model.getDirtLocations();
    	for (int i=0; i<dirts.size(); i++) {
    		addPercept(Literal.parseLiteral("dirt(" + dirts.get(i).x + ", " + dirts.get(i).y + ")"));
    	}
    	
    	//agent perceives the grid dimensions
    	addPercept(Literal.parseLiteral("gridwidth(" + model.getGridWidth() + ")"));
    	addPercept(Literal.parseLiteral("gridheight(" + model.getGridHeight() + ")"));  
    	*/  	
    }
}
