/**
 * SettleDebtRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class SettleDebtRequest  implements java.io.Serializable {
    private java.lang.String senderTokenId;

    private java.lang.String callerTokenId;

    private java.lang.String creditInstrumentId;

    private com.easyinsight.amazon.Amount settlementAmount;

    private java.util.Calendar transactionDate;

    private java.lang.String senderReference;

    private java.lang.String recipientReference;

    private java.lang.String callerReference;

    private com.easyinsight.amazon.ChargeFeeTo chargeFeeTo;

    private java.lang.String senderDescription;

    private java.lang.String recipientDescription;

    private java.lang.String callerDescription;

    private java.lang.String metaData;

    public SettleDebtRequest() {
    }

    public SettleDebtRequest(
           java.lang.String senderTokenId,
           java.lang.String callerTokenId,
           java.lang.String creditInstrumentId,
           com.easyinsight.amazon.Amount settlementAmount,
           java.util.Calendar transactionDate,
           java.lang.String senderReference,
           java.lang.String recipientReference,
           java.lang.String callerReference,
           com.easyinsight.amazon.ChargeFeeTo chargeFeeTo,
           java.lang.String senderDescription,
           java.lang.String recipientDescription,
           java.lang.String callerDescription,
           java.lang.String metaData) {
           this.senderTokenId = senderTokenId;
           this.callerTokenId = callerTokenId;
           this.creditInstrumentId = creditInstrumentId;
           this.settlementAmount = settlementAmount;
           this.transactionDate = transactionDate;
           this.senderReference = senderReference;
           this.recipientReference = recipientReference;
           this.callerReference = callerReference;
           this.chargeFeeTo = chargeFeeTo;
           this.senderDescription = senderDescription;
           this.recipientDescription = recipientDescription;
           this.callerDescription = callerDescription;
           this.metaData = metaData;
    }


    /**
     * Gets the senderTokenId value for this SettleDebtRequest.
     * 
     * @return senderTokenId
     */
    public java.lang.String getSenderTokenId() {
        return senderTokenId;
    }


    /**
     * Sets the senderTokenId value for this SettleDebtRequest.
     * 
     * @param senderTokenId
     */
    public void setSenderTokenId(java.lang.String senderTokenId) {
        this.senderTokenId = senderTokenId;
    }


    /**
     * Gets the callerTokenId value for this SettleDebtRequest.
     * 
     * @return callerTokenId
     */
    public java.lang.String getCallerTokenId() {
        return callerTokenId;
    }


    /**
     * Sets the callerTokenId value for this SettleDebtRequest.
     * 
     * @param callerTokenId
     */
    public void setCallerTokenId(java.lang.String callerTokenId) {
        this.callerTokenId = callerTokenId;
    }


    /**
     * Gets the creditInstrumentId value for this SettleDebtRequest.
     * 
     * @return creditInstrumentId
     */
    public java.lang.String getCreditInstrumentId() {
        return creditInstrumentId;
    }


    /**
     * Sets the creditInstrumentId value for this SettleDebtRequest.
     * 
     * @param creditInstrumentId
     */
    public void setCreditInstrumentId(java.lang.String creditInstrumentId) {
        this.creditInstrumentId = creditInstrumentId;
    }


    /**
     * Gets the settlementAmount value for this SettleDebtRequest.
     * 
     * @return settlementAmount
     */
    public com.easyinsight.amazon.Amount getSettlementAmount() {
        return settlementAmount;
    }


    /**
     * Sets the settlementAmount value for this SettleDebtRequest.
     * 
     * @param settlementAmount
     */
    public void setSettlementAmount(com.easyinsight.amazon.Amount settlementAmount) {
        this.settlementAmount = settlementAmount;
    }


    /**
     * Gets the transactionDate value for this SettleDebtRequest.
     * 
     * @return transactionDate
     */
    public java.util.Calendar getTransactionDate() {
        return transactionDate;
    }


    /**
     * Sets the transactionDate value for this SettleDebtRequest.
     * 
     * @param transactionDate
     */
    public void setTransactionDate(java.util.Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }


    /**
     * Gets the senderReference value for this SettleDebtRequest.
     * 
     * @return senderReference
     */
    public java.lang.String getSenderReference() {
        return senderReference;
    }


    /**
     * Sets the senderReference value for this SettleDebtRequest.
     * 
     * @param senderReference
     */
    public void setSenderReference(java.lang.String senderReference) {
        this.senderReference = senderReference;
    }


    /**
     * Gets the recipientReference value for this SettleDebtRequest.
     * 
     * @return recipientReference
     */
    public java.lang.String getRecipientReference() {
        return recipientReference;
    }


    /**
     * Sets the recipientReference value for this SettleDebtRequest.
     * 
     * @param recipientReference
     */
    public void setRecipientReference(java.lang.String recipientReference) {
        this.recipientReference = recipientReference;
    }


    /**
     * Gets the callerReference value for this SettleDebtRequest.
     * 
     * @return callerReference
     */
    public java.lang.String getCallerReference() {
        return callerReference;
    }


    /**
     * Sets the callerReference value for this SettleDebtRequest.
     * 
     * @param callerReference
     */
    public void setCallerReference(java.lang.String callerReference) {
        this.callerReference = callerReference;
    }


    /**
     * Gets the chargeFeeTo value for this SettleDebtRequest.
     * 
     * @return chargeFeeTo
     */
    public com.easyinsight.amazon.ChargeFeeTo getChargeFeeTo() {
        return chargeFeeTo;
    }


    /**
     * Sets the chargeFeeTo value for this SettleDebtRequest.
     * 
     * @param chargeFeeTo
     */
    public void setChargeFeeTo(com.easyinsight.amazon.ChargeFeeTo chargeFeeTo) {
        this.chargeFeeTo = chargeFeeTo;
    }


    /**
     * Gets the senderDescription value for this SettleDebtRequest.
     * 
     * @return senderDescription
     */
    public java.lang.String getSenderDescription() {
        return senderDescription;
    }


    /**
     * Sets the senderDescription value for this SettleDebtRequest.
     * 
     * @param senderDescription
     */
    public void setSenderDescription(java.lang.String senderDescription) {
        this.senderDescription = senderDescription;
    }


    /**
     * Gets the recipientDescription value for this SettleDebtRequest.
     * 
     * @return recipientDescription
     */
    public java.lang.String getRecipientDescription() {
        return recipientDescription;
    }


    /**
     * Sets the recipientDescription value for this SettleDebtRequest.
     * 
     * @param recipientDescription
     */
    public void setRecipientDescription(java.lang.String recipientDescription) {
        this.recipientDescription = recipientDescription;
    }


    /**
     * Gets the callerDescription value for this SettleDebtRequest.
     * 
     * @return callerDescription
     */
    public java.lang.String getCallerDescription() {
        return callerDescription;
    }


    /**
     * Sets the callerDescription value for this SettleDebtRequest.
     * 
     * @param callerDescription
     */
    public void setCallerDescription(java.lang.String callerDescription) {
        this.callerDescription = callerDescription;
    }


    /**
     * Gets the metaData value for this SettleDebtRequest.
     * 
     * @return metaData
     */
    public java.lang.String getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this SettleDebtRequest.
     * 
     * @param metaData
     */
    public void setMetaData(java.lang.String metaData) {
        this.metaData = metaData;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SettleDebtRequest)) return false;
        SettleDebtRequest other = (SettleDebtRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.senderTokenId==null && other.getSenderTokenId()==null) || 
             (this.senderTokenId!=null &&
              this.senderTokenId.equals(other.getSenderTokenId()))) &&
            ((this.callerTokenId==null && other.getCallerTokenId()==null) || 
             (this.callerTokenId!=null &&
              this.callerTokenId.equals(other.getCallerTokenId()))) &&
            ((this.creditInstrumentId==null && other.getCreditInstrumentId()==null) || 
             (this.creditInstrumentId!=null &&
              this.creditInstrumentId.equals(other.getCreditInstrumentId()))) &&
            ((this.settlementAmount==null && other.getSettlementAmount()==null) || 
             (this.settlementAmount!=null &&
              this.settlementAmount.equals(other.getSettlementAmount()))) &&
            ((this.transactionDate==null && other.getTransactionDate()==null) || 
             (this.transactionDate!=null &&
              this.transactionDate.equals(other.getTransactionDate()))) &&
            ((this.senderReference==null && other.getSenderReference()==null) || 
             (this.senderReference!=null &&
              this.senderReference.equals(other.getSenderReference()))) &&
            ((this.recipientReference==null && other.getRecipientReference()==null) || 
             (this.recipientReference!=null &&
              this.recipientReference.equals(other.getRecipientReference()))) &&
            ((this.callerReference==null && other.getCallerReference()==null) || 
             (this.callerReference!=null &&
              this.callerReference.equals(other.getCallerReference()))) &&
            ((this.chargeFeeTo==null && other.getChargeFeeTo()==null) || 
             (this.chargeFeeTo!=null &&
              this.chargeFeeTo.equals(other.getChargeFeeTo()))) &&
            ((this.senderDescription==null && other.getSenderDescription()==null) || 
             (this.senderDescription!=null &&
              this.senderDescription.equals(other.getSenderDescription()))) &&
            ((this.recipientDescription==null && other.getRecipientDescription()==null) || 
             (this.recipientDescription!=null &&
              this.recipientDescription.equals(other.getRecipientDescription()))) &&
            ((this.callerDescription==null && other.getCallerDescription()==null) || 
             (this.callerDescription!=null &&
              this.callerDescription.equals(other.getCallerDescription()))) &&
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
        if (getSenderTokenId() != null) {
            _hashCode += getSenderTokenId().hashCode();
        }
        if (getCallerTokenId() != null) {
            _hashCode += getCallerTokenId().hashCode();
        }
        if (getCreditInstrumentId() != null) {
            _hashCode += getCreditInstrumentId().hashCode();
        }
        if (getSettlementAmount() != null) {
            _hashCode += getSettlementAmount().hashCode();
        }
        if (getTransactionDate() != null) {
            _hashCode += getTransactionDate().hashCode();
        }
        if (getSenderReference() != null) {
            _hashCode += getSenderReference().hashCode();
        }
        if (getRecipientReference() != null) {
            _hashCode += getRecipientReference().hashCode();
        }
        if (getCallerReference() != null) {
            _hashCode += getCallerReference().hashCode();
        }
        if (getChargeFeeTo() != null) {
            _hashCode += getChargeFeeTo().hashCode();
        }
        if (getSenderDescription() != null) {
            _hashCode += getSenderDescription().hashCode();
        }
        if (getRecipientDescription() != null) {
            _hashCode += getRecipientDescription().hashCode();
        }
        if (getCallerDescription() != null) {
            _hashCode += getCallerDescription().hashCode();
        }
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SettleDebtRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleDebtRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SenderTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditInstrumentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CreditInstrumentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("settlementAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SettlementAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
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
        elemField.setFieldName("senderReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SenderReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RecipientReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chargeFeeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ChargeFeeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ChargeFeeTo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SenderDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RecipientDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
