package info.esblurock.thermodynamics.service;

import java.io.IOException;

import info.esblurock.thermodynamics.async.CalculateThermodynamicsService;
import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;
import info.esblurock.thermodynamics.service.CalculateThermodynamicsAPI;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

@SuppressWarnings("serial")
public class CalculateThermodynamicsServiceImpl extends ServerBase implements CalculateThermodynamicsService {

	public SetOfBensonThermodynamicBaseElements CalculateThermodynamics(String type, String parameter)
			throws IOException {
		System.out.println("CalculateThermodynamic: " + this.getServletContext().getContextPath());
		System.out.println("CalculateThermodynamic: " + this.getServletContext().getServerInfo());
		System.out.println("CalculateThermodynamic: " + this.getServletContext().getServletContextName());
		System.out.println("CalculateThermodynamic: " + this.getServletContext().getVirtualServerName());

		SetOfBensonThermodynamicBaseElements bensonset = CalculateThermodynamicsAPI.calculate(type,parameter);
		return bensonset;
		
	}
	
}
