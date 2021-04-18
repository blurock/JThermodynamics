package thermo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import thermo.properties.SProperties;

public class LineCommandsParameters {

	public static String thermoKeyword = SProperties.getProperty("thermo.parameter.thermo");
	public static String thermosetKeyword = SProperties.getProperty("thermo.parameter.thermoset");
	public static String thermocompareKeyword = SProperties.getProperty("thermo.parameter.thermocompare");
	public static String[] commands = { thermoKeyword, thermosetKeyword, thermocompareKeyword };

	// Computational Methods
	public static String thergaskey = SProperties.getProperty("thermo.parameter.thergas");
	public static String thermkey = SProperties.getProperty("thermo.parameter.therm");;
	public static String bensonkey = SProperties.getProperty("thermo.parameter.BensonRadical");
	public static String[] methods = { thergaskey, thermkey, bensonkey };

	// String input formats
	public static String nancykey = SProperties.getProperty("thermo.parameter.nancy");
	public static String iupackey = SProperties.getProperty("thermo.parameter.iupac");
	public static String smileskey = SProperties.getProperty("thermo.parameter.smiles");
	public static String[] molformats = { nancykey, iupackey, smileskey };

	// String output formats
	public static String stroutkey = SProperties.getProperty("thermo.parameter.stringout");
	public static String htmltablekey = SProperties.getProperty("thermo.parameter.htmlout");
	public static String cvstablekey = SProperties.getProperty("thermo.parameter.cvsout");
	public static String wikioutkey = SProperties.getProperty("thermo.parameter.wikiout");
	public static String formattedkey = SProperties.getProperty("thermo.parameter.formatout");
	public static String[] outformat = { stroutkey, htmltablekey, cvstablekey, wikioutkey, formattedkey };

	// String output detail
	public static String totalkey = SProperties.getProperty("thermo.parameter.total");
	public static String detailedkey = SProperties.getProperty("thermo.parameter.detailed");
	public static String summarykey = SProperties.getProperty("thermo.parameter.summary");
	public static String shortkey = SProperties.getProperty("thermo.parameter.short");
	public static String[] outdetail = { totalkey, detailedkey, summarykey, shortkey };

	// Thermodynamic formats
	public static String nasa = SProperties.getProperty("thermo.parameter.nasa");
	public static String benson = SProperties.getProperty("thermo.parameter.benson");
	public static String[] thermoformats = { nasa, benson };

	// Parameter names
	public static String calclasskey = SProperties.getProperty("thermo.parameter.calclass");
	public static String outformatkey = SProperties.getProperty("thermo.parameter.outformat");
	public static String outdetailkey = SProperties.getProperty("thermo.parameter.outdetail");
	public static String outfilekey = SProperties.getProperty("thermo.parameter.outfile");
	public static String methodkey = SProperties.getProperty("thermo.parameter.method");
	public static String molformkey = SProperties.getProperty("thermo.parameter.molform");
	public static String moldescrkey = SProperties.getProperty("thermo.parameter.moldescr");
	public static String molnamekey = SProperties.getProperty("thermo.parameter.molname");
	public static String compfilekey = SProperties.getProperty("thermo.parameter.compfile");
	public static String thermoformatkey = SProperties.getProperty("thermo.parameter.compformat");
	public static String parameternames = "ParameterNames";

	public static String[] parameters = { calclasskey, outformatkey, outdetailkey, outdetailkey, methodkey, molformkey,
			moldescrkey, molnamekey, compfilekey, thermoformatkey };

	public static HashMap<String, ArrayList<String>> parametersets;

	public static void initialize() {
		parametersets.put(calclasskey, new ArrayList<String>(Arrays.asList(commands)));
		parametersets.put(outformatkey, new ArrayList<String>(Arrays.asList(outformat)));
		parametersets.put(outdetailkey, new ArrayList<String>(Arrays.asList(outdetail)));
		parametersets.put(methodkey, new ArrayList<String>(Arrays.asList(methods)));
		parametersets.put(molformkey, new ArrayList<String>(Arrays.asList(molformats)));
		parametersets.put(thermoformatkey, new ArrayList<String>(Arrays.asList(thermoformats)));
		parametersets.put(parameternames, new ArrayList<String>(Arrays.asList(parameters)));
	}

	public static HashMap<String, String> defaultvalues;

	public static void defaults() {
		defaultvalues.put(calclasskey, thermoKeyword);
		defaultvalues.put(methodkey, thergaskey);
		defaultvalues.put(moldescrkey, "ch3/ch2/ch2/ch2(.)");
		defaultvalues.put(outformatkey, stroutkey);
		defaultvalues.put(outdetailkey, detailedkey);
		defaultvalues.put(molformkey, nancykey);
		defaultvalues.put(compfilekey, null);
		defaultvalues.put(thermoformatkey, benson);
		defaultvalues.put(molnamekey, "1-butyl radical");
	}

	public static String listOfValidParameters(String parameter) {
		StringBuffer buf = new StringBuffer();
		ArrayList<String> params = parametersets.get(parameter);
		if (params != null) {
			Iterator<String> iter = params.iterator();
			while (iter.hasNext()) {
				String param = iter.next();
				buf.append(" " + param);
			}
		}
		return buf.toString();
	}

	public static String returnStandardizedParameter(String type, String parameter) {
		String standard = null;
		String typeU = type.toUpperCase();
		ArrayList<String> params = parametersets.get(type);
		if (params != null) {
			Iterator<String> iter = params.iterator();
			while (standard == null && iter.hasNext()) {
				String param = iter.next();
				if (typeU.equals(param.toUpperCase())) {
					standard = param;
				}
			}
		}
		return standard;
	}
	
	/** This returns a copy of the default attribute value pairs
	 * @return a copy of the default attribute value pairs
	 */
	public static Map<String,String> initialParameters() {
		Map<String,String> map = new HashMap<String,String>();
		Set<String> keys = map.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String keyname = iter.next();
			map.put(keyname, defaultvalues.get(keyname));
		}
		return map;
	}
	
	/** The list from the line command of required parameter values
	 * 
	 * @param args The line command arguments
	 * @return A list of argument with no equal sign
	 * 
	 * The argument list can be of two forms:
	 * - A simple string argument
	 * - A attribute value pair (delimited by an '=')
	 */
	public static List<String> requiredParameterInOrder(String args[]) {
		List<String> required = new ArrayList<String>();
		for(int i=0;i<args.length;i++) {
			String parampair = args[i];
			int index = parampair.indexOf("=");
			if(index <= 0) {
				required.add(parampair);
			}
		}
		return required;
	}
		
	/** Full list of attribute value pairs with args inserted
	 * 
	 * @param args The line command arguments
	 * @return The full list of attribute value pairs (if not in the arguments, the default value is inserted)
	 */
	public static Map<String,String> parameterSetFromArguments(String args[]) {
		Map<String,String> paramset = initialParameters();
		for(int i=0;i<args.length;i++) {
			String parampair = args[i];
			int index = parampair.indexOf("=");
			if(index > 0) {
				String param = parampair.substring(0,index).trim();
				String value = parampair.substring(index+1).trim();
				String standard = returnStandardizedParameter(parameternames,param);
				paramset.put(standard,value);
			}
		}
		return paramset;
	}

}
