/**
 * DirectAccess.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class DirectAccess  implements java.io.Serializable {
    /* The DirectAccess Id. */
    private int direct_access_id;

    /* The Visit Id. */
    private int visit_id;

    /* The Date/Time of the DirectAccess. */
    private java.util.Calendar date;

    public DirectAccess() {
    }

    public DirectAccess(
           int direct_access_id,
           int visit_id,
           java.util.Calendar date) {
           this.direct_access_id = direct_access_id;
           this.visit_id = visit_id;
           this.date = date;
    }


    /**
     * Gets the direct_access_id value for this DirectAccess.
     * 
     * @return direct_access_id   * The DirectAccess Id.
     */
    public int getDirect_access_id() {
        return direct_access_id;
    }


    /**
     * Sets the direct_access_id value for this DirectAccess.
     * 
     * @param direct_access_id   * The DirectAccess Id.
     */
    public void setDirect_access_id(int direct_access_id) {
        this.direct_access_id = direct_access_id;
    }


    /**
     * Gets the visit_id value for this DirectAccess.
     * 
     * @return visit_id   * The Visit Id.
     */
    public int getVisit_id() {
        return visit_id;
    }


    /**
     * Sets the visit_id value for this DirectAccess.
     * 
     * @param visit_id   * The Visit Id.
     */
    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }


    /**
     * Gets the date value for this DirectAccess.
     * 
     * @return date   * The Date/Time of the DirectAccess.
     */
    public java.util.Calendar getDate() {
        return date;
    }


    /**
     * Sets the date value for this DirectAccess.
     * 
     * @param date   * The Date/Time of the DirectAccess.
     */
    public void setDate(java.util.Calendar date) {
        this.date = date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DirectAccess)) return false;
        DirectAccess other = (DirectAccess) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.direct_access_id == other.getDirect_access_id() &&
            this.visit_id == other.getVisit_id() &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate())));
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
        _hashCode += getDirect_access_id();
        _hashCode += getVisit_id();
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DirectAccess.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "DirectAccess"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direct_access_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "direct_access_id"));
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
