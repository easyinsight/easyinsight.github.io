/**
 * Adword.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class Adword  implements java.io.Serializable {
    /* The Adword Id. */
    private java.lang.Integer adword_id;

    /* The Adword Term. */
    private java.lang.String adword_term;

    /* The Adword GCLID. */
    private java.lang.Integer adword_gclid;

    /* The AdwordEngineDomain Id. */
    private java.lang.Integer adword_engine_domain_id;

    /* The AdwordEngine Id. */
    private java.lang.Integer adword_engine_id;

    /* The Search Id. */
    private java.lang.Integer search_id;

    /* The Date/Time of the Adword. */
    private java.util.Calendar date;

    /* The Visit Id of the Adword. */
    private java.lang.Integer visit_id;

    private com.easyinsight.client.netresults.AdwordEngine adwordEngine;

    private com.easyinsight.client.netresults.AdwordEngineDomain adwordEngineDomain;

    public Adword() {
    }

    public Adword(
           java.lang.Integer adword_id,
           java.lang.String adword_term,
           java.lang.Integer adword_gclid,
           java.lang.Integer adword_engine_domain_id,
           java.lang.Integer adword_engine_id,
           java.lang.Integer search_id,
           java.util.Calendar date,
           java.lang.Integer visit_id,
           com.easyinsight.client.netresults.AdwordEngine adwordEngine,
           com.easyinsight.client.netresults.AdwordEngineDomain adwordEngineDomain) {
           this.adword_id = adword_id;
           this.adword_term = adword_term;
           this.adword_gclid = adword_gclid;
           this.adword_engine_domain_id = adword_engine_domain_id;
           this.adword_engine_id = adword_engine_id;
           this.search_id = search_id;
           this.date = date;
           this.visit_id = visit_id;
           this.adwordEngine = adwordEngine;
           this.adwordEngineDomain = adwordEngineDomain;
    }


    /**
     * Gets the adword_id value for this Adword.
     * 
     * @return adword_id   * The Adword Id.
     */
    public java.lang.Integer getAdword_id() {
        return adword_id;
    }


    /**
     * Sets the adword_id value for this Adword.
     * 
     * @param adword_id   * The Adword Id.
     */
    public void setAdword_id(java.lang.Integer adword_id) {
        this.adword_id = adword_id;
    }


    /**
     * Gets the adword_term value for this Adword.
     * 
     * @return adword_term   * The Adword Term.
     */
    public java.lang.String getAdword_term() {
        return adword_term;
    }


    /**
     * Sets the adword_term value for this Adword.
     * 
     * @param adword_term   * The Adword Term.
     */
    public void setAdword_term(java.lang.String adword_term) {
        this.adword_term = adword_term;
    }


    /**
     * Gets the adword_gclid value for this Adword.
     * 
     * @return adword_gclid   * The Adword GCLID.
     */
    public java.lang.Integer getAdword_gclid() {
        return adword_gclid;
    }


    /**
     * Sets the adword_gclid value for this Adword.
     * 
     * @param adword_gclid   * The Adword GCLID.
     */
    public void setAdword_gclid(java.lang.Integer adword_gclid) {
        this.adword_gclid = adword_gclid;
    }


    /**
     * Gets the adword_engine_domain_id value for this Adword.
     * 
     * @return adword_engine_domain_id   * The AdwordEngineDomain Id.
     */
    public java.lang.Integer getAdword_engine_domain_id() {
        return adword_engine_domain_id;
    }


    /**
     * Sets the adword_engine_domain_id value for this Adword.
     * 
     * @param adword_engine_domain_id   * The AdwordEngineDomain Id.
     */
    public void setAdword_engine_domain_id(java.lang.Integer adword_engine_domain_id) {
        this.adword_engine_domain_id = adword_engine_domain_id;
    }


    /**
     * Gets the adword_engine_id value for this Adword.
     * 
     * @return adword_engine_id   * The AdwordEngine Id.
     */
    public java.lang.Integer getAdword_engine_id() {
        return adword_engine_id;
    }


    /**
     * Sets the adword_engine_id value for this Adword.
     * 
     * @param adword_engine_id   * The AdwordEngine Id.
     */
    public void setAdword_engine_id(java.lang.Integer adword_engine_id) {
        this.adword_engine_id = adword_engine_id;
    }


    /**
     * Gets the search_id value for this Adword.
     * 
     * @return search_id   * The Search Id.
     */
    public java.lang.Integer getSearch_id() {
        return search_id;
    }


    /**
     * Sets the search_id value for this Adword.
     * 
     * @param search_id   * The Search Id.
     */
    public void setSearch_id(java.lang.Integer search_id) {
        this.search_id = search_id;
    }


    /**
     * Gets the date value for this Adword.
     * 
     * @return date   * The Date/Time of the Adword.
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this Adword.
     * 
     * @param date   * The Date/Time of the Adword.
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }


    /**
     * Gets the visit_id value for this Adword.
     * 
     * @return visit_id   * The Visit Id of the Adword.
     */
    public java.lang.Integer getVisit_id() {
        return visit_id;
    }


    /**
     * Sets the visit_id value for this Adword.
     * 
     * @param visit_id   * The Visit Id of the Adword.
     */
    public void setVisit_id(java.lang.Integer visit_id) {
        this.visit_id = visit_id;
    }


    /**
     * Gets the adwordEngine value for this Adword.
     * 
     * @return adwordEngine
     */
    public com.easyinsight.client.netresults.AdwordEngine getAdwordEngine() {
        return adwordEngine;
    }


    /**
     * Sets the adwordEngine value for this Adword.
     * 
     * @param adwordEngine
     */
    public void setAdwordEngine(com.easyinsight.client.netresults.AdwordEngine adwordEngine) {
        this.adwordEngine = adwordEngine;
    }


    /**
     * Gets the adwordEngineDomain value for this Adword.
     * 
     * @return adwordEngineDomain
     */
    public com.easyinsight.client.netresults.AdwordEngineDomain getAdwordEngineDomain() {
        return adwordEngineDomain;
    }


    /**
     * Sets the adwordEngineDomain value for this Adword.
     * 
     * @param adwordEngineDomain
     */
    public void setAdwordEngineDomain(com.easyinsight.client.netresults.AdwordEngineDomain adwordEngineDomain) {
        this.adwordEngineDomain = adwordEngineDomain;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Adword)) return false;
        Adword other = (Adword) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.adword_id==null && other.getAdword_id()==null) || 
             (this.adword_id!=null &&
              this.adword_id.equals(other.getAdword_id()))) &&
            ((this.adword_term==null && other.getAdword_term()==null) || 
             (this.adword_term!=null &&
              this.adword_term.equals(other.getAdword_term()))) &&
            ((this.adword_gclid==null && other.getAdword_gclid()==null) || 
             (this.adword_gclid!=null &&
              this.adword_gclid.equals(other.getAdword_gclid()))) &&
            ((this.adword_engine_domain_id==null && other.getAdword_engine_domain_id()==null) || 
             (this.adword_engine_domain_id!=null &&
              this.adword_engine_domain_id.equals(other.getAdword_engine_domain_id()))) &&
            ((this.adword_engine_id==null && other.getAdword_engine_id()==null) || 
             (this.adword_engine_id!=null &&
              this.adword_engine_id.equals(other.getAdword_engine_id()))) &&
            ((this.search_id==null && other.getSearch_id()==null) || 
             (this.search_id!=null &&
              this.search_id.equals(other.getSearch_id()))) &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.visit_id==null && other.getVisit_id()==null) || 
             (this.visit_id!=null &&
              this.visit_id.equals(other.getVisit_id()))) &&
            ((this.adwordEngine==null && other.getAdwordEngine()==null) || 
             (this.adwordEngine!=null &&
              this.adwordEngine.equals(other.getAdwordEngine()))) &&
            ((this.adwordEngineDomain==null && other.getAdwordEngineDomain()==null) || 
             (this.adwordEngineDomain!=null &&
              this.adwordEngineDomain.equals(other.getAdwordEngineDomain())));
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
        if (getAdword_id() != null) {
            _hashCode += getAdword_id().hashCode();
        }
        if (getAdword_term() != null) {
            _hashCode += getAdword_term().hashCode();
        }
        if (getAdword_gclid() != null) {
            _hashCode += getAdword_gclid().hashCode();
        }
        if (getAdword_engine_domain_id() != null) {
            _hashCode += getAdword_engine_domain_id().hashCode();
        }
        if (getAdword_engine_id() != null) {
            _hashCode += getAdword_engine_id().hashCode();
        }
        if (getSearch_id() != null) {
            _hashCode += getSearch_id().hashCode();
        }
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getVisit_id() != null) {
            _hashCode += getVisit_id().hashCode();
        }
        if (getAdwordEngine() != null) {
            _hashCode += getAdwordEngine().hashCode();
        }
        if (getAdwordEngineDomain() != null) {
            _hashCode += getAdwordEngineDomain().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Adword.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Adword"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_term");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_term"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_gclid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_gclid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_engine_domain_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_engine_domain_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adword_engine_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adword_engine_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "search_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visit_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visit_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adwordEngine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AdwordEngine"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "AdwordEngine"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adwordEngineDomain");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AdwordEngineDomain"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "AdwordEngineDomain"));
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
