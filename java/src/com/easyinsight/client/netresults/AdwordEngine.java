/**
 * AdwordEngine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class AdwordEngine  implements java.io.Serializable {
    /* The AdwordEngine Id. */
    private java.lang.Integer adword_engine_id;

    /* The AdwordEngine. */
    private java.lang.String adword_engine;

    /* The AdwordEngine Search format. */
    private java.lang.String adword_search_format;

    /* Whether or not the url has to be encoded before being used
     * to link to the search results. */
    private java.lang.Boolean url_encode;

    public AdwordEngine() {
    }

    public AdwordEngine(
           java.lang.Integer adword_engine_id,
           java.lang.String adword_engine,
           java.lang.String adword_search_format,
           java.lang.Boolean url_encode) {
           this.adword_engine_id = adword_engine_id;
           this.adword_engine = adword_engine;
           this.adword_search_format = adword_search_format;
           this.url_encode = url_encode;
    }


    /**
     * Gets the adword_engine_id value for this AdwordEngine.
     * 
     * @return adword_engine_id   * The AdwordEngine Id.
     */
    public java.lang.Integer getAdword_engine_id() {
        return adword_engine_id;
    }


    /**
     * Sets the adword_engine_id value for this AdwordEngine.
     * 
     * @param adword_engine_id   * The AdwordEngine Id.
     */
    public void setAdword_engine_id(java.lang.Integer adword_engine_id) {
        this.adword_engine_id = adword_engine_id;
    }


    /**
     * Gets the adword_engine value for this AdwordEngine.
     * 
     * @return adword_engine   * The AdwordEngine.
     */
    public java.lang.String getAdword_engine() {
        return adword_engine;
    }


    /**
     * Sets the adword_engine value for this AdwordEngine.
     * 
     * @param adword_engine   * The AdwordEngine.
     */
    public void setAdword_engine(java.lang.String adword_engine) {
        this.adword_engine = adword_engine;
    }


    /**
     * Gets the adword_search_format value for this AdwordEngine.
     * 
     * @return adword_search_format   * The AdwordEngine Search format.
     */
    public java.lang.String getAdword_search_format() {
        return adword_search_format;
    }


    /**
     * Sets the adword_search_format value for this AdwordEngine.
     * 
     * @param adword_search_format   * The AdwordEngine Search format.
     */
    public void setAdword_search_format(java.lang.String adword_search_format) {
        this.adword_search_format = adword_search_format;
    }


    /**
     * Gets the url_encode value for this AdwordEngine.
     * 
     * @return url_encode   * Whether or not the url has to be encoded before being used
     * to link to the search results.
     */
    public java.lang.Boolean getUrl_encode() {
        return url_encode;
    }


    /**
     * Sets the url_encode value for this AdwordEngine.
     * 
     * @param url_encode   * Whether or not the url has to be encoded before being used
     * to link to the search results.
     */
    public void setUrl_encode(java.lang.Boolean url_encode) {
        this.url_encode = url_encode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdwordEngine)) return false;
        AdwordEngine other = (AdwordEngine) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.adword_engine_id==null && other.getAdword_engine_id()==null) || 
             (this.adword_engine_id!=null &&
              this.adword_engine_id.equals(other.getAdword_engine_id()))) &&
            ((this.adword_engine==null && other.getAdword_engine()==null) || 
             (this.adword_engine!=null &&
              this.adword_engine.equals(other.getAdword_engine()))) &&
            ((this.adword_search_format==null && other.getAdword_search_format()==null) || 
             (this.adword_search_format!=null &&
              this.adword_search_format.equals(other.getAdword_search_format()))) &&
            ((this.url_encode==null && other.getUrl_encode()==null) || 
             (this.url_encode!=null &&
              this.url_encode.equals(other.getUrl_encode())));
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
        if (getAdword_engine_id() != null) {
            _hashCode += getAdword_engine_id().hashCode();
        }
        if (getAdword_engine() != null) {
            _hashCode += getAdword_engine().hashCode();
        }
        if (getAdword_search_format() != null) {
            _hashCode += getAdword_search_format().hashCode();
        }
        if (getUrl_encode() != null) {
            _hashCode += getUrl_encode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AdwordEngine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "AdwordEngine"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_engine_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_engine_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_engine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_engine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_search_format");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_search_format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url_encode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url_encode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
