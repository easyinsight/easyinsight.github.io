/**
 * TokenUsageLimit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class TokenUsageLimit  implements java.io.Serializable {
    private java.math.BigInteger count;

    private com.easyinsight.amazon.Amount amount;

    private java.math.BigInteger lastResetCount;

    private com.easyinsight.amazon.Amount lastResetAmount;

    private java.util.Calendar lastResetTimeStamp;

    public TokenUsageLimit() {
    }

    public TokenUsageLimit(
           java.math.BigInteger count,
           com.easyinsight.amazon.Amount amount,
           java.math.BigInteger lastResetCount,
           com.easyinsight.amazon.Amount lastResetAmount,
           java.util.Calendar lastResetTimeStamp) {
           this.count = count;
           this.amount = amount;
           this.lastResetCount = lastResetCount;
           this.lastResetAmount = lastResetAmount;
           this.lastResetTimeStamp = lastResetTimeStamp;
    }


    /**
     * Gets the count value for this TokenUsageLimit.
     * 
     * @return count
     */
    public java.math.BigInteger getCount() {
        return count;
    }


    /**
     * Sets the count value for this TokenUsageLimit.
     * 
     * @param count
     */
    public void setCount(java.math.BigInteger count) {
        this.count = count;
    }


    /**
     * Gets the amount value for this TokenUsageLimit.
     * 
     * @return amount
     */
    public com.easyinsight.amazon.Amount getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this TokenUsageLimit.
     * 
     * @param amount
     */
    public void setAmount(com.easyinsight.amazon.Amount amount) {
        this.amount = amount;
    }


    /**
     * Gets the lastResetCount value for this TokenUsageLimit.
     * 
     * @return lastResetCount
     */
    public java.math.BigInteger getLastResetCount() {
        return lastResetCount;
    }


    /**
     * Sets the lastResetCount value for this TokenUsageLimit.
     * 
     * @param lastResetCount
     */
    public void setLastResetCount(java.math.BigInteger lastResetCount) {
        this.lastResetCount = lastResetCount;
    }


    /**
     * Gets the lastResetAmount value for this TokenUsageLimit.
     * 
     * @return lastResetAmount
     */
    public com.easyinsight.amazon.Amount getLastResetAmount() {
        return lastResetAmount;
    }


    /**
     * Sets the lastResetAmount value for this TokenUsageLimit.
     * 
     * @param lastResetAmount
     */
    public void setLastResetAmount(com.easyinsight.amazon.Amount lastResetAmount) {
        this.lastResetAmount = lastResetAmount;
    }


    /**
     * Gets the lastResetTimeStamp value for this TokenUsageLimit.
     * 
     * @return lastResetTimeStamp
     */
    public java.util.Calendar getLastResetTimeStamp() {
        return lastResetTimeStamp;
    }


    /**
     * Sets the lastResetTimeStamp value for this TokenUsageLimit.
     * 
     * @param lastResetTimeStamp
     */
    public void setLastResetTimeStamp(java.util.Calendar lastResetTimeStamp) {
        this.lastResetTimeStamp = lastResetTimeStamp;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TokenUsageLimit)) return false;
        TokenUsageLimit other = (TokenUsageLimit) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.count==null && other.getCount()==null) || 
             (this.count!=null &&
              this.count.equals(other.getCount()))) &&
            ((this.amount==null && other.getAmount()==null) || 
             (this.amount!=null &&
              this.amount.equals(other.getAmount()))) &&
            ((this.lastResetCount==null && other.getLastResetCount()==null) || 
             (this.lastResetCount!=null &&
              this.lastResetCount.equals(other.getLastResetCount()))) &&
            ((this.lastResetAmount==null && other.getLastResetAmount()==null) || 
             (this.lastResetAmount!=null &&
              this.lastResetAmount.equals(other.getLastResetAmount()))) &&
            ((this.lastResetTimeStamp==null && other.getLastResetTimeStamp()==null) || 
             (this.lastResetTimeStamp!=null &&
              this.lastResetTimeStamp.equals(other.getLastResetTimeStamp())));
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
        if (getCount() != null) {
            _hashCode += getCount().hashCode();
        }
        if (getAmount() != null) {
            _hashCode += getAmount().hashCode();
        }
        if (getLastResetCount() != null) {
            _hashCode += getLastResetCount().hashCode();
        }
        if (getLastResetAmount() != null) {
            _hashCode += getLastResetAmount().hashCode();
        }
        if (getLastResetTimeStamp() != null) {
            _hashCode += getLastResetTimeStamp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TokenUsageLimit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenUsageLimit"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastResetCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LastResetCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastResetAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LastResetAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastResetTimeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LastResetTimeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
