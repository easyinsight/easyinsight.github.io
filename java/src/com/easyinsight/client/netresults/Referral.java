/**
 * Referral.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class Referral  implements java.io.Serializable {
    /* The Id of the EmailList */
    private java.lang.Integer referral_id;

    /* The link of the Referral. */
    private java.lang.String referral;

    /* The Id of the referrer. Each FQDN TLD has an Id. */
    private java.lang.Integer referrer_id;

    /* The Visit Id of the the Referral. */
    private java.lang.Integer visit_id;

    /* The Date/Time of the Referral. */
    private java.util.Calendar date;

    private com.easyinsight.client.netresults.Referrer referrer;

    public Referral() {
    }

    public Referral(
           java.lang.Integer referral_id,
           java.lang.String referral,
           java.lang.Integer referrer_id,
           java.lang.Integer visit_id,
           java.util.Calendar date,
           com.easyinsight.client.netresults.Referrer referrer) {
           this.referral_id = referral_id;
           this.referral = referral;
           this.referrer_id = referrer_id;
           this.visit_id = visit_id;
           this.date = date;
           this.referrer = referrer;
    }


    /**
     * Gets the referral_id value for this Referral.
     * 
     * @return referral_id   * The Id of the EmailList
     */
    public java.lang.Integer getReferral_id() {
        return referral_id;
    }


    /**
     * Sets the referral_id value for this Referral.
     * 
     * @param referral_id   * The Id of the EmailList
     */
    public void setReferral_id(java.lang.Integer referral_id) {
        this.referral_id = referral_id;
    }


    /**
     * Gets the referral value for this Referral.
     * 
     * @return referral   * The link of the Referral.
     */
    public java.lang.String getReferral() {
        return referral;
    }


    /**
     * Sets the referral value for this Referral.
     * 
     * @param referral   * The link of the Referral.
     */
    public void setReferral(java.lang.String referral) {
        this.referral = referral;
    }


    /**
     * Gets the referrer_id value for this Referral.
     * 
     * @return referrer_id   * The Id of the referrer. Each FQDN TLD has an Id.
     */
    public java.lang.Integer getReferrer_id() {
        return referrer_id;
    }


    /**
     * Sets the referrer_id value for this Referral.
     * 
     * @param referrer_id   * The Id of the referrer. Each FQDN TLD has an Id.
     */
    public void setReferrer_id(java.lang.Integer referrer_id) {
        this.referrer_id = referrer_id;
    }


    /**
     * Gets the visit_id value for this Referral.
     * 
     * @return visit_id   * The Visit Id of the the Referral.
     */
    public java.lang.Integer getVisit_id() {
        return visit_id;
    }


    /**
     * Sets the visit_id value for this Referral.
     * 
     * @param visit_id   * The Visit Id of the the Referral.
     */
    public void setVisit_id(java.lang.Integer visit_id) {
        this.visit_id = visit_id;
    }


    /**
     * Gets the date value for this Referral.
     * 
     * @return date   * The Date/Time of the Referral.
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this Referral.
     * 
     * @param date   * The Date/Time of the Referral.
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }


    /**
     * Gets the referrer value for this Referral.
     * 
     * @return referrer
     */
    public com.easyinsight.client.netresults.Referrer getReferrer() {
        return referrer;
    }


    /**
     * Sets the referrer value for this Referral.
     * 
     * @param referrer
     */
    public void setReferrer(com.easyinsight.client.netresults.Referrer referrer) {
        this.referrer = referrer;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Referral)) return false;
        Referral other = (Referral) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.referral_id==null && other.getReferral_id()==null) || 
             (this.referral_id!=null &&
              this.referral_id.equals(other.getReferral_id()))) &&
            ((this.referral==null && other.getReferral()==null) || 
             (this.referral!=null &&
              this.referral.equals(other.getReferral()))) &&
            ((this.referrer_id==null && other.getReferrer_id()==null) || 
             (this.referrer_id!=null &&
              this.referrer_id.equals(other.getReferrer_id()))) &&
            ((this.visit_id==null && other.getVisit_id()==null) || 
             (this.visit_id!=null &&
              this.visit_id.equals(other.getVisit_id()))) &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
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
        if (getReferral_id() != null) {
            _hashCode += getReferral_id().hashCode();
        }
        if (getReferral() != null) {
            _hashCode += getReferral().hashCode();
        }
        if (getReferrer_id() != null) {
            _hashCode += getReferrer_id().hashCode();
        }
        if (getVisit_id() != null) {
            _hashCode += getVisit_id().hashCode();
        }
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getReferrer() != null) {
            _hashCode += getReferrer().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Referral.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Referral"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referral_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referral_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referral");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referral"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referrer_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referrer_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visit_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visit_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referrer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Referrer"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Referrer"));
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
