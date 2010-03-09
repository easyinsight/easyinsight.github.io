/**
 * SearchEngine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class SearchEngine  implements java.io.Serializable {
    /* The Search Engine Id. */
    private int search_engine_id;

    /* The Search Engine name. */
    private java.lang.String search_engine;

    /* The format of the Search Term. */
    private java.lang.String search_format;

    /* Whether or not the url has to be encoded before being used
     * to link to the search results. */
    private boolean url_encode;

    public SearchEngine() {
    }

    public SearchEngine(
           int search_engine_id,
           java.lang.String search_engine,
           java.lang.String search_format,
           boolean url_encode) {
           this.search_engine_id = search_engine_id;
           this.search_engine = search_engine;
           this.search_format = search_format;
           this.url_encode = url_encode;
    }


    /**
     * Gets the search_engine_id value for this SearchEngine.
     * 
     * @return search_engine_id   * The Search Engine Id.
     */
    public int getSearch_engine_id() {
        return search_engine_id;
    }


    /**
     * Sets the search_engine_id value for this SearchEngine.
     * 
     * @param search_engine_id   * The Search Engine Id.
     */
    public void setSearch_engine_id(int search_engine_id) {
        this.search_engine_id = search_engine_id;
    }


    /**
     * Gets the search_engine value for this SearchEngine.
     * 
     * @return search_engine   * The Search Engine name.
     */
    public java.lang.String getSearch_engine() {
        return search_engine;
    }


    /**
     * Sets the search_engine value for this SearchEngine.
     * 
     * @param search_engine   * The Search Engine name.
     */
    public void setSearch_engine(java.lang.String search_engine) {
        this.search_engine = search_engine;
    }


    /**
     * Gets the search_format value for this SearchEngine.
     * 
     * @return search_format   * The format of the Search Term.
     */
    public java.lang.String getSearch_format() {
        return search_format;
    }


    /**
     * Sets the search_format value for this SearchEngine.
     * 
     * @param search_format   * The format of the Search Term.
     */
    public void setSearch_format(java.lang.String search_format) {
        this.search_format = search_format;
    }


    /**
     * Gets the url_encode value for this SearchEngine.
     * 
     * @return url_encode   * Whether or not the url has to be encoded before being used
     * to link to the search results.
     */
    public boolean isUrl_encode() {
        return url_encode;
    }


    /**
     * Sets the url_encode value for this SearchEngine.
     * 
     * @param url_encode   * Whether or not the url has to be encoded before being used
     * to link to the search results.
     */
    public void setUrl_encode(boolean url_encode) {
        this.url_encode = url_encode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchEngine)) return false;
        SearchEngine other = (SearchEngine) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.search_engine_id == other.getSearch_engine_id() &&
            ((this.search_engine==null && other.getSearch_engine()==null) || 
             (this.search_engine!=null &&
              this.search_engine.equals(other.getSearch_engine()))) &&
            ((this.search_format==null && other.getSearch_format()==null) || 
             (this.search_format!=null &&
              this.search_format.equals(other.getSearch_format()))) &&
            this.url_encode == other.isUrl_encode();
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
        _hashCode += getSearch_engine_id();
        if (getSearch_engine() != null) {
            _hashCode += getSearch_engine().hashCode();
        }
        if (getSearch_format() != null) {
            _hashCode += getSearch_format().hashCode();
        }
        _hashCode += (isUrl_encode() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchEngine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "SearchEngine"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_format");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url_encode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url_encode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
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
