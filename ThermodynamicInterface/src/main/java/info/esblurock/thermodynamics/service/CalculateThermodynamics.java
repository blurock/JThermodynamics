package info.esblurock.thermodynamics.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.compute.ComputeThermodynamicsFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.benson.CML.CMLSetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.NormailizeStructureFromSmiles;
import thermo.exception.ThermodynamicComputeException;

/**
 * Servlet implementation class CalculateThermodynamics
 */
@WebServlet("/calculate")
public class CalculateThermodynamics extends HttpServlet {
	private static final long serialVersionUID = 1L;

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

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CalculateThermodynamics() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 * @param nancy
	 *            compute thermodynamics with nancy form SMILES
	 * @param inchi
	 *            compute thermodynamics with inchi form
	 * @param smiles
	 *            compute thermodynamics with smiles
	 * @param output
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object nancyO = null;
		Object inchiO = null;
		Object smilesO = null;
		Object outputO = null;
		Object totalO = null;

		if (request != null) {
			nancyO = request.getParameter(nancyParameterS);
			inchiO = request.getParameter(inchiParameterS);
			smilesO = request.getParameter(smilesParameterS);
			outputO = request.getParameter(outputParameterS);
			totalO = request.getParameter(totalParameterS);
		}
		String output = htmlOutputS;
		if (outputO != null) {
			String outputS = (String) outputO;
			if (outputS.equalsIgnoreCase(xmlOutputS)) {
				output = xmlOutputS;
			} else if (outputS.equalsIgnoreCase(htmlOutputS)) {
				output = htmlOutputS;
			} else if (outputS.equalsIgnoreCase(textOutputS)) {
				output = textOutputS;
			}
		}
		boolean totalB = false;
		if (totalO != null) {
			String totalS = (String) totalO;
			totalB = totalS.equalsIgnoreCase(trueS);
		}

		ThermoSQLConnection connection = new ThermoSQLConnection();
		boolean connected = connection.connect();
		boolean formB = nancyO != null | smilesO != null | inchiO != null;
		if (formB) {
			if (connected) {
				ComputeThermodynamicsFromMolecule compute;
				try {
					compute = new ComputeThermodynamicsFromMolecule(connection);
					SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
					ThermodynamicInformation total = null;
					if (nancyO != null) {
						String nancy = (String) nancyO;
						total = compute.computeThermodynamics(nancy, thermodynamics);
					}
					if (inchiO != null) {
						String inchi = (String) inchiO;
						total = compute.computeThermodynamicsFromInChI(inchi, thermodynamics);
					}
					if (smilesO != null) {
						String smiles = (String) smilesO;
						NormailizeStructureFromSmiles normalize = new NormailizeStructureFromSmiles();
						IAtomContainer molecule = normalize.moleculeFromSmiles(smiles);
						total = compute.computeThermodynamics(molecule, thermodynamics);
					}
					BensonThermodynamicBase benson  = (BensonThermodynamicBase) total;
					thermodynamics.add(benson);
					
					if (total != null) {
						if (output.equalsIgnoreCase(textOutputS)) {
							response.setContentType("text/text");
							String thermoS = thermodynamics.toString();
							System.out.println(thermoS);
							if (!totalB) {
								response.getWriter().println(thermoS);
							} else {
								BensonThermodynamicBase b  = (BensonThermodynamicBase) total;
								SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
								set.add(b);
								response.getWriter().println(set.toString());
							}
						} else if (output.equalsIgnoreCase(xmlOutputS)) {
							response.setContentType("text/xml");
							if (!totalB) {
								CMLSetOfBensonThermodynamicBase cmlbenson = new CMLSetOfBensonThermodynamicBase();
								cmlbenson.setStructure(thermodynamics);
								cmlbenson.toCML();
								response.getWriter().print(cmlbenson.toXML());
							} else {
								BensonThermodynamicBase b  = (BensonThermodynamicBase) total;
								SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
								set.add(b);
								System.out.println("total converted to set");
								CMLSetOfBensonThermodynamicBase cmlbensonset = new CMLSetOfBensonThermodynamicBase();
								cmlbensonset.setStructure(set);
								cmlbensonset.toCML();
								response.getWriter().print(cmlbensonset.toXML());
							}
						} else if (output.equalsIgnoreCase(htmlOutputS)) {
							response.setContentType("text/html");
							String header = "<!DOCTYPE html>\n"
									+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\n" + "<head>\n"
									+ "<meta http-equiv=\"content-type\" content=\"application/xhtml+xml; charset=UTF-8\" />\n"
									+ "<title> Thermodynamics: </title>\n" + "</head>\n" + "<body>\n";
							response.getWriter().print(header);
							if(totalB) {
								BensonThermodynamicBase totalbenson = (BensonThermodynamicBase) total;
								String totalhtml = totalbenson.printThermodynamicsAsHTMLTable();
								response.getWriter().print(totalhtml);								
							} else {
								String html = thermodynamics.printThermodynamicsAsHTMLTable();
								response.getWriter().print(html);								
							}
							String footer = "  </body>\n" + "</html>";
							response.getWriter().println(footer);
						}
					}
				} catch (ThermodynamicComputeException e) {
					response.setContentType("text/text");
					response.getWriter().println(e.getMessage());
				}
			} else {
				response.setContentType("text/text");
				response.getWriter().println("Could not connect to database");
			}
		} else {
			response.setContentType("text/text");
			response.getWriter().println("Have to give 'nancy', 'smiles' or 'inchi' ");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
