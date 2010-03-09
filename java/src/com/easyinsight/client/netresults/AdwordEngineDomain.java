/**
 * AdwordEngineDomain.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class AdwordEngineDomain  implements java.io.Serializable {
    /* The AdwordEngineDomain Id. */
    private java.lang.Integer adword_engine_domain_id;

    /* The AdwordEngineDomain. */
    private java.lang.String adword_engine_domain;

    public AdwordEngineDomain() {
    }

    public AdwordEngineDomain(
           java.lang.Integer adword_engine_domain_id,
           java.lang.String adword_engine_domain) {
           this.adword_engine_domain_id = adword_engine_domain_id;
           this.adword_engine_domain = adword_engine_domain;
    }


    /**
     * Gets the adword_engine_domain_id value for this AdwordEngineDomain.
     * 
     * @return adword_engine_domain_id   * The AdwordEngineDomain Id.
     */
    public java.lang.Integer getAdword_engine_domain_id() {
        return adword_engine_domain_id;
    }


    /**
     * Sets the adword_engine_domain_id value for this AdwordEngineDomain.
     * 
     * @param adword_engine_domain_id   * The AdwordEngineDomain Id.
     */
    public void setAdword_engine_domain_id(java.lang.Integer adword_engine_domain_id) {
        this.adword_engine_domain_id = adword_engine_domain_id;
    }


    /**
     * Gets the adword_engine_domain value for this AdwordEngineDomain.
     * 
     * @return adword_engine_domain   * The AdwordEngineDomain.
     */
    public java.lang.String getAdword_engine_domain() {
        return adword_engine_domain;
    }


    /**
     * Sets the adword_engine_domain value for this AdwordEngineDomain.
     * 
     * @param adword_engine_domain   * The AdwordEngineDomain.
     */
    public void setAdword_engine_domain(java.lang.String adword_engine_domain) {
        this.adword_engine_domain = adword_engine_domain;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdwordEngineDomain)) return false;
        AdwordEngineDomain other = (AdwordEngineDomain) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.adword_engine_domain_id==null && other.getAdword_engine_domain_id()==null) || 
             (this.adword_engine_domain_id!=null &&
              this.adword_engine_domain_id.equals(other.getAdword_engine_domain_id()))) &&
            ((this.adword_engine_domain==null && other.getAdword_engine_domain()==null) || 
             (this.adword_engine_domain!=null &&
              this.adword_engine_domain.equals(other.getAdword_engine_domain())));
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
        if (getAdword_engine_domain_id() != null) {
            _hashCode += getAdword_engine_domain_id().hashCode();
        }
        if (getAdword_engine_domain() != null) {
            _hashCode += getAdword_engine_domain().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AdwordEngineDomain.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "AdwordEngineDomain"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_engine_domain_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_engine_domain_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_engine_domain");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_engine_domain"));
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
