/**
 * GetResultsRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class GetResultsRequest  implements java.io.Serializable {
    private com.easyinsight.amazon.FPSOperation operation;

    private java.math.BigInteger maxResultsCount;

    public GetResultsRequest() {
    }

    public GetResultsRequest(
           com.easyinsight.amazon.FPSOperation operation,
           java.math.BigInteger maxResultsCount) {
           this.operation = operation;
           this.maxResultsCount = maxResultsCount;
    }


    /**
     * Gets the operation value for this GetResultsRequest.
     * 
     * @return operation
     */
    public com.easyinsight.amazon.FPSOperation getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this GetResultsRequest.
     * 
     * @param operation
     */
    public void setOperation(com.easyinsight.amazon.FPSOperation operation) {
        this.operation = operation;
    }


    /**
     * Gets the maxResultsCount value for this GetResultsRequest.
     * 
     * @return maxResultsCount
     */
    public java.math.BigInteger getMaxResultsCount() {
        return maxResultsCount;
    }


    /**
     * Sets the maxResultsCount value for this GetResultsRequest.
     * 
     * @param maxResultsCount
     */
    public void setMaxResultsCount(java.math.BigInteger maxResultsCount) {
        this.maxResultsCount = maxResultsCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetResultsRequest)) return false;
        GetResultsRequest other = (GetResultsRequest) obj;
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
            ((this.maxResultsCount==null && other.getMaxResultsCount()==null) || 
             (this.maxResultsCount!=null &&
              this.maxResultsCount.equals(other.getMaxResultsCount())));
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
        if (getMaxResultsCount() != null) {
            _hashCode += getMaxResultsCount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetResultsRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetResultsRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperation"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxResultsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MaxResultsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
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
