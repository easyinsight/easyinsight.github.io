/**
 * SettleRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class SettleRequest  implements java.io.Serializable {
    private java.lang.String reserveTransactionId;

    private com.easyinsight.amazon.Amount transactionAmount;

    private java.util.Calendar transactionDate;

    private java.lang.String metaData;

    public SettleRequest() {
    }

    public SettleRequest(
           java.lang.String reserveTransactionId,
           com.easyinsight.amazon.Amount transactionAmount,
           java.util.Calendar transactionDate,
           java.lang.String metaData) {
           this.reserveTransactionId = reserveTransactionId;
           this.transactionAmount = transactionAmount;
           this.transactionDate = transactionDate;
           this.metaData = metaData;
    }


    /**
     * Gets the reserveTransactionId value for this SettleRequest.
     * 
     * @return reserveTransactionId
     */
    public java.lang.String getReserveTransactionId() {
        return reserveTransactionId;
    }


    /**
     * Sets the reserveTransactionId value for this SettleRequest.
     * 
     * @param reserveTransactionId
     */
    public void setReserveTransactionId(java.lang.String reserveTransactionId) {
        this.reserveTransactionId = reserveTransactionId;
    }


    /**
     * Gets the transactionAmount value for this SettleRequest.
     * 
     * @return transactionAmount
     */
    public com.easyinsight.amazon.Amount getTransactionAmount() {
        return transactionAmount;
    }


    /**
     * Sets the transactionAmount value for this SettleRequest.
     * 
     * @param transactionAmount
     */
    public void setTransactionAmount(com.easyinsight.amazon.Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    /**
     * Gets the transactionDate value for this SettleRequest.
     * 
     * @return transactionDate
     */
    public java.util.Calendar getTransactionDate() {
        return transactionDate;
    }


    /**
     * Sets the transactionDate value for this SettleRequest.
     * 
     * @param transactionDate
     */
    public void setTransactionDate(java.util.Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }


    /**
     * Gets the metaData value for this SettleRequest.
     * 
     * @return metaData
     */
    public java.lang.String getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this SettleRequest.
     * 
     * @param metaData
     */
    public void setMetaData(java.lang.String metaData) {
        this.metaData = metaData;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SettleRequest)) return false;
        SettleRequest other = (SettleRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.reserveTransactionId==null && other.getReserveTransactionId()==null) || 
             (this.reserveTransactionId!=null &&
              this.reserveTransactionId.equals(other.getReserveTransactionId()))) &&
            ((this.transactionAmount==null && other.getTransactionAmount()==null) || 
             (this.transactionAmount!=null &&
              this.transactionAmount.equals(other.getTransactionAmount()))) &&
            ((this.transactionDate==null && other.getTransactionDate()==null) || 
             (this.transactionDate!=null &&
              this.transactionDate.equals(other.getTransactionDate()))) &&
            ((this.metaData==null && other.getMetaData()==null) || 
             (this.metaData!=null &&
              this.metaData.equals(other.getMetaData())));
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
        if (getReserveTransactionId() != null) {
            _hashCode += getReserveTransactionId().hashCode();
        }
        if (getTransactionAmount() != null) {
            _hashCode += getTransactionAmount().hashCode();
        }
        if (getTransactionDate() != null) {
            _hashCode += getTransactionDate().hashCode();
        }
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SettleRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reserveTransactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ReserveTransactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MetaData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
