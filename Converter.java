import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Converter extends JFrame // class extending awt class JFrame
{
	public static void main (String args[]) {
		Converter frame = new Converter();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // setting some defaults
		frame.setVisible(true);	
		frame.setTitle("Converter");
		frame.pack(); // compact view
		frame.setLocationRelativeTo(null);	
	}
	
	private Container container; 								// the entire window
	private JPanel panel = new JPanel();						// main interface
	private JComboBox quantBox = new JComboBox();				// drop down to choose from list of Quantities
	private JLabel fromLabel = new JLabel("From:");		// visuals for user
	private JTextField fromField = new JTextField(10);	// to enter value
	private JComboBox fromBox = new JComboBox();				// drop down containing Units to convert from 
	private JButton convButton = new JButton("Convert");	// Button to do the conversion
	private JLabel toLabel = new JLabel("To:");			// visuals for user
	private JTextField toField = new JTextField(10);	// to show the converted value
	private JComboBox toBox = new JComboBox();					// drop down containing Units to convert to
	
	public Converter()  // each instance of Converter will be initialized in the following frame
	{
		
		panel.setBorder(BorderFactory.createTitledBorder("Unit Conversion"));  // create the main interface
		panel.add(quantBox);
			quantBox.addItem("---select---"); // adding the List of Quantities as a drop down
			quantBox.addItem("Length");
			quantBox.addItem("Weight");
			quantBox.addItem("Time");
			quantBox.addItem("Temperature");
			quantBox.setSelectedItem("---select---");
			quantBox.addActionListener(new convListener());	 // perform actions based on item choosed
		panel.add(fromLabel); // adding the From part of the converter
		panel.add(fromField);
		panel.add(fromBox);
			fromBox.addItem("                   ");
			fromBox.setEditable(false); // initially no choosing from
			fromBox.addActionListener(new convListener());	
		panel.add(convButton); // adding the Convert Button
			convButton.addActionListener(new convListener());
		panel.add(toLabel); // adding the To part of the converter
		panel.add(toField);
			toField.setEditable(false); // initially no typing in textbox
		panel.add(toBox);
			toBox.addItem("                   ");
			toBox.setEditable(false);
			toBox.addActionListener(new convListener());
		
		container = getContentPane(); // gives the user interface
		container.setLayout(new FlowLayout()); // FlowLayout to arrange all components horizontally
		container.add(panel); // adding the main interface to the Container
	
	}
	
	static String allUnits[][] = { {"---select---", "Kilometre", "Metre", "Centimetre", "Millimetre", "Micrometre", "Nanometre", "Mile", "Yard", "Foot", "Inch"}, 
	{"---select---", "Kilogram", "Gram", "Tonne", "Milligram", "Microgram", "US ton", "Pound", "Ounce"}, 
	{"---select---", "Second", "Nanosecond", "Microsecond", "Millisecond", "Minute", "Hour", "Day", "Week", "Month", "Calendar Year", "Decade", "Century"}, 
	{"---select---", "Celsius", "Fahrenheit", "Kelvin"}
	}; // array of all the units
	
	public void giveOptions(int index) // function to load options in the drop down list
	{
		index -= 1;
		String units[] = allUnits[index]; // selecting the Quantity
		fromBox.removeAllItems(); // remove current items from the 2 drop lists
		toBox.removeAllItems();

		for(int i = 0; i<units.length; i++) 
		{
			fromBox.addItem(units[i]); // add new items to the drop lists
			toBox.addItem(units[i]);
		}
	}

	static int conv = 0; // stores current mode of the Converter
	private class convListener implements ActionListener // internal class which implements the ActionListener interface, defined in java.awt.event package
	{
		public void actionPerformed (ActionEvent event) // perform action based on current action
		{
			if(event.getSource() == quantBox) // give user different Unit list based on Quantity selected
			{
				conv = quantBox.getSelectedIndex(); // get user choice (current mode)
				giveOptions(conv); // fill options
				fromField.setText(""); // set both fields initially to empty
				toField.setText("");
			}

			if (event.getSource() == convButton)  // perform the conversion
			{
				try 
				{
					
					int i = fromBox.getSelectedIndex(); // Convert Frojm
					int j = toBox.getSelectedIndex(); // Convert To
					if(j==0) throw new Exception(); // if no unit is selected

					double val = Double.parseDouble(fromField.getText()); // get value from Textfield
					double ans = 0, pow = 1e+4; // ans - converted value, pow - significance level
					if(conv == 1)
						ans = convertLength(val, i, j);  // convert length
					else if(conv == 2)
					{	ans = convertWeight(val, i, j); pow = 1e+6; } // convert weight and change significance level
					else if(conv == 3)
					{	ans = convertTime(val, i, j);	pow = 1e+8; } // convert time and change significance level
					else
						ans = convertTemp(val, i, j); // convert temperature
						
					if(Math.abs(ans)<1/pow) 						// maintaining a level of significance for visuals
						ans = (double) Math.round(ans*1e16) / 1e16;
					else if((Math.abs(ans)*pow)%10 < 10)
						ans = (double) Math.round(ans*pow*100)/(pow*100);
					else
						ans = (double) Math.round(ans*pow)/pow;

					toField.setText("" + ans); // print answer to TextField
					
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Select Units", "ERROR", JOptionPane.WARNING_MESSAGE); // show a ERROR message window
				}
			}
		}
	}
	
	public static double convertLength(double value, int from, int to) // function to convert the length from one unit to another
	{
		double val_m = value; // first convert given to Metre, then convert from Metre to the needed
		if(from == 1) val_m = value*1000; // km to m
		if(from == 2) val_m = value; // m to m
		if(from == 3) val_m = value/100; // cm to m
		if(from == 4) val_m = value/1000; // mm to m
		if(from == 5) val_m = value/1e6; // micro m to m
		if(from == 6) val_m = value/1e9; // nm to m
		if(from == 7) val_m = value*1609.34; // miles to m
		if(from == 8) val_m = value/1.094; // yard to m
		if(from == 9) val_m = value/3.281; // feet to m
		if(from == 10) val_m = value/39.37; // inch to m

		if(to == 1) return val_m/1000; // m to km
		if(to == 2) return val_m; // m to m
		if(to == 3) return val_m*100; // m to cm
		if(to == 4) return val_m*1000; // m to mm
		if(to == 5) return val_m*1e6; // m to micro m
		if(to == 6) return val_m*1e9; // m to nm
		if(to == 7) return val_m/1609.34; // m to miles
		if(to == 8) return val_m*1.094; // m to yard
		if(to == 9) return val_m*3.281; // m to feet
		if(to == 10) return val_m*39.37; // m to inch

		return val_m; // default case
	}

	public static double convertWeight(double value, int from, int to) // function to convert the weight from one unit to another
	{
		double val_m = value; // first convert given to Kilogram, then convert Kilogram to needed
		if(from == 1) val_m = value; // kg to kg
		if(from == 2) val_m = value/1000; // g to kg
		if(from == 3) val_m = value*1000; // tonne to kg
		if(from == 4) val_m = value/1e6; // mg to kg
		if(from == 5) val_m = value/1e9; // micro g to kg
		if(from == 6) val_m = value*907.185; // US ton to kg
		if(from == 7) val_m = value/2.205; // Pound to kg
		if(from == 8) val_m = value/35.274; // Ounce to kg

		if(to == 1) return val_m; // kg to kg
		if(to == 2) return val_m*1000; // kg to g
		if(to == 3) return val_m/1000; // kg to tonne
		if(to == 4) return val_m*1e6; // kg to mg
		if(to == 5) return val_m*1e9; // kg to micro g
		if(to == 6) return val_m/907.185; // kg to US ton
		if(to == 7) return val_m*2.205; // kg to Pound
		if(to == 8) return val_m*35.274; // kg to Ounce

		return val_m; // default case
	}

	public static double convertTime(double value, int from, int to) // function to convert the time from one unit to another
	{
		double val_m = value; // first convert given to Second, then convert Second to the needed
		if(from == 1) val_m = value; // s to s
		if(from == 2) val_m = value/1e9; // ns to s
		if(from == 3) val_m = value/1e6; // micro s to s
		if(from == 4) val_m = value/1e3; // ms to s
		if(from == 5) val_m = value*60; // minute to s
		if(from == 6) val_m = value*3600; // hour to s
		if(from == 7) val_m = value*86400; // day to s
		if(from == 8) val_m = value*604800; // week to s
		if(from == 9) val_m = value*2.628e6; // month to s
		if(from == 10) val_m = value*3.154e7; // year to s
		if(from == 11) val_m = value*3.154e8; // decade to s
		if(from == 12) val_m = value*3.154e9; // century to s
		
		if(to == 1) return val_m; // s to s
		if(to == 2) return val_m*1e9; // s to ns
		if(to == 3) return val_m*1e6; // s to micro s
		if(to == 4) return val_m*1e3; // s to ms
		if(to == 5) return val_m/60; // s to minute
		if(to == 6) return val_m/3600; // s to hour
		if(to == 7) return val_m/86400; // s to day
		if(to == 8) return val_m/604800; // s to week
		if(to == 9) return val_m/2.628e6; // s to month
		if(to == 10) return val_m/3.154e7; // s to year
		if(to == 11) return val_m/3.154e8; // s to decade
		if(to == 12) return val_m/3.154e9; // s to century
		
		return val_m;
	}

	public static double convertTemp(double value, int from, int to) // function to convert the temperature from one unit to another
	{
		double val_m = value; // first convert given to Celsius, then convert Celsius to needed
		if(from == 1) val_m = value; // C to C
		if(from == 2) val_m = (value-32)*5/9; // F to C
		if(from == 3) val_m = value - 273.15; // K to C

		if(to == 1) return val_m; // C to C
		if(to == 2) return (val_m*9/5) + 32; // C to F
		if(to == 3) return val_m + 273.15; // C to K

		return val_m;
	}
	
}
