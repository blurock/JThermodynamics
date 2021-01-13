package thermo;

import thermo.build.BuildDatabase;
import thermo.compute.ComputeThermodynamicsFromNancyString;
import thermo.compute.ComputeThermoFromInChIString;

public class LineCommands {
	
	   public static void main(String[] args) {

	        if (args.length < 1) {
	        	commands();
	        }
	        
	        if(!BuildDatabase.executeCommand(args)) {
	        	if(!ComputeThermodynamicsFromNancyString.executeCommand(args)) {
	        		if(!ComputeThermoFromInChIString.executeCommand(args)) {
	        			commands();
	        		}
	        	}
	        }
	   }
	 private static void commands() {
		 BuildDatabase.commands();
		 ComputeThermodynamicsFromNancyString.commands();
		 ComputeThermoFromInChIString.commands();
	 }

}
