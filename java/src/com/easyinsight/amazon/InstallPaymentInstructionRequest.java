/**
 * InstallPaymentInstructionRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class InstallPaymentInstructionRequest  implements java.io.Serializable {
    private java.lang.String paymentInstruction;

    private java.lang.String tokenFriendlyName;

    private java.lang.String callerReference;

    private com.easyinsight.amazon.TokenType tokenType;

    private java.lang.String paymentReason;

    public InstallPaymentInstructionRequest() {
    }

    public InstallPaymentInstructionRequest(
           java.lang.String paymentInstruction,
           java.lang.String tokenFriendlyName,
           java.lang.String callerReference,
           com.easyinsight.amazon.TokenType tokenType,
           java.lang.String paymentReason) {
           this.paymentInstruction = paymentInstruction;
           this.tokenFriendlyName = tokenFriendlyName;
           this.callerReference = callerReference;
           this.tokenType = tokenType;
           this.paymentReason = paymentReason;
    }


    /**
     * Gets the paymentInstruction value for this InstallPaymentInstructionRequest.
     * 
     * @return paymentInstruction
     */
    public java.lang.String getPaymentInstruction() {
        return paymentInstruction;
    }


    /**
     * Sets the paymentInstruction value for this InstallPaymentInstructionRequest.
     * 
     * @param paymentInstruction
     */
    public void setPaymentInstruction(java.lang.String paymentInstruction) {
        this.paymentInstruction = paymentInstruction;
    }


    /**
     * Gets the tokenFriendlyName value for this InstallPaymentInstructionRequest.
     * 
     * @return tokenFriendlyName
     */
    public java.lang.String getTokenFriendlyName() {
        return tokenFriendlyName;
    }


    /**
     * Sets the tokenFriendlyName value for this InstallPaymentInstructionRequest.
     * 
     * @param tokenFriendlyName
     */
    public void setTokenFriendlyName(java.lang.String tokenFriendlyName) {
        this.tokenFriendlyName = tokenFriendlyName;
    }


    /**
     * Gets the callerReference value for this InstallPaymentInstructionRequest.
     * 
     * @return callerReference
     */
    public java.lang.String getCallerReference() {
        return callerReference;
    }


    /**
     * Sets the callerReference value for this InstallPaymentInstructionRequest.
     * 
     * @param callerReference
     */
    public void setCallerReference(java.lang.String callerReference) {
        this.callerReference = callerReference;
    }


    /**
     * Gets the tokenType value for this InstallPaymentInstructionRequest.
     * 
     * @return tokenType
     */
    public com.easyinsight.amazon.TokenType getTokenType() {
        return tokenType;
    }


    /**
     * Sets the tokenType value for this InstallPaymentInstructionRequest.
     * 
     * @param tokenType
     */
    public void setTokenType(com.easyinsight.amazon.TokenType tokenType) {
        this.tokenType = tokenType;
    }


    /**
     * Gets the paymentReason value for this InstallPaymentInstructionRequest.
     * 
     * @return paymentReason
     */
    public java.lang.String getPaymentReason() {
        return paymentReason;
    }


    /**
     * Sets the paymentReason value for this InstallPaymentInstructionRequest.
     * 
     * @param paymentReason
     */
    public void setPaymentReason(java.lang.String paymentReason) {
        this.paymentReason = paymentReason;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InstallPaymentInstructionRequest)) return false;
        InstallPaymentInstructionRequest other = (InstallPaymentInstructionRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.paymentInstruction==null && other.getPaymentInstruction()==null) || 
             (this.paymentInstruction!=null &&
              this.paymentInstruction.equals(other.getPaymentInstruction()))) &&
            ((this.tokenFriendlyName==null && other.getTokenFriendlyName()==null) || 
             (this.tokenFriendlyName!=null &&
              this.tokenFriendlyName.equals(other.getTokenFriendlyName()))) &&
            ((this.callerReference==null && other.getCallerReference()==null) || 
             (this.callerReference!=null &&
              this.callerReference.equals(other.getCallerReference()))) &&
            ((this.tokenType==null && other.getTokenType()==null) || 
             (this.tokenType!=null &&
              this.tokenType.equals(other.getTokenType()))) &&
            ((this.paymentReason==null && other.getPaymentReason()==null) || 
             (this.paymentReason!=null &&
              this.paymentReason.equals(other.getPaymentReason())));
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
        if (getPaymentInstruction() != null) {
            _hashCode += getPaymentInstruction().hashCode();
        }
        if (getTokenFriendlyName() != null) {
            _hashCode += getTokenFriendlyName().hashCode();
        }
        if (getCallerReference() != null) {
            _hashCode += getCallerReference().hashCode();
        }
        if (getTokenType() != null) {
            _hashCode += getTokenType().hashCode();
        }
        if (getPaymentReason() != null) {
            _hashCode += getPaymentReason().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InstallPaymentInstructionRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentInstruction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PaymentInstruction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenFriendlyName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenFriendlyName"));
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
        elemField.setFieldName("tokenType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentReason");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PaymentReason"));
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
