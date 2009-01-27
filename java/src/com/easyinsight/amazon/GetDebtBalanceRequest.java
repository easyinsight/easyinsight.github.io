/**
 * GetDebtBalanceRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class GetDebtBalanceRequest  implements java.io.Serializable {
    private java.lang.String creditInstrumentId;

    public GetDebtBalanceRequest() {
    }

    public GetDebtBalanceRequest(
           java.lang.String creditInstrumentId) {
           this.creditInstrumentId = creditInstrumentId;
    }


    /**
     * Gets the creditInstrumentId value for this GetDebtBalanceRequest.
     * 
     * @return creditInstrumentId
     */
    public java.lang.String getCreditInstrumentId() {
        return creditInstrumentId;
    }


    /**
     * Sets the creditInstrumentId value for this GetDebtBalanceRequest.
     * 
     * @param creditInstrumentId
     */
    public void setCreditInstrumentId(java.lang.String creditInstrumentId) {
        this.creditInstrumentId = creditInstrumentId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetDebtBalanceRequest)) return false;
        GetDebtBalanceRequest other = (GetDebtBalanceRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.creditInstrumentId==null && other.getCreditInstrumentId()==null) || 
             (this.creditInstrumentId!=null &&
              this.creditInstrumentId.equals(other.getCreditInstrumentId())));
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
        if (getCreditInstrumentId() != null) {
            _hashCode += getCreditInstrumentId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetDebtBalanceRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetDebtBalanceRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditInstrumentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CreditInstrumentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
