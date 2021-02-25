package info.esblurock.thermodynamics.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;
import info.esblurock.thermodynamics.service.convert.ConvertBensonStructures;
import nu.xom.ParsingException;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.CML.CMLSetOfBensonThermodynamicBase;

public class CalculateThermodynamicsAPI {
	public static String nancyParameterS = "nancy";
	public static String smilesParameterS = "smiles";
	public static String inchiParameterS = "inchi";
	public static String outputParameterS = "output";
	public static String totalParameterS = "total";
	public static String textOutputS = "text";
	public static String htmlOutputS = "html";
	public static String xmlOutputS = "xml";
	public static String trueS = "true";
	public static String falseS = "false";

	public static SetOfBensonThermodynamicBaseElements calculate(String type, String parameter)
			throws IOException {

		//String api = "http://jthermodynamics.appspot.com/calculate";
		String api = "http://localhost:8080/calculate";
		
		String input = type;
		String urlS = api + "?output=xml&" + input + "=" + parameter;
		System.out.println(urlS);
		URL url = new URL(urlS);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "text/xml");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		StringBuilder build = new StringBuilder();
		while ((output = br.readLine()) != null) {
			build.append(output);
			build.append("\n");
		}
		String msg = build.toString();
		CMLSetOfBensonThermodynamicBase cmlbensonset = new CMLSetOfBensonThermodynamicBase();
		try {
			cmlbensonset.parse(msg);
		} catch (ParsingException e) {
			new IOException("Calculate Thermodynamics parsing error:\n" + e.toString());
		}
		SetOfBensonThermodynamicBase bensonset = (SetOfBensonThermodynamicBase) cmlbensonset.getStructure();
		SetOfBensonThermodynamicBaseElements elements = ConvertBensonStructures.convert(bensonset);
		return elements;
	}
}
