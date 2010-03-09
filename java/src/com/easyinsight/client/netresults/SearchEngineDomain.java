/**
 * SearchEngineDomain.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class SearchEngineDomain  implements java.io.Serializable {
    /* The SearchEngine Domain Id. */
    private int search_engine_domain_id;

    /* The SearchEngineDomain */
    private java.lang.String search_engine_domain;

    /* The SearchEngine Id. */
    private int search_engine_id;

    /* The regular expression that must be applied to incoming referrers
     * to parse out the SearchTerm. */
    private int search_engine_regex_id;

    public SearchEngineDomain() {
    }

    public SearchEngineDomain(
           int search_engine_domain_id,
           java.lang.String search_engine_domain,
           int search_engine_id,
           int search_engine_regex_id) {
           this.search_engine_domain_id = search_engine_domain_id;
           this.search_engine_domain = search_engine_domain;
           this.search_engine_id = search_engine_id;
           this.search_engine_regex_id = search_engine_regex_id;
    }


    /**
     * Gets the search_engine_domain_id value for this SearchEngineDomain.
     * 
     * @return search_engine_domain_id   * The SearchEngine Domain Id.
     */
    public int getSearch_engine_domain_id() {
        return search_engine_domain_id;
    }


    /**
     * Sets the search_engine_domain_id value for this SearchEngineDomain.
     * 
     * @param search_engine_domain_id   * The SearchEngine Domain Id.
     */
    public void setSearch_engine_domain_id(int search_engine_domain_id) {
        this.search_engine_domain_id = search_engine_domain_id;
    }


    /**
     * Gets the search_engine_domain value for this SearchEngineDomain.
     * 
     * @return search_engine_domain   * The SearchEngineDomain
     */
    public java.lang.String getSearch_engine_domain() {
        return search_engine_domain;
    }


    /**
     * Sets the search_engine_domain value for this SearchEngineDomain.
     * 
     * @param search_engine_domain   * The SearchEngineDomain
     */
    public void setSearch_engine_domain(java.lang.String search_engine_domain) {
        this.search_engine_domain = search_engine_domain;
    }


    /**
     * Gets the search_engine_id value for this SearchEngineDomain.
     * 
     * @return search_engine_id   * The SearchEngine Id.
     */
    public int getSearch_engine_id() {
        return search_engine_id;
    }


    /**
     * Sets the search_engine_id value for this SearchEngineDomain.
     * 
     * @param search_engine_id   * The SearchEngine Id.
     */
    public void setSearch_engine_id(int search_engine_id) {
        this.search_engine_id = search_engine_id;
    }


    /**
     * Gets the search_engine_regex_id value for this SearchEngineDomain.
     * 
     * @return search_engine_regex_id   * The regular expression that must be applied to incoming referrers
     * to parse out the SearchTerm.
     */
    public int getSearch_engine_regex_id() {
        return search_engine_regex_id;
    }


    /**
     * Sets the search_engine_regex_id value for this SearchEngineDomain.
     * 
     * @param search_engine_regex_id   * The regular expression that must be applied to incoming referrers
     * to parse out the SearchTerm.
     */
    public void setSearch_engine_regex_id(int search_engine_regex_id) {
        this.search_engine_regex_id = search_engine_regex_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchEngineDomain)) return false;
        SearchEngineDomain other = (SearchEngineDomain) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.search_engine_domain_id == other.getSearch_engine_domain_id() &&
            ((this.search_engine_domain==null && other.getSearch_engine_domain()==null) || 
             (this.search_engine_domain!=null &&
              this.search_engine_domain.equals(other.getSearch_engine_domain()))) &&
            this.search_engine_id == other.getSearch_engine_id() &&
            this.search_engine_regex_id == other.getSearch_engine_regex_id();
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
        _hashCode += getSearch_engine_domain_id();
        if (getSearch_engine_domain() != null) {
            _hashCode += getSearch_engine_domain().hashCode();
        }
        _hashCode += getSearch_engine_id();
        _hashCode += getSearch_engine_regex_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchEngineDomain.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "SearchEngineDomain"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_domain_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_domain_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_domain");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_domain"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_regex_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_regex_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
