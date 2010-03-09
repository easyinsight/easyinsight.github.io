/**
 * Search.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class Search  implements java.io.Serializable {
    /* The Id of the Search. */
    private int search_id;

    /* The SearchEngineDomain Id. */
    private int search_engine_domain_id;

    /* The SearchEngine Id. */
    private int search_engine_id;

    /* The Visit Id. */
    private int visit_id;

    /* The Date/Time of the Search. */
    private java.util.Calendar date;

    /* The Search Term. */
    private java.lang.String search_term;

    private com.easyinsight.client.netresults.SearchEngine searchEngine;

    private com.easyinsight.client.netresults.SearchEngineDomain searchEngineDomain;

    private com.easyinsight.client.netresults.Adword adword;

    public Search() {
    }

    public Search(
           int search_id,
           int search_engine_domain_id,
           int search_engine_id,
           int visit_id,
           java.util.Calendar date,
           java.lang.String search_term,
           com.easyinsight.client.netresults.SearchEngine searchEngine,
           com.easyinsight.client.netresults.SearchEngineDomain searchEngineDomain,
           com.easyinsight.client.netresults.Adword adword) {
           this.search_id = search_id;
           this.search_engine_domain_id = search_engine_domain_id;
           this.search_engine_id = search_engine_id;
           this.visit_id = visit_id;
           this.date = date;
           this.search_term = search_term;
           this.searchEngine = searchEngine;
           this.searchEngineDomain = searchEngineDomain;
           this.adword = adword;
    }


    /**
     * Gets the search_id value for this Search.
     * 
     * @return search_id   * The Id of the Search.
     */
    public int getSearch_id() {
        return search_id;
    }


    /**
     * Sets the search_id value for this Search.
     * 
     * @param search_id   * The Id of the Search.
     */
    public void setSearch_id(int search_id) {
        this.search_id = search_id;
    }


    /**
     * Gets the search_engine_domain_id value for this Search.
     * 
     * @return search_engine_domain_id   * The SearchEngineDomain Id.
     */
    public int getSearch_engine_domain_id() {
        return search_engine_domain_id;
    }


    /**
     * Sets the search_engine_domain_id value for this Search.
     * 
     * @param search_engine_domain_id   * The SearchEngineDomain Id.
     */
    public void setSearch_engine_domain_id(int search_engine_domain_id) {
        this.search_engine_domain_id = search_engine_domain_id;
    }


    /**
     * Gets the search_engine_id value for this Search.
     * 
     * @return search_engine_id   * The SearchEngine Id.
     */
    public int getSearch_engine_id() {
        return search_engine_id;
    }


    /**
     * Sets the search_engine_id value for this Search.
     * 
     * @param search_engine_id   * The SearchEngine Id.
     */
    public void setSearch_engine_id(int search_engine_id) {
        this.search_engine_id = search_engine_id;
    }


    /**
     * Gets the visit_id value for this Search.
     * 
     * @return visit_id   * The Visit Id.
     */
    public int getVisit_id() {
        return visit_id;
    }


    /**
     * Sets the visit_id value for this Search.
     * 
     * @param visit_id   * The Visit Id.
     */
    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }


    /**
     * Gets the date value for this Search.
     * 
     * @return date   * The Date/Time of the Search.
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this Search.
     * 
     * @param date   * The Date/Time of the Search.
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }


    /**
     * Gets the search_term value for this Search.
     * 
     * @return search_term   * The Search Term.
     */
    public java.lang.String getSearch_term() {
        return search_term;
    }


    /**
     * Sets the search_term value for this Search.
     * 
     * @param search_term   * The Search Term.
     */
    public void setSearch_term(java.lang.String search_term) {
        this.search_term = search_term;
    }


    /**
     * Gets the searchEngine value for this Search.
     * 
     * @return searchEngine
     */
    public com.easyinsight.client.netresults.SearchEngine getSearchEngine() {
        return searchEngine;
    }


    /**
     * Sets the searchEngine value for this Search.
     * 
     * @param searchEngine
     */
    public void setSearchEngine(com.easyinsight.client.netresults.SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }


    /**
     * Gets the searchEngineDomain value for this Search.
     * 
     * @return searchEngineDomain
     */
    public com.easyinsight.client.netresults.SearchEngineDomain getSearchEngineDomain() {
        return searchEngineDomain;
    }


    /**
     * Sets the searchEngineDomain value for this Search.
     * 
     * @param searchEngineDomain
     */
    public void setSearchEngineDomain(com.easyinsight.client.netresults.SearchEngineDomain searchEngineDomain) {
        this.searchEngineDomain = searchEngineDomain;
    }


    /**
     * Gets the adword value for this Search.
     * 
     * @return adword
     */
    public com.easyinsight.client.netresults.Adword getAdword() {
        return adword;
    }


    /**
     * Sets the adword value for this Search.
     * 
     * @param adword
     */
    public void setAdword(com.easyinsight.client.netresults.Adword adword) {
        this.adword = adword;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Search)) return false;
        Search other = (Search) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.search_id == other.getSearch_id() &&
            this.search_engine_domain_id == other.getSearch_engine_domain_id() &&
            this.search_engine_id == other.getSearch_engine_id() &&
            this.visit_id == other.getVisit_id() &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.search_term==null && other.getSearch_term()==null) || 
             (this.search_term!=null &&
              this.search_term.equals(other.getSearch_term()))) &&
            ((this.searchEngine==null && other.getSearchEngine()==null) || 
             (this.searchEngine!=null &&
              this.searchEngine.equals(other.getSearchEngine()))) &&
            ((this.searchEngineDomain==null && other.getSearchEngineDomain()==null) || 
             (this.searchEngineDomain!=null &&
              this.searchEngineDomain.equals(other.getSearchEngineDomain()))) &&
            ((this.adword==null && other.getAdword()==null) || 
             (this.adword!=null &&
              this.adword.equals(other.getAdword())));
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
        _hashCode += getSearch_id();
        _hashCode += getSearch_engine_domain_id();
        _hashCode += getSearch_engine_id();
        _hashCode += getVisit_id();
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getSearch_term() != null) {
            _hashCode += getSearch_term().hashCode();
        }
        if (getSearchEngine() != null) {
            _hashCode += getSearchEngine().hashCode();
        }
        if (getSearchEngineDomain() != null) {
            _hashCode += getSearchEngineDomain().hashCode();
        }
        if (getAdword() != null) {
            _hashCode += getAdword().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Search.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Search"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_domain_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_domain_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_engine_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_engine_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visit_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visit_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_term");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_term"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchEngine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SearchEngine"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "SearchEngine"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchEngineDomain");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SearchEngineDomain"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "SearchEngineDomain"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Adword"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Adword"));
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
