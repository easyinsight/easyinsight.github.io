/**
 * AccountBalance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class AccountBalance  implements java.io.Serializable {
    private com.easyinsight.amazon.Amount totalBalance;

    private com.easyinsight.amazon.Amount pendingInBalance;

    private com.easyinsight.amazon.Amount pendingOutBalance;

    private com.easyinsight.amazon.AvailableBalances availableBalances;

    public AccountBalance() {
    }

    public AccountBalance(
           com.easyinsight.amazon.Amount totalBalance,
           com.easyinsight.amazon.Amount pendingInBalance,
           com.easyinsight.amazon.Amount pendingOutBalance,
           com.easyinsight.amazon.AvailableBalances availableBalances) {
           this.totalBalance = totalBalance;
           this.pendingInBalance = pendingInBalance;
           this.pendingOutBalance = pendingOutBalance;
           this.availableBalances = availableBalances;
    }


    /**
     * Gets the totalBalance value for this AccountBalance.
     * 
     * @return totalBalance
     */
    public com.easyinsight.amazon.Amount getTotalBalance() {
        return totalBalance;
    }


    /**
     * Sets the totalBalance value for this AccountBalance.
     * 
     * @param totalBalance
     */
    public void setTotalBalance(com.easyinsight.amazon.Amount totalBalance) {
        this.totalBalance = totalBalance;
    }


    /**
     * Gets the pendingInBalance value for this AccountBalance.
     * 
     * @return pendingInBalance
     */
    public com.easyinsight.amazon.Amount getPendingInBalance() {
        return pendingInBalance;
    }


    /**
     * Sets the pendingInBalance value for this AccountBalance.
     * 
     * @param pendingInBalance
     */
    public void setPendingInBalance(com.easyinsight.amazon.Amount pendingInBalance) {
        this.pendingInBalance = pendingInBalance;
    }


    /**
     * Gets the pendingOutBalance value for this AccountBalance.
     * 
     * @return pendingOutBalance
     */
    public com.easyinsight.amazon.Amount getPendingOutBalance() {
        return pendingOutBalance;
    }


    /**
     * Sets the pendingOutBalance value for this AccountBalance.
     * 
     * @param pendingOutBalance
     */
    public void setPendingOutBalance(com.easyinsight.amazon.Amount pendingOutBalance) {
        this.pendingOutBalance = pendingOutBalance;
    }


    /**
     * Gets the availableBalances value for this AccountBalance.
     * 
     * @return availableBalances
     */
    public com.easyinsight.amazon.AvailableBalances getAvailableBalances() {
        return availableBalances;
    }


    /**
     * Sets the availableBalances value for this AccountBalance.
     * 
     * @param availableBalances
     */
    public void setAvailableBalances(com.easyinsight.amazon.AvailableBalances availableBalances) {
        this.availableBalances = availableBalances;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccountBalance)) return false;
        AccountBalance other = (AccountBalance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.totalBalance==null && other.getTotalBalance()==null) || 
             (this.totalBalance!=null &&
              this.totalBalance.equals(other.getTotalBalance()))) &&
            ((this.pendingInBalance==null && other.getPendingInBalance()==null) || 
             (this.pendingInBalance!=null &&
              this.pendingInBalance.equals(other.getPendingInBalance()))) &&
            ((this.pendingOutBalance==null && other.getPendingOutBalance()==null) || 
             (this.pendingOutBalance!=null &&
              this.pendingOutBalance.equals(other.getPendingOutBalance()))) &&
            ((this.availableBalances==null && other.getAvailableBalances()==null) || 
             (this.availableBalances!=null &&
              this.availableBalances.equals(other.getAvailableBalances())));
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
        if (getTotalBalance() != null) {
            _hashCode += getTotalBalance().hashCode();
        }
        if (getPendingInBalance() != null) {
            _hashCode += getPendingInBalance().hashCode();
        }
        if (getPendingOutBalance() != null) {
            _hashCode += getPendingOutBalance().hashCode();
        }
        if (getAvailableBalances() != null) {
            _hashCode += getAvailableBalances().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AccountBalance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AccountBalance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TotalBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pendingInBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PendingInBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pendingOutBalance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PendingOutBalance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("availableBalances");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AvailableBalances"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AvailableBalances"));
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
