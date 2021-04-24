package thermo.compute;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import thermo.LineCommandsParameters;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.SProperties;

public class ThermodynamicOutputFormation {

	public static String printThermodynamics(SetOfBensonThermodynamicBase bensonset, ThermodynamicInformation thermo,
			String outputform, String outdetail, String title, boolean standalone)
			throws ThermodynamicComputeException {
		StringBuffer buf = new StringBuffer();

		if (outputform.equals(LineCommandsParameters.stroutkey)) {
			if (outdetail.equals(LineCommandsParameters.detailedkey)) {
				if(bensonset != null) {
					buf.append(bensonset.toString());
				}
				buf.append(thermo.toString());
			} else {
				buf.append(thermo.toString());
			}

		} else {

			if (outputform.equals(LineCommandsParameters.htmltablekey)) {
				if (standalone) {
					String prefix = printThermodynamicsPrefix(outputform, outdetail, title);
					buf.append(prefix);
				}
				if(bensonset != null) {
				Iterator<BensonThermodynamicBase> bensoniter = bensonset.iterator();
				while (bensoniter.hasNext()) {
					BensonThermodynamicBase benson = bensoniter.next();
					htmlBensonLine(benson, buf, outdetail);
				}
				}
				htmlBensonLine((BensonThermodynamicBase) thermo, buf, outdetail);
				if (standalone) {
					String suffix = printThermodynamicSuffix(outputform);
					buf.append(suffix);
				}
			} else if (outputform.equals(LineCommandsParameters.cvstablekey)) {
				if(bensonset != null) {
					if (standalone) {
						String prefix = printThermodynamicsPrefix(outputform, outdetail, title);
						buf.append(prefix);
					}					
				Iterator<BensonThermodynamicBase> bensoniter = bensonset.iterator();
				while (bensoniter.hasNext()) {
					BensonThermodynamicBase benson = bensoniter.next();
					cvsBensonLine(benson, buf, outdetail);
				}
				}
				cvsBensonLine((BensonThermodynamicBase) thermo, buf, outdetail);
				buf.append("\n");
				if (standalone) {
					String suffix = printThermodynamicSuffix(outputform);
					buf.append(suffix);
				}
			} else if (outputform.equals(LineCommandsParameters.wikioutkey)) {
				if (standalone) {
					String pre = printThermodynamicsPrefix(outputform, outdetail, title);
					buf.append(pre);
				}
				if(bensonset != null) {
				Iterator<BensonThermodynamicBase> bensoniter = bensonset.iterator();
				while (bensoniter.hasNext()) {
					BensonThermodynamicBase benson = bensoniter.next();
					ArrayList<String> line = generateThermoData(outdetail, benson);
					Iterator<String> lineiter = line.iterator();
					buf.append("|-\n");
					while (lineiter.hasNext()) {
						buf.append("| ");
						buf.append(lineiter.next());
						buf.append("\n");
					}
				}
				}
				if (standalone) {
					String suffix = printThermodynamicSuffix(outputform);
					buf.append(suffix);
					buf.append("\n|}\n");
				}

			} else if (outputform.equals(LineCommandsParameters.formattedkey)) {

			} else {
				buf.append("No formatting specified\n");
				buf.append(title + "\n");
				buf.append(bensonset.toString());
				buf.append(thermo.toString());
			}
		}
		return buf.toString();
	}

	private static void cvsBensonLine(BensonThermodynamicBase benson, StringBuffer buf, String outdetail)
			throws ThermodynamicComputeException {
		ArrayList<String> line = generateThermoData(outdetail, benson);
		Iterator<String> lineiter = line.iterator();
		buf.append(lineiter.next());
		while (lineiter.hasNext()) {
			buf.append(",\"");
			buf.append(lineiter.next());
			buf.append("\" ");
		}
		buf.append("\n");
	}

	private static void htmlBensonLine(BensonThermodynamicBase benson, StringBuffer buf, String outdetail)
			throws ThermodynamicComputeException {
		ArrayList<String> line = generateThermoData(outdetail, benson);
		Iterator<String> lineiter = line.iterator();
		buf.append("<tr>\n");
		while (lineiter.hasNext()) {
			buf.append("<td>");
			buf.append(lineiter.next());
			buf.append("</td> ");
		}
		buf.append("</tr>\n");

	}

	public static ArrayList<String> generateThermoHeadings(String outdetail) {
		ArrayList<String> heading = new ArrayList<String>();
		heading.add(SProperties.getProperty("thermo.output.name"));
		heading.add(SProperties.getProperty("thermo.output.reference"));
		if (outdetail.equals(LineCommandsParameters.shortkey)) {
			heading.add(SProperties.getProperty("thermo.output.hf"));
			heading.add(SProperties.getProperty("thermo.output.sf"));
			heading.add(SProperties.getProperty("thermo.output.cp" + 300));
		} else if (outdetail.equals(LineCommandsParameters.summarykey)) {
			heading.add(SProperties.getProperty("thermo.output.hf"));
			heading.add(SProperties.getProperty("thermo.output.sf"));
			heading.add(SProperties.getProperty("thermo.output.cp"));
			// heading.add(SProperties.getProperty("thermo.output.optical"));
			// heading.add(SProperties.getProperty("thermo.output.internal"));
			// heading.add(SProperties.getProperty("thermo.output.external"));
		} else {
			heading.add(SProperties.getProperty("thermo.output.hf"));
			heading.add(SProperties.getProperty("thermo.output.sf"));
			String stdtemps = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
			StringTokenizer tok = new StringTokenizer(stdtemps, ",");
			while (tok.hasMoreElements()) {
				heading.add(SProperties.getProperty("thermo.output.cp") + tok.nextToken());
			}

		}
		return heading;
	}

