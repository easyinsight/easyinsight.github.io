/**
 * OutstandingDebtBalance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class OutstandingDebtBalance  implements java.io.Serializable {
    private com.easyinsight.amazon.Amount outstandingBalance;

    private com.easyinsight.amazon.Amount pendingOutBalance;

    public OutstandingDebtBalance() {
    }

    public OutstandingDebtBalance(
           com.easyinsight.amazon.Amount outstandingBalance,
           com.easyinsight.amazon.Amount pendingOutBalance) {
           this.outstandingBalance = outstandingBalance;
           this.pendingOutBalance = pendingOutBalance;
    }


    /**
     * Gets the outstandingBalance value for this OutstandingDebtBalance.
     * 
     * @return outstandingBalance
     */
    public com.easyinsight.amazon.Amount getOutstandingBalance() {
        return outstandingBalance;
    }


    /**
     * Sets the outstandingBalance value for this OutstandingDebtBalance.
     * 
     * @param outstandingBalance
     */
    public void setOutstandingBalance(com.easyinsight.amazon.Amount outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }


    /**
     * Gets the pendingOutBalance value for this OutstandingDebtBalance.
     * 
     * @return pendingOutBalance
     */
    public com.easyinsight.amazon.Amount getPendingOutBalance() {
        return pendingOutBalance;
    }


    /**
     * Sets the pendingOutBalance value for this OutstandingDebtBalance.
     * 
     * @param pendingOutBalance
     */
    public void setPendingOutBalance(com.easyinsight.amazon.Amount pendingOutBalance) {
        this.pendingOutBalance = pendingOutBalance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OutstandingDebtBalance)) return false;
        OutstandingDebtBalance other = (OutstandingDebtBalance) obj;
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
            ((this.pendingOutBalance==null && other.getPendingOutBalance()==null) || 
             (this.pendingOutBalance!=null &&
              this.pendingOutBalance.equals(other.getPendingOutBalance())));
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
        if (getPendingOutBalance() != null) {
            _hashCode += getPendingOutBalance().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OutstandingDebtBalance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "OutstandingDebtBalance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outstandingBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OutstandingBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pendingOutBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PendingOutBalance"));
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
