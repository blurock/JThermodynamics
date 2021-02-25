package molecules;


import org.junit.Test;
import org.openscience.cdk.Atom;

public class AtomNameTest {

	@Test
	public void test() {
		try {
			Atom atm1 = new Atom("X");
		} catch(Exception ex) {
			System.out.println("X not working");
			Atom atm2 = new Atom("R");
			atm2.setSymbol("X");
			System.out.println(atm2);
		}

		try {
			Atom atm2 = new Atom("CO");
		} catch(Exception ex) {
			System.out.println("CO not working");
			Atom atm3 = new Atom("R");
			atm3.setSymbol("CO");
			System.out.println(atm3);
		}

		try {
			Atom atm4 = new Atom("Du");
			System.out.println("Du working");
		} catch(Exception ex) {
			System.out.println("Du not working");
			Atom atm5 = new Atom("R");
			atm5.setSymbol("Du");
			System.out.println(atm5);
		}

	
	}

}
