//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.11.01 at 01:39:48 AM CET 
//


package it.storelink.openmaintmango.xmlconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sensoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sensoreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mango" type="{http://www.w3.org/2001/XMLSchema}mangoType"/>
 *         &lt;element name="opemaint" type="{http://www.w3.org/2001/XMLSchema}opemaintType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sensoreType", propOrder = {
    "mango",
    "opemaint"
})
public class SensoreType {

    @XmlElement(required = true)
    protected MangoType mango;
    @XmlElement(required = true)
    protected OpemaintType opemaint;

    /**
     * Gets the value of the mango property.
     * 
     * @return
     *     possible object is
     *     {@link MangoType }
     *     
     */
    public MangoType getMango() {
        return mango;
    }

    /**
     * Sets the value of the mango property.
     * 
     * @param value
     *     allowed object is
     *     {@link MangoType }
     *     
     */
    public void setMango(MangoType value) {
        this.mango = value;
    }

    /**
     * Gets the value of the opemaint property.
     * 
     * @return
     *     possible object is
     *     {@link OpemaintType }
     *     
     */
    public OpemaintType getOpemaint() {
        return opemaint;
    }

    /**
     * Sets the value of the opemaint property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpemaintType }
     *     
     */
    public void setOpemaint(OpemaintType value) {
        this.opemaint = value;
    }

}