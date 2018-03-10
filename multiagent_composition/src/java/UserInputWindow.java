import javax.swing.*;

import jm.music.data.Note;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class UserInputWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> parts;
	private JComboBox<Integer> notes;
	private JComboBox<Double> durations;
	private static int startX=0;
	private static int startY=0;

	public UserInputWindow() {
	    setTitle("User Input Window");
	    setSize(475, 110);
	    setLayout(null);
	    
	    parts = new JComboBox<String>();
	    notes = new JComboBox<Integer>();
	    durations = new JComboBox<Double>();
	    notes.setEditable(true);
	    
	    parts.setBounds(startX+25, startY+25, 100, 25);
	    notes.setBounds(startX+125, startY+25, 100, 25);
	    durations.setBounds(startX+225, startY+25, 100, 25);
	    
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
	    
	    for(int i=Parameters.BASS_LOWER; i<=Parameters.BASS_UPPER; i++) {
	    	notes.addItem(i);
	    }
	    
	    parts.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String newPart = (String) e.getItemSelectable().getSelectedObjects()[0];
				int lower, upper;
				if(newPart.equals("Bass")) {
					lower = Parameters.BASS_LOWER;
					upper = Parameters.BASS_UPPER;
				}
				else if(newPart.equals("Tenor")) {
					lower = Parameters.TENOR_LOWER;
					upper = Parameters.TENOR_UPPER;
				}
				else if(newPart.equals("Alto")) {
					lower = Parameters.ALTO_LOWER;
					upper = Parameters.ALTO_UPPER;
				}
				else if(newPart.equals("Soprano")) {
					lower = Parameters.SOPRANO_LOWER;
					upper = Parameters.SOPRANO_UPPER;
				}
				else {
					lower = Parameters.BASS_LOWER;
					upper = Parameters.BASS_UPPER;					
				}
				notes.removeAllItems();
				for(int i=lower; i<=upper; i++) {
			    	notes.addItem(i);
			    }
			}
	    });
	   
	    Button b = new Button();
	    b.setBounds(startX+325, startY+20, 125, 35);
	    b.setLabel("Place Note");
	    b.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent event) {
	     	    Note n = new Note();
	     	    n.setPitch((Integer) notes.getSelectedItem());
	     	    n.setRhythmValue((Double) durations.getSelectedItem(), true);
	     	    Model.phrases[parts.getSelectedIndex()].addNote(n);
	     	    if(Parameters.GUI) Model.refreshView();
		    }
	    });

	    add(parts);
	    add(notes);
	    add(durations);
	    add(b);
	    setLocation(0, 550);
	    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
