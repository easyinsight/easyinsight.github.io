/**
 * GetResultsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class GetResultsResponse  implements java.io.Serializable {
    private com.easyinsight.amazon.TransactionResult[] transactionResults;

    private java.math.BigInteger numberPending;

    private com.easyinsight.amazon.ResponseStatus status;

    private com.easyinsight.amazon.ServiceError[] errors;

    private java.lang.String requestId;

    public GetResultsResponse() {
    }

    public GetResultsResponse(
           com.easyinsight.amazon.TransactionResult[] transactionResults,
           java.math.BigInteger numberPending,
           com.easyinsight.amazon.ResponseStatus status,
           com.easyinsight.amazon.ServiceError[] errors,
           java.lang.String requestId) {
           this.transactionResults = transactionResults;
           this.numberPending = numberPending;
           this.status = status;
           this.errors = errors;
           this.requestId = requestId;
    }


    /**
     * Gets the transactionResults value for this GetResultsResponse.
     * 
     * @return transactionResults
     */
    public com.easyinsight.amazon.TransactionResult[] getTransactionResults() {
        return transactionResults;
    }


    /**
     * Sets the transactionResults value for this GetResultsResponse.
     * 
     * @param transactionResults
     */
    public void setTransactionResults(com.easyinsight.amazon.TransactionResult[] transactionResults) {
        this.transactionResults = transactionResults;
    }

    public com.easyinsight.amazon.TransactionResult getTransactionResults(int i) {
        return this.transactionResults[i];
    }

    public void setTransactionResults(int i, com.easyinsight.amazon.TransactionResult _value) {
        this.transactionResults[i] = _value;
    }


    /**
     * Gets the numberPending value for this GetResultsResponse.
     * 
     * @return numberPending
     */
    public java.math.BigInteger getNumberPending() {
        return numberPending;
    }


    /**
     * Sets the numberPending value for this GetResultsResponse.
     * 
     * @param numberPending
     */
    public void setNumberPending(java.math.BigInteger numberPending) {
        this.numberPending = numberPending;
    }


    /**
     * Gets the status value for this GetResultsResponse.
     * 
     * @return status
     */
    public com.easyinsight.amazon.ResponseStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this GetResultsResponse.
     * 
     * @param status
     */
    public void setStatus(com.easyinsight.amazon.ResponseStatus status) {
        this.status = status;
    }


    /**
     * Gets the errors value for this GetResultsResponse.
     * 
     * @return errors
     */
    public com.easyinsight.amazon.ServiceError[] getErrors() {
        return errors;
    }


    /**
     * Sets the errors value for this GetResultsResponse.
     * 
     * @param errors
     */
    public void setErrors(com.easyinsight.amazon.ServiceError[] errors) {
        this.errors = errors;
    }


    /**
     * Gets the requestId value for this GetResultsResponse.
     * 
     * @return requestId
     */
    public java.lang.String getRequestId() {
        return requestId;
    }


    /**
     * Sets the requestId value for this GetResultsResponse.
     * 
     * @param requestId
     */
    public void setRequestId(java.lang.String requestId) {
        this.requestId = requestId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetResultsResponse)) return false;
        GetResultsResponse other = (GetResultsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionResults==null && other.getTransactionResults()==null) || 
             (this.transactionResults!=null &&
              java.util.Arrays.equals(this.transactionResults, other.getTransactionResults()))) &&
            ((this.numberPending==null && other.getNumberPending()==null) || 
             (this.numberPending!=null &&
              this.numberPending.equals(other.getNumberPending()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.errors==null && other.getErrors()==null) || 
             (this.errors!=null &&
              java.util.Arrays.equals(this.errors, other.getErrors()))) &&
            ((this.requestId==null && other.getRequestId()==null) || 
             (this.requestId!=null &&
              this.requestId.equals(other.getRequestId())));
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
        if (getTransactionResults() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransactionResults());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransactionResults(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumberPending() != null) {
            _hashCode += getNumberPending().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getErrors() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrors());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getErrors(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRequestId() != null) {
            _hashCode += getRequestId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetResultsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetResultsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionResults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberPending");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumberPending"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ResponseStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errors");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Errors"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ServiceError"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Errors"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RequestId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
