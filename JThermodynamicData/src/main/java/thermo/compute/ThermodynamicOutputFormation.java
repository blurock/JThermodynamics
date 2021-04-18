package thermo.compute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import thermo.LineCommandsParameters;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.SProperties;

public class ThermodynamicOutputFormation {

	public static String printThermodynamics(SetOfBensonThermodynamicBase bensonset,
			ThermodynamicInformation thermo,
			String outputform, String outdetail) throws ThermodynamicComputeException {
		StringBuffer buf = new StringBuffer();
		
		if(outputform.equals(LineCommandsParameters.stroutkey)) {
			if(outdetail.equals(LineCommandsParameters.detailedkey)) {
				buf.append(bensonset.toString());
				buf.append(thermo.toString());
			} else {
				buf.append(thermo.toString());
			}
			
		} else {
			ArrayList<String> headings= generateThermoHeadings(outdetail);
			Iterator<String> headiter = headings.iterator();
			
		if(outputform.equals(LineCommandsParameters.htmltablekey)) {
			buf.append("<tr>\n");
			while(headiter.hasNext()) {
				buf.append("<th>");
				buf.append(headiter.next());
				buf.append("</th> ");
			}
			buf.append("</tr>\n");
			Iterator<BensonThermodynamicBase> bensoniter = bensonset.iterator();
			while(bensoniter.hasNext()) {
				BensonThermodynamicBase benson = bensoniter.next();
				ArrayList<String> line = generateThermoData(outdetail,benson);
				Iterator<String> lineiter = line.iterator();
				buf.append("<tr>\n");
				while(lineiter.hasNext()) {
					buf.append("<td>");
					buf.append(lineiter.next());
					buf.append("<\td> ");
				}
				buf.append("<\tr>\n");
			}
		} else if(outputform.equals(LineCommandsParameters.cvstablekey)) {
			buf.append(headiter.next());
			while(headiter.hasNext()) {
				buf.append(",\"");
				buf.append(headiter.next());
				buf.append("\" ");
			}
			Iterator<BensonThermodynamicBase> bensoniter = bensonset.iterator();
			while(bensoniter.hasNext()) {
				BensonThermodynamicBase benson = bensoniter.next();
				ArrayList<String> line = generateThermoData(outdetail,benson);
				Iterator<String> lineiter = line.iterator();
				buf.append(lineiter.next());
				while(lineiter.hasNext()) {
					buf.append(",\"");
					buf.append(lineiter.next());
					buf.append("\" ");
				}
				buf.append("\n");
			}
			buf.append("/n");
			
		} else if(outputform.equals(LineCommandsParameters.wikioutkey)) {
			
			buf.append("{| class=\"wikitable\"\n");
			while(headiter.hasNext()) {
				buf.append(" ! ");
				buf.append(headiter.next());
			}
			Iterator<BensonThermodynamicBase> bensoniter = bensonset.iterator();
			while(bensoniter.hasNext()) {
				BensonThermodynamicBase benson = bensoniter.next();
				ArrayList<String> line = generateThermoData(outdetail,benson);
				Iterator<String> lineiter = line.iterator();
				buf.append("|-\n");
				while(lineiter.hasNext()) {
					buf.append(" | ");
					buf.append(lineiter.next());
					buf.append("\n");
				}
				buf.append("\n|}\n");
			}
			
		} else if(outputform.equals(LineCommandsParameters.formattedkey)) {
			
		}
		}
		return buf.toString();
	}
	
	
	public static ArrayList<String> generateThermoHeadings(String outdetail)  {
		ArrayList<String> heading = new ArrayList<String>();
		if(outdetail.equals(LineCommandsParameters.shortkey)) {
			heading.add(SProperties.getProperty("thermo.output.hf"));
			heading.add(SProperties.getProperty("thermo.output.sf"));
			heading.add(SProperties.getProperty("thermo.output.cp" + 300));
		} else if(outdetail.equals(LineCommandsParameters.summarykey)) {
			heading.add(SProperties.getProperty("thermo.output.hf"));
			heading.add(SProperties.getProperty("thermo.output.sf"));
			heading.add(SProperties.getProperty("thermo.output.cp"));
			//heading.add(SProperties.getProperty("thermo.output.optical"));
			//heading.add(SProperties.getProperty("thermo.output.internal"));
			//heading.add(SProperties.getProperty("thermo.output.external"));
		} else {
			heading.add(SProperties.getProperty("thermo.output.hf"));
			heading.add(SProperties.getProperty("thermo.output.sf"));
			String stdtemps = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
			StringTokenizer tok = new StringTokenizer(stdtemps,",");
			while(tok.hasMoreElements()) {
				heading.add(SProperties.getProperty("thermo.output.cp" + tok.nextToken()));				
			}

		}
 		return heading;
	}
	
	public static ArrayList<String> generateThermoData(String outdetail, BensonThermodynamicBase benson) throws ThermodynamicComputeException {
		ArrayList<String> line = new ArrayList<String>();
		String formatS = "%6.2f";
		String hf = String.format(formatS,benson.getStandardEnthalpy());
		String sf = String.format(formatS,benson.getStandardEntropy());

		if(outdetail.equals(LineCommandsParameters.shortkey)) {
			line.add(hf);
			line.add(sf);
			String cp300 = String.format(formatS,benson.getHeatCapacity(300.0));
		} else if(outdetail.equals(LineCommandsParameters.summarykey)) {
			line.add(hf);
			line.add(sf);
			String cp300 = String.format(formatS,benson.getHeatCapacity(300.0));			
		} else {
			String stdtemps = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
			StringTokenizer tok = new StringTokenizer(stdtemps,",");
			while(tok.hasMoreElements()) {
				Double cpD= Double.parseDouble(tok.nextToken());
				String cp = String.format(formatS,benson.getHeatCapacity(cpD.doubleValue()));
				line.add(cp);
			}

		}
 		return line;
		
	}
		
}
