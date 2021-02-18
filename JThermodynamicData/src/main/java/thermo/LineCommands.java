package thermo;

import thermo.build.BuildDatabase;
import thermo.build.test.CorrectionElementTests;
import thermo.build.test.ExamineCorrectionElements;
import thermo.compute.ComputeThermodynamicsFromNancyString;
import thermo.compute.ComputeThermoFromInChIString;

public class LineCommands {

	public static void main(String[] args) {

		if (args.length < 1) {
			commands();
		}

		if (!BuildDatabase.executeCommand(args)) {
			if (!ComputeThermodynamicsFromNancyString.executeCommand(args)) {
				if (!ComputeThermoFromInChIString.executeCommand(args)) {
					if (!CorrectionElementTests.executeCommand(args)) {
						if(!ExamineCorrectionElements.executeCommand(args)) {
							commands();
						}
					}
				}
			}
		}
	}

	private static void commands() {
		BuildDatabase.commands();
		ComputeThermodynamicsFromNancyString.commands();
		ComputeThermoFromInChIString.commands();
		CorrectionElementTests.commands();
		ExamineCorrectionElements.commands();
	}

}
