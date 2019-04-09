package api;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import nu.xom.ParsingException;
import thermo.api.CalculateThermodynamicsAPI;
import thermo.data.benson.SetOfBensonThermodynamicBase;

public class CalculateThermodynamicsAPITest {

	@Test
	public void test() {
		try {
			SetOfBensonThermodynamicBase bensonset = CalculateThermodynamicsAPI.calculate("nancy", "ch3/ch3");
			System.out.println(bensonset.toString());
		} catch (IOException | ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
