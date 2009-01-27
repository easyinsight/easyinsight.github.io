/**
 * TransactionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class TransactionResponse  implements java.io.Serializable {
    private java.lang.String transactionId;

    private com.easyinsight.amazon.TransactionStatus status;

    private java.lang.String statusDetail;

    private com.easyinsight.amazon.TokenUsageLimit[] newSenderTokenUsage;

    public TransactionResponse() {
    }

    public TransactionResponse(
           java.lang.String transactionId,
           com.easyinsight.amazon.TransactionStatus status,
           java.lang.String statusDetail,
           com.easyinsight.amazon.TokenUsageLimit[] newSenderTokenUsage) {
           this.transactionId = transactionId;
           this.status = status;
           this.statusDetail = statusDetail;
           this.newSenderTokenUsage = newSenderTokenUsage;
    }


    /**
     * Gets the transactionId value for this TransactionResponse.
     * 
     * @return transactionId
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this TransactionResponse.
     * 
     * @param transactionId
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * Gets the status value for this TransactionResponse.
     * 
     * @return status
     */
    public com.easyinsight.amazon.TransactionStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this TransactionResponse.
     * 
     * @param status
     */
    public void setStatus(com.easyinsight.amazon.TransactionStatus status) {
        this.status = status;
    }


    /**
     * Gets the statusDetail value for this TransactionResponse.
     * 
     * @return statusDetail
     */
    public java.lang.String getStatusDetail() {
        return statusDetail;
    }


    /**
     * Sets the statusDetail value for this TransactionResponse.
     * 
     * @param statusDetail
     */
    public void setStatusDetail(java.lang.String statusDetail) {
        this.statusDetail = statusDetail;
    }


    /**
     * Gets the newSenderTokenUsage value for this TransactionResponse.
     * 
     * @return newSenderTokenUsage
     */
    public com.easyinsight.amazon.TokenUsageLimit[] getNewSenderTokenUsage() {
        return newSenderTokenUsage;
    }


    /**
     * Sets the newSenderTokenUsage value for this TransactionResponse.
     * 
     * @param newSenderTokenUsage
     */
    public void setNewSenderTokenUsage(com.easyinsight.amazon.TokenUsageLimit[] newSenderTokenUsage) {
        this.newSenderTokenUsage = newSenderTokenUsage;
    }

    public com.easyinsight.amazon.TokenUsageLimit getNewSenderTokenUsage(int i) {
        return this.newSenderTokenUsage[i];
    }

    public void setNewSenderTokenUsage(int i, com.easyinsight.amazon.TokenUsageLimit _value) {
        this.newSenderTokenUsage[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransactionResponse)) return false;
        TransactionResponse other = (TransactionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionId==null && other.getTransactionId()==null) || 
             (this.transactionId!=null &&
              this.transactionId.equals(other.getTransactionId()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.statusDetail==null && other.getStatusDetail()==null) || 
             (this.statusDetail!=null &&
              this.statusDetail.equals(other.getStatusDetail()))) &&
            ((this.newSenderTokenUsage==null && other.getNewSenderTokenUsage()==null) || 
             (this.newSenderTokenUsage!=null &&
              java.util.Arrays.equals(this.newSenderTokenUsage, other.getNewSenderTokenUsage())));
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
        if (getTransactionId() != null) {
            _hashCode += getTransactionId().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getStatusDetail() != null) {
            _hashCode += getStatusDetail().hashCode();
        }
        if (getNewSenderTokenUsage() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewSenderTokenUsage());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNewSenderTokenUsage(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransactionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">TransactionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StatusDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newSenderTokenUsage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NewSenderTokenUsage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenUsageLimit"));
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
