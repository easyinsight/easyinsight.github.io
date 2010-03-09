/**
 * Referrer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class Referrer  implements java.io.Serializable {
    /* The Referrer Id. */
    private java.lang.Integer referrer_id;

    /* The unique FQDN TLD of the referrer. */
    private java.lang.String referrer;

    public Referrer() {
    }

    public Referrer(
           java.lang.Integer referrer_id,
           java.lang.String referrer) {
           this.referrer_id = referrer_id;
           this.referrer = referrer;
    }


    /**
     * Gets the referrer_id value for this Referrer.
     * 
     * @return referrer_id   * The Referrer Id.
     */
    public java.lang.Integer getReferrer_id() {
        return referrer_id;
    }


    /**
     * Sets the referrer_id value for this Referrer.
     * 
     * @param referrer_id   * The Referrer Id.
     */
    public void setReferrer_id(java.lang.Integer referrer_id) {
        this.referrer_id = referrer_id;
    }


    /**
     * Gets the referrer value for this Referrer.
     * 
     * @return referrer   * The unique FQDN TLD of the referrer.
     */
    public java.lang.String getReferrer() {
        return referrer;
    }


    /**
     * Sets the referrer value for this Referrer.
     * 
     * @param referrer   * The unique FQDN TLD of the referrer.
     */
    public void setReferrer(java.lang.String referrer) {
        this.referrer = referrer;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Referrer)) return false;
        Referrer other = (Referrer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.referrer_id==null && other.getReferrer_id()==null) || 
             (this.referrer_id!=null &&
              this.referrer_id.equals(other.getReferrer_id()))) &&
            ((this.referrer==null && other.getReferrer()==null) || 
             (this.referrer!=null &&
              this.referrer.equals(other.getReferrer())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getReferrer_id() != null) {
            _hashCode += getReferrer_id().hashCode();
        }
        if (getReferrer() != null) {
            _hashCode += getReferrer().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Referrer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Referrer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referrer_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referrer_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referrer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referrer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
