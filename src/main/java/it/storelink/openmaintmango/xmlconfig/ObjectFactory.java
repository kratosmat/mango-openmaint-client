//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.11.01 at 01:39:48 AM CET 
//


package it.storelink.openmaintmango.xmlconfig;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.storelink.openmaintmango.xmlconfig package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Sensori_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "sensori");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.storelink.openmaintmango.xmlconfig
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SensoriType }
     * 
     */
    public SensoriType createSensoriType() {
        return new SensoriType();
    }

    /**
     * Create an instance of {@link SensoreType }
     * 
     */
    public SensoreType createSensoreType() {
        return new SensoreType();
    }

    /**
     * Create an instance of {@link OpemaintType }
     * 
     */
    public OpemaintType createOpemaintType() {
        return new OpemaintType();
    }

    /**
     * Create an instance of {@link MangoType }
     * 
     */
    public MangoType createMangoType() {
        return new MangoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SensoriType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2001/XMLSchema", name = "sensori")
    public JAXBElement<SensoriType> createSensori(SensoriType value) {
        return new JAXBElement<SensoriType>(_Sensori_QNAME, SensoriType.class, null, value);
    }

}
