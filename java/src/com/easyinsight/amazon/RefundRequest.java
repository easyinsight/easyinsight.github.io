/**
 * RefundRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class RefundRequest  implements java.io.Serializable {
    private java.lang.String callerTokenId;

    private java.lang.String refundSenderTokenId;

    private java.lang.String transactionId;

    private com.easyinsight.amazon.Amount refundAmount;

    private com.easyinsight.amazon.ChargeFeeTo chargeFeeTo;

    private java.util.Calendar transactionDate;

    private java.lang.String callerReference;

    private java.lang.String refundSenderReference;

    private java.lang.String refundRecipientReference;

    private java.lang.String callerDescription;

    private java.lang.String refundSenderDescription;

    private java.lang.String refundRecipientDescription;

    private java.lang.String metaData;

    private com.easyinsight.amazon.MarketplaceRefundPolicy marketplaceRefundPolicy;

    public RefundRequest() {
    }

    public RefundRequest(
           java.lang.String callerTokenId,
           java.lang.String refundSenderTokenId,
           java.lang.String transactionId,
           com.easyinsight.amazon.Amount refundAmount,
           com.easyinsight.amazon.ChargeFeeTo chargeFeeTo,
           java.util.Calendar transactionDate,
           java.lang.String callerReference,
           java.lang.String refundSenderReference,
           java.lang.String refundRecipientReference,
           java.lang.String callerDescription,
           java.lang.String refundSenderDescription,
           java.lang.String refundRecipientDescription,
           java.lang.String metaData,
           com.easyinsight.amazon.MarketplaceRefundPolicy marketplaceRefundPolicy) {
           this.callerTokenId = callerTokenId;
           this.refundSenderTokenId = refundSenderTokenId;
           this.transactionId = transactionId;
           this.refundAmount = refundAmount;
           this.chargeFeeTo = chargeFeeTo;
           this.transactionDate = transactionDate;
           this.callerReference = callerReference;
           this.refundSenderReference = refundSenderReference;
           this.refundRecipientReference = refundRecipientReference;
           this.callerDescription = callerDescription;
           this.refundSenderDescription = refundSenderDescription;
           this.refundRecipientDescription = refundRecipientDescription;
           this.metaData = metaData;
           this.marketplaceRefundPolicy = marketplaceRefundPolicy;
    }


    /**
     * Gets the callerTokenId value for this RefundRequest.
     * 
     * @return callerTokenId
     */
    public java.lang.String getCallerTokenId() {
        return callerTokenId;
    }


    /**
     * Sets the callerTokenId value for this RefundRequest.
     * 
     * @param callerTokenId
     */
    public void setCallerTokenId(java.lang.String callerTokenId) {
        this.callerTokenId = callerTokenId;
    }


    /**
     * Gets the refundSenderTokenId value for this RefundRequest.
     * 
     * @return refundSenderTokenId
     */
    public java.lang.String getRefundSenderTokenId() {
        return refundSenderTokenId;
    }


    /**
     * Sets the refundSenderTokenId value for this RefundRequest.
     * 
     * @param refundSenderTokenId
     */
    public void setRefundSenderTokenId(java.lang.String refundSenderTokenId) {
        this.refundSenderTokenId = refundSenderTokenId;
    }


    /**
     * Gets the transactionId value for this RefundRequest.
     * 
     * @return transactionId
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this RefundRequest.
     * 
     * @param transactionId
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * Gets the refundAmount value for this RefundRequest.
     * 
     * @return refundAmount
     */
    public com.easyinsight.amazon.Amount getRefundAmount() {
        return refundAmount;
    }


    /**
     * Sets the refundAmount value for this RefundRequest.
     * 
     * @param refundAmount
     */
    public void setRefundAmount(com.easyinsight.amazon.Amount refundAmount) {
        this.refundAmount = refundAmount;
    }


    /**
     * Gets the chargeFeeTo value for this RefundRequest.
     * 
     * @return chargeFeeTo
     */
    public com.easyinsight.amazon.ChargeFeeTo getChargeFeeTo() {
        return chargeFeeTo;
    }


    /**
     * Sets the chargeFeeTo value for this RefundRequest.
     * 
     * @param chargeFeeTo
     */
    public void setChargeFeeTo(com.easyinsight.amazon.ChargeFeeTo chargeFeeTo) {
        this.chargeFeeTo = chargeFeeTo;
    }


    /**
     * Gets the transactionDate value for this RefundRequest.
     * 
     * @return transactionDate
     */
    public java.util.Calendar getTransactionDate() {
        return transactionDate;
    }


    /**
     * Sets the transactionDate value for this RefundRequest.
     * 
     * @param transactionDate
     */
    public void setTransactionDate(java.util.Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }


    /**
     * Gets the callerReference value for this RefundRequest.
     * 
     * @return callerReference
     */
    public java.lang.String getCallerReference() {
        return callerReference;
    }


    /**
     * Sets the callerReference value for this RefundRequest.
     * 
     * @param callerReference
     */
    public void setCallerReference(java.lang.String callerReference) {
        this.callerReference = callerReference;
    }


    /**
     * Gets the refundSenderReference value for this RefundRequest.
     * 
     * @return refundSenderReference
     */
    public java.lang.String getRefundSenderReference() {
        return refundSenderReference;
    }


    /**
     * Sets the refundSenderReference value for this RefundRequest.
     * 
     * @param refundSenderReference
     */
    public void setRefundSenderReference(java.lang.String refundSenderReference) {
        this.refundSenderReference = refundSenderReference;
    }


    /**
     * Gets the refundRecipientReference value for this RefundRequest.
     * 
     * @return refundRecipientReference
     */
    public java.lang.String getRefundRecipientReference() {
        return refundRecipientReference;
    }


    /**
     * Sets the refundRecipientReference value for this RefundRequest.
     * 
     * @param refundRecipientReference
     */
    public void setRefundRecipientReference(java.lang.String refundRecipientReference) {
        this.refundRecipientReference = refundRecipientReference;
    }


    /**
     * Gets the callerDescription value for this RefundRequest.
     * 
     * @return callerDescription
     */
    public java.lang.String getCallerDescription() {
        return callerDescription;
    }


    /**
     * Sets the callerDescription value for this RefundRequest.
     * 
     * @param callerDescription
     */
    public void setCallerDescription(java.lang.String callerDescription) {
        this.callerDescription = callerDescription;
    }


    /**
     * Gets the refundSenderDescription value for this RefundRequest.
     * 
     * @return refundSenderDescription
     */
    public java.lang.String getRefundSenderDescription() {
        return refundSenderDescription;
    }


    /**
     * Sets the refundSenderDescription value for this RefundRequest.
     * 
     * @param refundSenderDescription
     */
    public void setRefundSenderDescription(java.lang.String refundSenderDescription) {
        this.refundSenderDescription = refundSenderDescription;
    }


    /**
     * Gets the refundRecipientDescription value for this RefundRequest.
     * 
     * @return refundRecipientDescription
     */
    public java.lang.String getRefundRecipientDescription() {
        return refundRecipientDescription;
    }


    /**
     * Sets the refundRecipientDescription value for this RefundRequest.
     * 
     * @param refundRecipientDescription
     */
    public void setRefundRecipientDescription(java.lang.String refundRecipientDescription) {
        this.refundRecipientDescription = refundRecipientDescription;
    }


    /**
     * Gets the metaData value for this RefundRequest.
     * 
     * @return metaData
     */
    public java.lang.String getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this RefundRequest.
     * 
     * @param metaData
     */
    public void setMetaData(java.lang.String metaData) {
        this.metaData = metaData;
    }


    /**
     * Gets the marketplaceRefundPolicy value for this RefundRequest.
     * 
     * @return marketplaceRefundPolicy
     */
    public com.easyinsight.amazon.MarketplaceRefundPolicy getMarketplaceRefundPolicy() {
        return marketplaceRefundPolicy;
    }


    /**
     * Sets the marketplaceRefundPolicy value for this RefundRequest.
     * 
     * @param marketplaceRefundPolicy
     */
    public void setMarketplaceRefundPolicy(com.easyinsight.amazon.MarketplaceRefundPolicy marketplaceRefundPolicy) {
        this.marketplaceRefundPolicy = marketplaceRefundPolicy;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RefundRequest)) return false;
        RefundRequest other = (RefundRequest) obj;
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
            ((this.refundSenderTokenId==null && other.getRefundSenderTokenId()==null) || 
             (this.refundSenderTokenId!=null &&
              this.refundSenderTokenId.equals(other.getRefundSenderTokenId()))) &&
            ((this.transactionId==null && other.getTransactionId()==null) || 
             (this.transactionId!=null &&
              this.transactionId.equals(other.getTransactionId()))) &&
            ((this.refundAmount==null && other.getRefundAmount()==null) || 
             (this.refundAmount!=null &&
              this.refundAmount.equals(other.getRefundAmount()))) &&
            ((this.chargeFeeTo==null && other.getChargeFeeTo()==null) || 
             (this.chargeFeeTo!=null &&
              this.chargeFeeTo.equals(other.getChargeFeeTo()))) &&
            ((this.transactionDate==null && other.getTransactionDate()==null) || 
             (this.transactionDate!=null &&
              this.transactionDate.equals(other.getTransactionDate()))) &&
            ((this.callerReference==null && other.getCallerReference()==null) || 
             (this.callerReference!=null &&
              this.callerReference.equals(other.getCallerReference()))) &&
            ((this.refundSenderReference==null && other.getRefundSenderReference()==null) || 
             (this.refundSenderReference!=null &&
              this.refundSenderReference.equals(other.getRefundSenderReference()))) &&
            ((this.refundRecipientReference==null && other.getRefundRecipientReference()==null) || 
             (this.refundRecipientReference!=null &&
              this.refundRecipientReference.equals(other.getRefundRecipientReference()))) &&
            ((this.callerDescription==null && other.getCallerDescription()==null) || 
             (this.callerDescription!=null &&
              this.callerDescription.equals(other.getCallerDescription()))) &&
            ((this.refundSenderDescription==null && other.getRefundSenderDescription()==null) || 
             (this.refundSenderDescription!=null &&
              this.refundSenderDescription.equals(other.getRefundSenderDescription()))) &&
            ((this.refundRecipientDescription==null && other.getRefundRecipientDescription()==null) || 
             (this.refundRecipientDescription!=null &&
              this.refundRecipientDescription.equals(other.getRefundRecipientDescription()))) &&
            ((this.metaData==null && other.getMetaData()==null) || 
             (this.metaData!=null &&
              this.metaData.equals(other.getMetaData()))) &&
            ((this.marketplaceRefundPolicy==null && other.getMarketplaceRefundPolicy()==null) || 
             (this.marketplaceRefundPolicy!=null &&
              this.marketplaceRefundPolicy.equals(other.getMarketplaceRefundPolicy())));
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
        if (getRefundSenderTokenId() != null) {
            _hashCode += getRefundSenderTokenId().hashCode();
        }
        if (getTransactionId() != null) {
            _hashCode += getTransactionId().hashCode();
        }
        if (getRefundAmount() != null) {
            _hashCode += getRefundAmount().hashCode();
        }
        if (getChargeFeeTo() != null) {
            _hashCode += getChargeFeeTo().hashCode();
        }
        if (getTransactionDate() != null) {
            _hashCode += getTransactionDate().hashCode();
        }
        if (getCallerReference() != null) {
            _hashCode += getCallerReference().hashCode();
        }
        if (getRefundSenderReference() != null) {
            _hashCode += getRefundSenderReference().hashCode();
        }
        if (getRefundRecipientReference() != null) {
            _hashCode += getRefundRecipientReference().hashCode();
        }
        if (getCallerDescription() != null) {
            _hashCode += getCallerDescription().hashCode();
        }
        if (getRefundSenderDescription() != null) {
            _hashCode += getRefundSenderDescription().hashCode();
        }
        if (getRefundRecipientDescription() != null) {
            _hashCode += getRefundRecipientDescription().hashCode();
        }
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        if (getMarketplaceRefundPolicy() != null) {
            _hashCode += getMarketplaceRefundPolicy().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RefundRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RefundRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundSenderTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundSenderTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chargeFeeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ChargeFeeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ChargeFeeTo"));
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
        elemField.setFieldName("callerReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundSenderReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundSenderReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundRecipientReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundRecipientReference"));
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
        elemField.setFieldName("refundSenderDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundSenderDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundRecipientDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RefundRecipientDescription"));
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
        elemField.setFieldName("marketplaceRefundPolicy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MarketplaceRefundPolicy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "MarketplaceRefundPolicy"));
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
