package info.esblurock.thermodynamics;


import java.io.IOException;

import thermo.compute.ComputeThermodynamicsFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.benson.CML.CMLSetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.exception.ThermodynamicComputeException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hello"}
)
public class HelloAppEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	Object nancyO = null;
	if(request != null) {
		nancyO = request.getParameter("nancy");
	}
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    String header = "<!DOCTYPE html>\n" +
    		"<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\n" + 
    		"<head>\n" +
    		"<meta http-equiv=\"content-type\" content=\"application/xhtml+xml; charset=UTF-8\" />\n" +
    		"<title> Thermodynamics: </title>\n" + 
    		"</head>\n" + 
  			"<body>\n";
    response.getWriter().print(header);
    if(nancyO != null) {
    	String nancy = (String) nancyO;
    	ThermoSQLConnection connection = new ThermoSQLConnection();
    	boolean connected = connection.connect();
    	if(connected) {
    	try {
			ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(connection);
			SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
			ThermodynamicInformation total = compute.computeThermodynamics(nancy, thermodynamics);
			System.out.println("Total: \n" + thermodynamics.toString());
			System.out.println("Elements\n" + thermodynamics.toString());
			String html = thermodynamics.printThermodynamicsAsHTMLTable();
			response.getWriter().print(html);

			BensonThermodynamicBase totalbenson = (BensonThermodynamicBase) total;
			String totalhtml = totalbenson.printThermodynamicsAsHTMLTable();
			response.getWriter().print(totalhtml);
			
			String footer = "  </body>\n" + 
					"</html>";
			response.getWriter().println(footer);
			
			
		} catch (ThermodynamicComputeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	} else {
    		System.out.println("HelloApp:  could not connect");
    	}
    	
    	//response.getWriter().print("Nancy: " + nancy + "\n");
    }
  }
}