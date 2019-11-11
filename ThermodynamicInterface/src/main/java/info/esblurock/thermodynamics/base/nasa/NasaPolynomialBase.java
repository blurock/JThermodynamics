package info.esblurock.thermodynamics.base.nasa;

import java.io.Serializable;

public class NasaPolynomialBase implements Serializable {
	private static final long serialVersionUID = 1L;

	String reference;

    /** The name of the molecule
     */
    public String name;
    /** The array of atom symbol names
     */
    public String atoms[] = new String[4];
    /** The corresponding counts of each atom symbol
     */
    public int atomcnt[] = new int[4];
    /** The phase of the molecule
     * Here it is, by default gas (G)
     */
    public String phase;
    /** The lower temperature of the range
     */
    public double lowerT;
    /** The upper temperature of the range
     */
    public double upperT;
    /** The common temperature between the two NASA polynomials
     */
    public double middleT;
    /** The lower temperature range coefficients
     */
    public double[] lower = new double[7];
    /** The upper temperature range coefficients
     */
    public double[] upper = new double[7];

    public NasaPolynomialBase() {
    	
    }
    
    /** Convert to a NASAPolynomial String
    *
    * @return The 4 line string
    
   @Override
   public String toString() {
       StringBuilder buf = new StringBuilder();
       Formatter f = new Formatter();
       if(name != null)
           buf.append(f.format("%-24s", name));
       else
           buf.append("                        ");

       if (atoms != null) {
           for (int i = 0; i < 4; i++) {
               f = new Formatter();
               if (atoms[i] != null && atoms[i].length() > 0) {
                   buf.append(f.format("%-3s%2d", atoms[i].toLowerCase(), atomcnt[i]));
               } else {
                   buf.append("     ");
               }
           }
       } else {
           buf.append("                    ");
       }
       buf.append("G");
       //f = new Formatter();
       buf.append(f.format("%10.2f%10.2f%10.2f    1\n", lowerT, upperT, middleT));
       //f = new Formatter();
       buf.append(f.format("%+15.8e%+15.8e%+15.8e%+15.8e%+15.8e    2\n",
               upper[0], upper[1], upper[2], upper[3], upper[4]));
       //f = new Formatter();
       buf.append(f.format("%+15.8e%+15.8e%+15.8e%+15.8e%+15.8e    3\n",
               upper[5], upper[6], lower[0], lower[1], lower[2]));
       //f = new Formatter();
       buf.append(f.format("%+15.8e%+15.8e%+15.8e%+15.8e                   4\n",
               lower[3], lower[4], lower[5], lower[6]));
       f.close();
       return buf.toString();
   }
   */
   /** Get the origin of the polynomial
   *
   * @return The name
   */
  public String getThermodynamicType() {
      return reference;
  }
  /** Set the origin of the polynomial
   *
   * @param type The name
   */
  public void setThermodynamicType(String type) {
      reference = type;
  }
  public String getName() {
      return name;
  }
  public void setName(String name) {
      this.name = name;
  }
}