	public static ArrayList<String> generateThermoData(String outdetail, BensonThermodynamicBase benson)
			throws ThermodynamicComputeException {
		ArrayList<String> line = new ArrayList<String>();
		String formatS = "%6.2f";
		String title = "";
		if (benson.getID() != null) {
			title = benson.getID();
		}
		String reference = "";
		if (benson.getReference() != null) {
			reference = benson.getReference();
		}
		String hf = String.format(formatS, benson.getStandardEnthalpy());
		String sf = String.format(formatS, benson.getStandardEntropy());
		System.out.println(benson.toString());
		line.add(title);
		line.add(reference);
		if (outdetail.equals(LineCommandsParameters.shortkey)) {
			line.add(hf);
			line.add(sf);
			String cp300 = String.format(formatS, benson.getHeatCapacity(300.0));
			line.add(cp300);
		} else if (outdetail.equals(LineCommandsParameters.summarykey)) {
			line.add(hf);
			line.add(sf);
			String cp300 = String.format(formatS, benson.getHeatCapacity(300.0));
			line.add(cp300);
		} else {
			line.add(hf);
			line.add(sf);
			String stdtemps = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
			StringTokenizer tok = new StringTokenizer(stdtemps, ",");
			while (tok.hasMoreElements()) {
				Double tempD = Double.parseDouble(tok.nextToken());
				String cp = String.format(formatS, benson.getHeatCapacity(tempD.doubleValue()));
				line.add(cp);
			}

		}
		return line;

	}

	public static void printToFile(String thermoS, String outfile) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outfile);
		out.println(thermoS);
		out.close();
	}

	public static String printSetThermodynamics(SetOfThermodynamicInformation thermodynamics, String outputform,
			String outdetail, String title) throws ThermodynamicComputeException {
		StringBuffer buf = new StringBuffer();
		Iterator<ThermodynamicInformation> iter = thermodynamics.iterator();
		String pre = printThermodynamicsPrefix(outputform, outdetail, title);
		buf.append(pre);
		while (iter.hasNext()) {
			ThermodynamicInformation benson = iter.next();
			String bensonS = printThermodynamics(null, benson, outputform, outdetail, title, false);
			buf.append(bensonS);
		}
		String suf = printThermodynamicSuffix(outputform);
		buf.append(suf);

		return buf.toString();
	}

	private static String printThermodynamicSuffix(String outputform) {
		StringBuffer buf = new StringBuffer();
		if (outputform.equals(LineCommandsParameters.htmltablekey)) {
			String postfix = "</table>\n" + "</body>\n" + "</html>\n" + "";
			buf.append(postfix);
		} else if (outputform.equals(LineCommandsParameters.wikioutkey)) {
			buf.append("\n|}\n");
		}

		return buf.toString();
	}

	private static String printThermodynamicsPrefix(String outputform, String outdetail, String title) {
		StringBuffer buf = new StringBuffer();
		ArrayList<String> headings = generateThermoHeadings(outdetail);
		Iterator<String> headiter = headings.iterator();
		if (outputform.equals(LineCommandsParameters.htmltablekey)) {
			String prefix = "<html lang=\"en\">\n" + "<head>\n" + "  <meta charset=\"utf-8\">\n" + "\n" + "  <title>"
					+ title + "</title>\n" + "  <meta name=\"description\" content=\"Thermodynamic Calculation\">\n"
					+ "  <meta name=\"Edward S. Blurock\" content=\"JThermodynamics\">\n" + "\n" + "</head>\n" + "\n"
					+ "<body>\n" + "<h1>" + title + "</h1>" + "<table style=\"width:100%\">\n";
			buf.append(prefix);
			buf.append("<tr>\n");
			while (headiter.hasNext()) {
				buf.append("<th>");
				buf.append(headiter.next());
				buf.append("</th> ");
			}
			buf.append("</tr>\n");
		} else if (outputform.equals(LineCommandsParameters.wikioutkey)) {
			buf.append("== " + title + " ==\n");
			buf.append("{| class=\"wikitable\"\n");
			while (headiter.hasNext()) {
				buf.append(" ! ");
				buf.append(headiter.next());
			}

		} else if(outputform.equals(LineCommandsParameters.cvstablekey)) {
			buf.append(headiter.next());
			while (headiter.hasNext()) {
				buf.append(",\"");
				buf.append(headiter.next());
				buf.append("\" ");
			}
			buf.append("\n");
			
		}

		return buf.toString();

	}

}
