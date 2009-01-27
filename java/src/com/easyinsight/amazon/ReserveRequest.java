/**
 * ReserveRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class ReserveRequest  implements java.io.Serializable {
    private java.lang.String callerTokenId;

    private java.lang.String senderTokenId;

    private java.lang.String recipientTokenId;

    private com.easyinsight.amazon.Amount transactionAmount;

    private java.util.Calendar transactionDate;

    private com.easyinsight.amazon.ChargeFeeTo chargeFeeTo;

    private java.lang.String callerReference;

    private java.lang.String senderReference;

    private java.lang.String recipientReference;

    private java.lang.String callerDescription;

    private java.lang.String senderDescription;

    private java.lang.String recipientDescription;

    private java.lang.String metaData;

    private com.easyinsight.amazon.Amount marketplaceFixedFee;

    private java.math.BigDecimal marketplaceVariableFee;

    public ReserveRequest() {
    }

    public ReserveRequest(
           java.lang.String callerTokenId,
           java.lang.String senderTokenId,
           java.lang.String recipientTokenId,
           com.easyinsight.amazon.Amount transactionAmount,
           java.util.Calendar transactionDate,
           com.easyinsight.amazon.ChargeFeeTo chargeFeeTo,
           java.lang.String callerReference,
           java.lang.String senderReference,
           java.lang.String recipientReference,
           java.lang.String callerDescription,
           java.lang.String senderDescription,
           java.lang.String recipientDescription,
           java.lang.String metaData,
           com.easyinsight.amazon.Amount marketplaceFixedFee,
           java.math.BigDecimal marketplaceVariableFee) {
           this.callerTokenId = callerTokenId;
           this.senderTokenId = senderTokenId;
           this.recipientTokenId = recipientTokenId;
           this.transactionAmount = transactionAmount;
           this.transactionDate = transactionDate;
           this.chargeFeeTo = chargeFeeTo;
           this.callerReference = callerReference;
           this.senderReference = senderReference;
           this.recipientReference = recipientReference;
           this.callerDescription = callerDescription;
           this.senderDescription = senderDescription;
           this.recipientDescription = recipientDescription;
           this.metaData = metaData;
           this.marketplaceFixedFee = marketplaceFixedFee;
           this.marketplaceVariableFee = marketplaceVariableFee;
    }


    /**
     * Gets the callerTokenId value for this ReserveRequest.
     * 
     * @return callerTokenId
     */
    public java.lang.String getCallerTokenId() {
        return callerTokenId;
    }


    /**
     * Sets the callerTokenId value for this ReserveRequest.
     * 
     * @param callerTokenId
     */
    public void setCallerTokenId(java.lang.String callerTokenId) {
        this.callerTokenId = callerTokenId;
    }


    /**
     * Gets the senderTokenId value for this ReserveRequest.
     * 
     * @return senderTokenId
     */
    public java.lang.String getSenderTokenId() {
        return senderTokenId;
    }


    /**
     * Sets the senderTokenId value for this ReserveRequest.
     * 
     * @param senderTokenId
     */
    public void setSenderTokenId(java.lang.String senderTokenId) {
        this.senderTokenId = senderTokenId;
    }


    /**
     * Gets the recipientTokenId value for this ReserveRequest.
     * 
     * @return recipientTokenId
     */
    public java.lang.String getRecipientTokenId() {
        return recipientTokenId;
    }


    /**
     * Sets the recipientTokenId value for this ReserveRequest.
     * 
     * @param recipientTokenId
     */
    public void setRecipientTokenId(java.lang.String recipientTokenId) {
        this.recipientTokenId = recipientTokenId;
    }


    /**
     * Gets the transactionAmount value for this ReserveRequest.
     * 
     * @return transactionAmount
     */
    public com.easyinsight.amazon.Amount getTransactionAmount() {
        return transactionAmount;
    }


    /**
     * Sets the transactionAmount value for this ReserveRequest.
     * 
     * @param transactionAmount
     */
    public void setTransactionAmount(com.easyinsight.amazon.Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    /**
     * Gets the transactionDate value for this ReserveRequest.
     * 
     * @return transactionDate
     */
    public java.util.Calendar getTransactionDate() {
        return transactionDate;
    }


    /**
     * Sets the transactionDate value for this ReserveRequest.
     * 
     * @param transactionDate
     */
    public void setTransactionDate(java.util.Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }


    /**
     * Gets the chargeFeeTo value for this ReserveRequest.
     * 
     * @return chargeFeeTo
     */
    public com.easyinsight.amazon.ChargeFeeTo getChargeFeeTo() {
        return chargeFeeTo;
    }


    /**
     * Sets the chargeFeeTo value for this ReserveRequest.
     * 
     * @param chargeFeeTo
     */
    public void setChargeFeeTo(com.easyinsight.amazon.ChargeFeeTo chargeFeeTo) {
        this.chargeFeeTo = chargeFeeTo;
    }


    /**
     * Gets the callerReference value for this ReserveRequest.
     * 
     * @return callerReference
     */
    public java.lang.String getCallerReference() {
        return callerReference;
    }


    /**
     * Sets the callerReference value for this ReserveRequest.
     * 
     * @param callerReference
     */
    public void setCallerReference(java.lang.String callerReference) {
        this.callerReference = callerReference;
    }


    /**
     * Gets the senderReference value for this ReserveRequest.
     * 
     * @return senderReference
     */
    public java.lang.String getSenderReference() {
        return senderReference;
    }


    /**
     * Sets the senderReference value for this ReserveRequest.
     * 
     * @param senderReference
     */
    public void setSenderReference(java.lang.String senderReference) {
        this.senderReference = senderReference;
    }


    /**
     * Gets the recipientReference value for this ReserveRequest.
     * 
     * @return recipientReference
     */
    public java.lang.String getRecipientReference() {
        return recipientReference;
    }


    /**
     * Sets the recipientReference value for this ReserveRequest.
     * 
     * @param recipientReference
     */
    public void setRecipientReference(java.lang.String recipientReference) {
        this.recipientReference = recipientReference;
    }


    /**
     * Gets the callerDescription value for this ReserveRequest.
     * 
     * @return callerDescription
     */
    public java.lang.String getCallerDescription() {
        return callerDescription;
    }


    /**
     * Sets the callerDescription value for this ReserveRequest.
     * 
     * @param callerDescription
     */
    public void setCallerDescription(java.lang.String callerDescription) {
        this.callerDescription = callerDescription;
    }


    /**
     * Gets the senderDescription value for this ReserveRequest.
     * 
     * @return senderDescription
     */
    public java.lang.String getSenderDescription() {
        return senderDescription;
    }


    /**
     * Sets the senderDescription value for this ReserveRequest.
     * 
     * @param senderDescription
     */
    public void setSenderDescription(java.lang.String senderDescription) {
        this.senderDescription = senderDescription;
    }


    /**
     * Gets the recipientDescription value for this ReserveRequest.
     * 
     * @return recipientDescription
     */
    public java.lang.String getRecipientDescription() {
        return recipientDescription;
    }


    /**
     * Sets the recipientDescription value for this ReserveRequest.
     * 
     * @param recipientDescription
     */
    public void setRecipientDescription(java.lang.String recipientDescription) {
        this.recipientDescription = recipientDescription;
    }


    /**
     * Gets the metaData value for this ReserveRequest.
     * 
     * @return metaData
     */
    public java.lang.String getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this ReserveRequest.
     * 
     * @param metaData
     */
    public void setMetaData(java.lang.String metaData) {
        this.metaData = metaData;
    }


    /**
     * Gets the marketplaceFixedFee value for this ReserveRequest.
     * 
     * @return marketplaceFixedFee
     */
    public com.easyinsight.amazon.Amount getMarketplaceFixedFee() {
        return marketplaceFixedFee;
    }


    /**
     * Sets the marketplaceFixedFee value for this ReserveRequest.
     * 
     * @param marketplaceFixedFee
     */
    public void setMarketplaceFixedFee(com.easyinsight.amazon.Amount marketplaceFixedFee) {
        this.marketplaceFixedFee = marketplaceFixedFee;
    }


    /**
     * Gets the marketplaceVariableFee value for this ReserveRequest.
     * 
     * @return marketplaceVariableFee
     */
    public java.math.BigDecimal getMarketplaceVariableFee() {
        return marketplaceVariableFee;
    }


    /**
     * Sets the marketplaceVariableFee value for this ReserveRequest.
     * 
     * @param marketplaceVariableFee
     */
    public void setMarketplaceVariableFee(java.math.BigDecimal marketplaceVariableFee) {
        this.marketplaceVariableFee = marketplaceVariableFee;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReserveRequest)) return false;
        ReserveRequest other = (ReserveRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.callerTokenId==null && other.getCallerTokenId()==null) || 
             (this.callerTokenId!=null &&
              this.callerTokenId.equals(other.getCallerTokenId()))) &&
            ((this.senderTokenId==null && other.getSenderTokenId()==null) || 
             (this.senderTokenId!=null &&
              this.senderTokenId.equals(other.getSenderTokenId()))) &&
            ((this.recipientTokenId==null && other.getRecipientTokenId()==null) || 
             (this.recipientTokenId!=null &&
              this.recipientTokenId.equals(other.getRecipientTokenId()))) &&
            ((this.transactionAmount==null && other.getTransactionAmount()==null) || 
             (this.transactionAmount!=null &&
              this.transactionAmount.equals(other.getTransactionAmount()))) &&
            ((this.transactionDate==null && other.getTransactionDate()==null) || 
             (this.transactionDate!=null &&
              this.transactionDate.equals(other.getTransactionDate()))) &&
            ((this.chargeFeeTo==null && other.getChargeFeeTo()==null) || 
             (this.chargeFeeTo!=null &&
              this.chargeFeeTo.equals(other.getChargeFeeTo()))) &&
            ((this.callerReference==null && other.getCallerReference()==null) || 
             (this.callerReference!=null &&
              this.callerReference.equals(other.getCallerReference()))) &&
            ((this.senderReference==null && other.getSenderReference()==null) || 
             (this.senderReference!=null &&
              this.senderReference.equals(other.getSenderReference()))) &&
            ((this.recipientReference==null && other.getRecipientReference()==null) || 
             (this.recipientReference!=null &&
              this.recipientReference.equals(other.getRecipientReference()))) &&
            ((this.callerDescription==null && other.getCallerDescription()==null) || 
             (this.callerDescription!=null &&
              this.callerDescription.equals(other.getCallerDescription()))) &&
            ((this.senderDescription==null && other.getSenderDescription()==null) || 
             (this.senderDescription!=null &&
              this.senderDescription.equals(other.getSenderDescription()))) &&
            ((this.recipientDescription==null && other.getRecipientDescription()==null) || 
             (this.recipientDescription!=null &&
              this.recipientDescription.equals(other.getRecipientDescription()))) &&
            ((this.metaData==null && other.getMetaData()==null) || 
             (this.metaData!=null &&
              this.metaData.equals(other.getMetaData()))) &&
            ((this.marketplaceFixedFee==null && other.getMarketplaceFixedFee()==null) || 
             (this.marketplaceFixedFee!=null &&
              this.marketplaceFixedFee.equals(other.getMarketplaceFixedFee()))) &&
            ((this.marketplaceVariableFee==null && other.getMarketplaceVariableFee()==null) || 
             (this.marketplaceVariableFee!=null &&
              this.marketplaceVariableFee.equals(other.getMarketplaceVariableFee())));
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
        if (getCallerTokenId() != null) {
            _hashCode += getCallerTokenId().hashCode();
        }
        if (getSenderTokenId() != null) {
            _hashCode += getSenderTokenId().hashCode();
        }
        if (getRecipientTokenId() != null) {
            _hashCode += getRecipientTokenId().hashCode();
        }
        if (getTransactionAmount() != null) {
            _hashCode += getTransactionAmount().hashCode();
        }
        if (getTransactionDate() != null) {
            _hashCode += getTransactionDate().hashCode();
        }
        if (getChargeFeeTo() != null) {
            _hashCode += getChargeFeeTo().hashCode();
        }
        if (getCallerReference() != null) {
            _hashCode += getCallerReference().hashCode();
        }
        if (getSenderReference() != null) {
            _hashCode += getSenderReference().hashCode();
        }
        if (getRecipientReference() != null) {
            _hashCode += getRecipientReference().hashCode();
        }
        if (getCallerDescription() != null) {
            _hashCode += getCallerDescription().hashCode();
        }
        if (getSenderDescription() != null) {
            _hashCode += getSenderDescription().hashCode();
        }
        if (getRecipientDescription() != null) {
            _hashCode += getRecipientDescription().hashCode();
        }
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        if (getMarketplaceFixedFee() != null) {
            _hashCode += getMarketplaceFixedFee().hashCode();
        }
        if (getMarketplaceVariableFee() != null) {
            _hashCode += getMarketplaceVariableFee().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReserveRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">ReserveRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SenderTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RecipientTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionAmount"));
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
        elemField.setFieldName("chargeFeeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ChargeFeeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ChargeFeeTo"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("callerDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("metaData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MetaData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marketplaceFixedFee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MarketplaceFixedFee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marketplaceVariableFee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MarketplaceVariableFee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
