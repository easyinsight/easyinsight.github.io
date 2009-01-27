/**
 * AvailableBalances.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class AvailableBalances  implements java.io.Serializable {
    private com.easyinsight.amazon.Amount disburseBalance;

    private com.easyinsight.amazon.Amount refundBalance;

    public AvailableBalances() {
    }

    public AvailableBalances(
           com.easyinsight.amazon.Amount disburseBalance,
           com.easyinsight.amazon.Amount refundBalance) {
           this.disburseBalance = disburseBalance;
           this.refundBalance = refundBalance;
    }


    /**
     * Gets the disburseBalance value for this AvailableBalances.
     * 
     * @return disburseBalance
     */
    public com.easyinsight.amazon.Amount getDisburseBalance() {
        return disburseBalance;
    }


    /**
     * Sets the disburseBalance value for this AvailableBalances.
     * 
     * @param disburseBalance
     */
    public void setDisburseBalance(com.easyinsight.amazon.Amount disburseBalance) {
        this.disburseBalance = disburseBalance;
    }


    /**
     * Gets the refundBalance value for this AvailableBalances.
     * 
     * @return refundBalance
     */
    public com.easyinsight.amazon.Amount getRefundBalance() {
        return refundBalance;
    }


    /**
     * Sets the refundBalance value for this AvailableBalances.
     * 
     * @param refundBalance
     */
    public void setRefundBalance(com.easyinsight.amazon.Amount refundBalance) {
        this.refundBalance = refundBalance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AvailableBalances)) return false;
        AvailableBalances other = (AvailableBalances) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.disburseBalance==null && other.getDisburseBalance()==null) || 
             (this.disburseBalance!=null &&
              this.disburseBalance.equals(other.getDisburseBalance()))) &&
            ((this.refundBalance==null && other.getRefundBalance()==null) || 
             (this.refundBalance!=null &&
              this.refundBalance.equals(other.getRefundBalance())));
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
        if (getDisburseBalance() != null) {
            _hashCode += getDisburseBalance().hashCode();
        }
        if (getRefundBalance() != null) {
            _hashCode += getRefundBalance().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AvailableBalances.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AvailableBalances"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disburseBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DisburseBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
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
