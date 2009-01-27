/**
 * PrepaidBalance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class PrepaidBalance  implements java.io.Serializable {
    private com.easyinsight.amazon.Amount availableBalance;

    private com.easyinsight.amazon.Amount pendingInBalance;

    public PrepaidBalance() {
    }

    public PrepaidBalance(
           com.easyinsight.amazon.Amount availableBalance,
           com.easyinsight.amazon.Amount pendingInBalance) {
           this.availableBalance = availableBalance;
           this.pendingInBalance = pendingInBalance;
    }


    /**
     * Gets the availableBalance value for this PrepaidBalance.
     * 
     * @return availableBalance
     */
    public com.easyinsight.amazon.Amount getAvailableBalance() {
        return availableBalance;
    }


    /**
     * Sets the availableBalance value for this PrepaidBalance.
     * 
     * @param availableBalance
     */
    public void setAvailableBalance(com.easyinsight.amazon.Amount availableBalance) {
        this.availableBalance = availableBalance;
    }


    /**
     * Gets the pendingInBalance value for this PrepaidBalance.
     * 
     * @return pendingInBalance
     */
    public com.easyinsight.amazon.Amount getPendingInBalance() {
        return pendingInBalance;
    }


    /**
     * Sets the pendingInBalance value for this PrepaidBalance.
     * 
     * @param pendingInBalance
     */
    public void setPendingInBalance(com.easyinsight.amazon.Amount pendingInBalance) {
        this.pendingInBalance = pendingInBalance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PrepaidBalance)) return false;
        PrepaidBalance other = (PrepaidBalance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.availableBalance==null && other.getAvailableBalance()==null) || 
             (this.availableBalance!=null &&
              this.availableBalance.equals(other.getAvailableBalance()))) &&
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
        if (getAvailableBalance() != null) {
            _hashCode += getAvailableBalance().hashCode();
        }
        if (getPendingInBalance() != null) {
            _hashCode += getPendingInBalance().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PrepaidBalance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PrepaidBalance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("availableBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AvailableBalance"));
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
