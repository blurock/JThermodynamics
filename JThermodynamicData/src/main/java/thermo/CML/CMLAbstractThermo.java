/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.CML;

import java.io.IOException;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;


/**
 *
 * @author blurock
 */
public abstract class CMLAbstractThermo extends CMLProperty {
    
    private Object structure;
    
    public CMLAbstractThermo() {
        
    }
    
    public void parse(String data) throws ValidityException, ParsingException, IOException {
       CMLBuilder build = new CMLBuilder();
       Element ele = build.parseString(data);
       CMLElement cml = (CMLElement) ele;
        this.copyChildrenFrom( cml);
        fromCML();
    }
    
    public String restore() {
 
                toCML();
                Document doc = new Document(this);
                String docxml = doc.toXML();
                String thisxml = this.toXML();
                
                return thisxml;
    }

    /**
     * 
     */
    abstract public void toCML();
    abstract public void fromCML();

    public Object getStructure() {
        return structure;
    }
    public void setStructure(Object s) {
        this.structure = s;
    }
}
