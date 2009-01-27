/**
 * OutstandingPrepaidLiability.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class OutstandingPrepaidLiability  implements java.io.Serializable {
    private com.easyinsight.amazon.Amount outstandingBalance;

    private com.easyinsight.amazon.Amount pendingInBalance;

    public OutstandingPrepaidLiability() {
    }

    public OutstandingPrepaidLiability(
           com.easyinsight.amazon.Amount outstandingBalance,
           com.easyinsight.amazon.Amount pendingInBalance) {
           this.outstandingBalance = outstandingBalance;
           this.pendingInBalance = pendingInBalance;
    }


    /**
     * Gets the outstandingBalance value for this OutstandingPrepaidLiability.
     * 
     * @return outstandingBalance
     */
    public com.easyinsight.amazon.Amount getOutstandingBalance() {
        return outstandingBalance;
    }


    /**
     * Sets the outstandingBalance value for this OutstandingPrepaidLiability.
     * 
     * @param outstandingBalance
     */
    public void setOutstandingBalance(com.easyinsight.amazon.Amount outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }


    /**
     * Gets the pendingInBalance value for this OutstandingPrepaidLiability.
     * 
     * @return pendingInBalance
     */
    public com.easyinsight.amazon.Amount getPendingInBalance() {
        return pendingInBalance;
    }


    /**
     * Sets the pendingInBalance value for this OutstandingPrepaidLiability.
     * 
     * @param pendingInBalance
     */
    public void setPendingInBalance(com.easyinsight.amazon.Amount pendingInBalance) {
        this.pendingInBalance = pendingInBalance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OutstandingPrepaidLiability)) return false;
        OutstandingPrepaidLiability other = (OutstandingPrepaidLiability) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.outstandingBalance==null && other.getOutstandingBalance()==null) || 
             (this.outstandingBalance!=null &&
              this.outstandingBalance.equals(other.getOutstandingBalance()))) &&
            ((this.pendingInBalance==null && other.getPendingInBalance()==null) || 
             (this.pendingInBalance!=null &&
              this.pendingInBalance.equals(other.getPendingInBalance())));
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
        if (getOutstandingBalance() != null) {
            _hashCode += getOutstandingBalance().hashCode();
        }
        if (getPendingInBalance() != null) {
            _hashCode += getPendingInBalance().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OutstandingPrepaidLiability.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "OutstandingPrepaidLiability"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outstandingBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OutstandingBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pendingInBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PendingInBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
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
