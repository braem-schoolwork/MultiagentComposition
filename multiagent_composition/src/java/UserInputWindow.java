import javax.swing.*;

import jm.music.data.Note;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Window for user input. 
 * Allows the user to operate on the environment.
 * @author braem
 */
public class UserInputWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Dropdown for parts.
	 */
	private JComboBox<String> parts;
	/**
	 * Dropdown for notes.
	 * Selectable notes change when the part changes.
	 */
	private JComboBox<Integer> notes;
	/**
	 * Dropdown for durations.
	 * 4 = Whole note, 2 = half note, 1 = quarter note,
	 * 0.5 = eighth note, 0.25 = sixteenth note.
	 */
	private JComboBox<Double> durations;

	public UserInputWindow() {
	    setTitle("User Input Window");
	    setSize(475, 110);
	    setLayout(null);
	    
	    parts = new JComboBox<String>();
	    notes = new JComboBox<Integer>();
	    durations = new JComboBox<Double>();
	    notes.setEditable(true);
	    
	    parts.setBounds(25, 25, 100, 25);
	    notes.setBounds(125, 25, 100, 25);
	    durations.setBounds(225, 25, 100, 25);
	    
	    parts.addItem("Bass");
	    parts.addItem("Tenor");
	    parts.addItem("Alto");
	    parts.addItem("Soprano");
	    parts.setSelectedIndex(0);
	    
	    durations.addItem(4d);
	    durations.addItem(2d);
	    durations.addItem(1d);
	    durations.addItem(0.5d);
	    durations.addItem(0.25d);
	    
	    for(int i=MusicParams.BASS_LOWER; i<=MusicParams.BASS_UPPER; i++) {
	    	notes.addItem(i);
	    }
	    
	    parts.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String newPart = (String) e.getItemSelectable().getSelectedObjects()[0];
				int lower, upper;
				if(newPart.equals("Bass")) {
					lower = MusicParams.BASS_LOWER;
					upper = MusicParams.BASS_UPPER;
				}
				else if(newPart.equals("Tenor")) {
					lower = MusicParams.TENOR_LOWER;
					upper = MusicParams.TENOR_UPPER;
				}
				else if(newPart.equals("Alto")) {
					lower = MusicParams.ALTO_LOWER;
					upper = MusicParams.ALTO_UPPER;
				}
				else if(newPart.equals("Soprano")) {
					lower = MusicParams.SOPRANO_LOWER;
					upper = MusicParams.SOPRANO_UPPER;
				}
				else {
					lower = MusicParams.BASS_LOWER;
					upper = MusicParams.BASS_UPPER;					
				}
				notes.removeAllItems();
				for(int i=lower; i<=upper; i++) {
			    	notes.addItem(i);
			    }
			}
	    });
	   
	    Button b = new Button();
	    b.setBounds(325, 20, 125, 35);
	    b.setLabel("Place Note");
	    b.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent event) {
	     	    Note n = new Note();
	     	    n.setPitch((Integer) notes.getSelectedItem());
	     	    n.setRhythmValue((Double) durations.getSelectedItem(), true);
	     	    Model.getPhrase(parts.getSelectedIndex()).addNote(n);
	     	    if(SystemParams.SHOW_COMPOSITION_GUI) Model.refreshView();
		    }
	    });

	    add(parts);
	    add(notes);
	    add(durations);
	    add(b);
	    setLocation(0, 550);
	    this.setAlwaysOnTop(true);
	    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
