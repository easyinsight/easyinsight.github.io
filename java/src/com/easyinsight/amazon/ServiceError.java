/**
 * ServiceError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class ServiceError  implements java.io.Serializable {
    private com.easyinsight.amazon.ErrorType errorType;

    private boolean isRetriable;

    private java.lang.String errorCode;

    private java.lang.String reasonText;

    public ServiceError() {
    }

    public ServiceError(
           com.easyinsight.amazon.ErrorType errorType,
           boolean isRetriable,
           java.lang.String errorCode,
           java.lang.String reasonText) {
           this.errorType = errorType;
           this.isRetriable = isRetriable;
           this.errorCode = errorCode;
           this.reasonText = reasonText;
    }


    /**
     * Gets the errorType value for this ServiceError.
     * 
     * @return errorType
     */
    public com.easyinsight.amazon.ErrorType getErrorType() {
        return errorType;
    }


    /**
     * Sets the errorType value for this ServiceError.
     * 
     * @param errorType
     */
    public void setErrorType(com.easyinsight.amazon.ErrorType errorType) {
        this.errorType = errorType;
    }


    /**
     * Gets the isRetriable value for this ServiceError.
     * 
     * @return isRetriable
     */
    public boolean isIsRetriable() {
        return isRetriable;
    }


    /**
     * Sets the isRetriable value for this ServiceError.
     * 
     * @param isRetriable
     */
    public void setIsRetriable(boolean isRetriable) {
        this.isRetriable = isRetriable;
    }


    /**
     * Gets the errorCode value for this ServiceError.
     * 
     * @return errorCode
     */
    public java.lang.String getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this ServiceError.
     * 
     * @param errorCode
     */
    public void setErrorCode(java.lang.String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the reasonText value for this ServiceError.
     * 
     * @return reasonText
     */
    public java.lang.String getReasonText() {
        return reasonText;
    }


    /**
     * Sets the reasonText value for this ServiceError.
     * 
     * @param reasonText
     */
    public void setReasonText(java.lang.String reasonText) {
        this.reasonText = reasonText;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceError)) return false;
        ServiceError other = (ServiceError) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.errorType==null && other.getErrorType()==null) || 
             (this.errorType!=null &&
              this.errorType.equals(other.getErrorType()))) &&
            this.isRetriable == other.isIsRetriable() &&
            ((this.errorCode==null && other.getErrorCode()==null) || 
             (this.errorCode!=null &&
              this.errorCode.equals(other.getErrorCode()))) &&
            ((this.reasonText==null && other.getReasonText()==null) || 
             (this.reasonText!=null &&
              this.reasonText.equals(other.getReasonText())));
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
        if (getErrorType() != null) {
            _hashCode += getErrorType().hashCode();
        }
        _hashCode += (isIsRetriable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getErrorCode() != null) {
            _hashCode += getErrorCode().hashCode();
        }
        if (getReasonText() != null) {
            _hashCode += getReasonText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceError.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ServiceError"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ErrorType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isRetriable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IsRetriable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reasonText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ReasonText"));
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
