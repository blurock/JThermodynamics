package info.esblurock.thermodynamics.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xmlcml.cml.element.CMLPropertyList;
import org.xmlcml.cml.element.CMLScalar;

import thermo.CML.CMLAbstractThermo;
import thermo.DB.SQLStructureFactory;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;

/**
 * Servlet implementation class ManageStructures
 */
@WebServlet("/manage")
public class ManageStructures extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static String commandParameterS = "command";
	public static String structureParameterS = "structure";
	public static String listParameterS = "list";
	public static String outputParameterS = "output";
	public static String parameterParameterS = "parameter";

	public static String namesInputS = "names";
	public static String elementInputS = "element";
	public static String textOutputS = "text";
	public static String htmlOutputS = "html";
	public static String xmlOutputS = "xml";

	private ThermoSQLConnection connect = null;

	private ThermoSQLConnection getConnectionInstance() {
		if (connect == null) {
			connect = new ThermoSQLConnection();
			connect.connect();
		}
		return connect;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageStructures() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object structureO = null;
		Object commandO = null;
		Object outputO = null;
		Object parameterO = null;
		if (request != null) {
			structureO = request.getParameter(structureParameterS);
			commandO = request.getParameter(commandParameterS);
			outputO = request.getParameter(outputParameterS);
			parameterO = request.getParameter(parameterParameterS);
		}
		String output = xmlOutputS;
		if (outputO != null) {
			String outputS = (String) outputO;
			if (outputS.equalsIgnoreCase(xmlOutputS)) {
				output = xmlOutputS;
			} else if (outputS.equalsIgnoreCase(textOutputS)) {
				output = textOutputS;
			} else if (outputS.equalsIgnoreCase(htmlOutputS)) {
				output = htmlOutputS;
			}
		}
		String parameter = null;
		if(parameterO != null) {
			parameter = (String) parameterO;
		}
		if (structureO != null) {
			String structure = (String) structureO;
			SQLStructureFactory factory = SQLStructureFactory.valueOf(structure);
			if (factory != null) {
				SQLStructureThermoAbstractInterface structureinterface = factory.getInterface(getConnectionInstance());
				if (commandO != null) {
					String command = (String) commandO;
					if (command.equalsIgnoreCase(namesInputS)) {
						try {
							System.out.println("Classname: " + factory.getClassName());
							ArrayList<String> names = structureinterface.retrieveDatabaseNames();
							if (output.equalsIgnoreCase(htmlOutputS)) {
								response.setContentType("text/html");
								response.getWriter().append(this.getHeader("names"));
								response.getWriter().println("<ul>");
								for (String name : names) {
									response.getWriter().println("<li> " + name);
								}
								response.getWriter().println("</ul>");
								response.getWriter().append(this.getFooter());

							} else if (output.equalsIgnoreCase(xmlOutputS)) {
								CMLPropertyList prop = new CMLPropertyList();
								for (String name : names) {
									CMLScalar nameS = new CMLScalar();
									nameS.setId("name");
									nameS.setValue(name);
									prop.appendChild(nameS);
								}
								response.setContentType("text/xml");
								response.getWriter().print(prop.toXML());
							} else if (output.equalsIgnoreCase(textOutputS)) {
								response.setContentType("text/text");
								for (String name : names) {
									response.getWriter().println(name);
								}
							}
						} catch (SQLException e) {
							String errormsg = "Cannot Retrieve names for '" + structure + "'\n" + e.getMessage();
							ErrorResponse.setErrorResponse(response, errormsg);
						}
					}
					if (command.equalsIgnoreCase(elementInputS)) {
						if(parameter != null) {
							try {
								HashSet<Object> set = structureinterface.retrieveStructuresFromDatabase(parameter);
								if(output.equalsIgnoreCase(textOutputS)) {
									for(Object obj : set) {
										response.getWriter().println("------------------------------------------------");
										response.getWriter().println(obj.toString());
									}
								} else if(output.equalsIgnoreCase(htmlOutputS)) {
									String header = getHeader(parameter);
									response.getWriter().print(header);
									
									response.getWriter().println("<pre>");
									for(Object obj : set) {
										response.getWriter().println("------------------------------------------------");
										response.getWriter().println(obj.toString());
									}									
									response.getWriter().println("</pre>");
									String footer = getFooter();
									response.getWriter().print(footer);
								} else if(output.equalsIgnoreCase(xmlOutputS)) {
									CMLPropertyList props = new CMLPropertyList();
									CMLAbstractThermo cmlthermo = factory.getCMLAbstract();
									for(Object obj : set) {
										cmlthermo.setStructure(obj);
										cmlthermo.toCML();
										props.appendChild(cmlthermo);
									}
									response.getWriter().print(props.toXML());
								}
							} catch (SQLException e) {
								String errormsg = "Cannot Retrieve names for '" 
										+ structure + "'\n" + e.getMessage();
								ErrorResponse.setErrorResponse(response, errormsg);
							}
						} else {
							ErrorResponse.setErrorResponse(response, "Need to define the element name with '" 
									+ elementInputS + "'");
						}
					}
				}
			} else {
				String errormsg = "Illegal structure: '" + structure + "'";
				ErrorResponse.setErrorResponse(response, errormsg);
			}
		} else {
			ErrorResponse.setErrorResponse(response, "'structure' parameter must be given");
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

	private String getHeader(String title) {
		String header = "<!DOCTYPE html>\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\n" + "<head>\n"
				+ "<meta http-equiv=\"content-type\" content=\"application/xhtml+xml; charset=UTF-8\" />\n" + "<title> "
				+ title + " </title>\n" + "</head>\n" + "<body>\n";
		return header;
	}

	private String getFooter() {
		String footer = "  </body>\n" + "</html>";
		return footer;
	}
}
