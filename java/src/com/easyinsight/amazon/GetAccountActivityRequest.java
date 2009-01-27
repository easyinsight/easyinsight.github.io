/**
 * GetAccountActivityRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class GetAccountActivityRequest  implements java.io.Serializable {
    private com.easyinsight.amazon.FPSOperationFilter operation;

    private com.easyinsight.amazon.PaymentMethod paymentMethod;

    private java.math.BigInteger maxBatchSize;

    private java.util.Calendar startDate;

    private java.util.Calendar endDate;

    private com.easyinsight.amazon.GetAccountActivityResponseGroup responseGroup;

    private com.easyinsight.amazon.SortOrder sortOrderByDate;

    private com.easyinsight.amazon.TransactionalRoleFilter[] role;

    private com.easyinsight.amazon.TransactionStatusFilter status;

    public GetAccountActivityRequest() {
    }

    public GetAccountActivityRequest(
           com.easyinsight.amazon.FPSOperationFilter operation,
           com.easyinsight.amazon.PaymentMethod paymentMethod,
           java.math.BigInteger maxBatchSize,
           java.util.Calendar startDate,
           java.util.Calendar endDate,
           com.easyinsight.amazon.GetAccountActivityResponseGroup responseGroup,
           com.easyinsight.amazon.SortOrder sortOrderByDate,
           com.easyinsight.amazon.TransactionalRoleFilter[] role,
           com.easyinsight.amazon.TransactionStatusFilter status) {
           this.operation = operation;
           this.paymentMethod = paymentMethod;
           this.maxBatchSize = maxBatchSize;
           this.startDate = startDate;
           this.endDate = endDate;
           this.responseGroup = responseGroup;
           this.sortOrderByDate = sortOrderByDate;
           this.role = role;
           this.status = status;
    }


    /**
     * Gets the operation value for this GetAccountActivityRequest.
     * 
     * @return operation
     */
    public com.easyinsight.amazon.FPSOperationFilter getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this GetAccountActivityRequest.
     * 
     * @param operation
     */
    public void setOperation(com.easyinsight.amazon.FPSOperationFilter operation) {
        this.operation = operation;
    }


    /**
     * Gets the paymentMethod value for this GetAccountActivityRequest.
     * 
     * @return paymentMethod
     */
    public com.easyinsight.amazon.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    /**
     * Sets the paymentMethod value for this GetAccountActivityRequest.
     * 
     * @param paymentMethod
     */
    public void setPaymentMethod(com.easyinsight.amazon.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    /**
     * Gets the maxBatchSize value for this GetAccountActivityRequest.
     * 
     * @return maxBatchSize
     */
    public java.math.BigInteger getMaxBatchSize() {
        return maxBatchSize;
    }


    /**
     * Sets the maxBatchSize value for this GetAccountActivityRequest.
     * 
     * @param maxBatchSize
     */
    public void setMaxBatchSize(java.math.BigInteger maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }


    /**
     * Gets the startDate value for this GetAccountActivityRequest.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this GetAccountActivityRequest.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the endDate value for this GetAccountActivityRequest.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this GetAccountActivityRequest.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the responseGroup value for this GetAccountActivityRequest.
     * 
     * @return responseGroup
     */
    public com.easyinsight.amazon.GetAccountActivityResponseGroup getResponseGroup() {
        return responseGroup;
    }


    /**
     * Sets the responseGroup value for this GetAccountActivityRequest.
     * 
     * @param responseGroup
     */
    public void setResponseGroup(com.easyinsight.amazon.GetAccountActivityResponseGroup responseGroup) {
        this.responseGroup = responseGroup;
    }


    /**
     * Gets the sortOrderByDate value for this GetAccountActivityRequest.
     * 
     * @return sortOrderByDate
     */
    public com.easyinsight.amazon.SortOrder getSortOrderByDate() {
        return sortOrderByDate;
    }


    /**
     * Sets the sortOrderByDate value for this GetAccountActivityRequest.
     * 
     * @param sortOrderByDate
     */
    public void setSortOrderByDate(com.easyinsight.amazon.SortOrder sortOrderByDate) {
        this.sortOrderByDate = sortOrderByDate;
    }


    /**
     * Gets the role value for this GetAccountActivityRequest.
     * 
     * @return role
     */
    public com.easyinsight.amazon.TransactionalRoleFilter[] getRole() {
        return role;
    }


    /**
     * Sets the role value for this GetAccountActivityRequest.
     * 
     * @param role
     */
    public void setRole(com.easyinsight.amazon.TransactionalRoleFilter[] role) {
        this.role = role;
    }

    public com.easyinsight.amazon.TransactionalRoleFilter getRole(int i) {
        return this.role[i];
    }

    public void setRole(int i, com.easyinsight.amazon.TransactionalRoleFilter _value) {
        this.role[i] = _value;
    }


    /**
     * Gets the status value for this GetAccountActivityRequest.
     * 
     * @return status
     */
    public com.easyinsight.amazon.TransactionStatusFilter getStatus() {
        return status;
    }


    /**
     * Sets the status value for this GetAccountActivityRequest.
     * 
     * @param status
     */
    public void setStatus(com.easyinsight.amazon.TransactionStatusFilter status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAccountActivityRequest)) return false;
        GetAccountActivityRequest other = (GetAccountActivityRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            ((this.paymentMethod==null && other.getPaymentMethod()==null) || 
             (this.paymentMethod!=null &&
              this.paymentMethod.equals(other.getPaymentMethod()))) &&
            ((this.maxBatchSize==null && other.getMaxBatchSize()==null) || 
             (this.maxBatchSize!=null &&
              this.maxBatchSize.equals(other.getMaxBatchSize()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.responseGroup==null && other.getResponseGroup()==null) || 
             (this.responseGroup!=null &&
              this.responseGroup.equals(other.getResponseGroup()))) &&
            ((this.sortOrderByDate==null && other.getSortOrderByDate()==null) || 
             (this.sortOrderByDate!=null &&
              this.sortOrderByDate.equals(other.getSortOrderByDate()))) &&
            ((this.role==null && other.getRole()==null) || 
             (this.role!=null &&
              java.util.Arrays.equals(this.role, other.getRole()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        if (getPaymentMethod() != null) {
            _hashCode += getPaymentMethod().hashCode();
        }
        if (getMaxBatchSize() != null) {
            _hashCode += getMaxBatchSize().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getResponseGroup() != null) {
            _hashCode += getResponseGroup().hashCode();
        }
        if (getSortOrderByDate() != null) {
            _hashCode += getSortOrderByDate().hashCode();
        }
        if (getRole() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRole());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRole(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAccountActivityRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountActivityRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperationFilter"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PaymentMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PaymentMethod"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxBatchSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MaxBatchSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ResponseGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAccountActivityResponseGroup"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sortOrderByDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SortOrderByDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SortOrder"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("role");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Role"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionalRoleFilter"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatusFilter"));
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
