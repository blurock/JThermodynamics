package thermo.data.structure.structure.vibrational;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.properties.SProperties;

public class CalculateVibrationalCorrectionUtilities {
	static double standardTemperature = 298.0;
	static boolean debug = false;
	static String  referenceRoot = "Correction: ";
	static String typeS = "Correction";
	
	public static BensonThermodynamicBase contribution(String elementname, double matches, double frequency, double symmetry) {
		FrequencyCorrection frequencyCorrection = new FrequencyCorrection();
       	//double factor = matches/symmetry;
    	double factor = matches;
    	String reference = referenceRoot + "Frequency:" + frequency;
    	if(debug) {
    		System.out.println("CalculateVibrationalCorrectionForRadical : " +
    				frequency + " mat=" + matches + " sym= " + symmetry + " fac= " + factor);
    	}
    	double entropyC = frequencyCorrection.correctEntropyInCalories(frequency, standardTemperature)*factor;
    	ArrayList<HeatCapacityTemperaturePair> pairs;
    	BensonThermodynamicBase benson = null;
		try {
			pairs = heatCapacityPairs(reference);
	    	Iterator<HeatCapacityTemperaturePair> capacityiter = pairs.iterator();
	    	while(capacityiter.hasNext()){
	    		HeatCapacityTemperaturePair pair = capacityiter.next();
	    		double fcorrection = frequencyCorrection.correctCpInCalories(frequency, pair.getTemperatureValue());
	    		pair.setHeatCapacityValue(fcorrection*factor);
	    	}
	    	//String correctionS = referenceRoot + " " + count.getElementName() + ": " + frequency + "\t Count=" + matches;
	    	String correctionS = referenceRoot + " " + elementname + ": " + frequency + "\t Count=" + matches;
	    	if(debug) {
	    		System.out.println("CalculateVibrationalCorrectionForRadical : corr=" +
	                correctionS);    	
	    	}
	    	benson = new BensonThermodynamicBase(typeS, pairs, 0.0, entropyC);
	    	benson.setID(correctionS);
	    	benson.setReference("Vibration Correction");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return benson;
	}
	
    private static ArrayList<HeatCapacityTemperaturePair> heatCapacityPairs(String referenceS) throws IOException {
    	ArrayList<HeatCapacityTemperaturePair> pairs = new ArrayList<HeatCapacityTemperaturePair>();
        String temperature = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
        StringTokenizer tok = new StringTokenizer(temperature,",");
        while(tok.hasMoreElements()) {
            String tempS = tok.nextToken();
            Double tempD = Double.valueOf(tempS);
            HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
            pair.setReference(referenceS);
            pair.setTemperatureValue(tempD.doubleValue());
            pairs.add(pair);
        }
        return pairs;
    }


}
