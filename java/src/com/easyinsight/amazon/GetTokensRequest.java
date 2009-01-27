/**
 * GetTokensRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class GetTokensRequest  implements java.io.Serializable {
    private java.lang.String tokenFriendlyName;

    private com.easyinsight.amazon.TokenStatus tokenStatus;

    private java.lang.String callerReference;

    public GetTokensRequest() {
    }

    public GetTokensRequest(
           java.lang.String tokenFriendlyName,
           com.easyinsight.amazon.TokenStatus tokenStatus,
           java.lang.String callerReference) {
           this.tokenFriendlyName = tokenFriendlyName;
           this.tokenStatus = tokenStatus;
           this.callerReference = callerReference;
    }


    /**
     * Gets the tokenFriendlyName value for this GetTokensRequest.
     * 
     * @return tokenFriendlyName
     */
    public java.lang.String getTokenFriendlyName() {
        return tokenFriendlyName;
    }


    /**
     * Sets the tokenFriendlyName value for this GetTokensRequest.
     * 
     * @param tokenFriendlyName
     */
    public void setTokenFriendlyName(java.lang.String tokenFriendlyName) {
        this.tokenFriendlyName = tokenFriendlyName;
    }


    /**
     * Gets the tokenStatus value for this GetTokensRequest.
     * 
     * @return tokenStatus
     */
    public com.easyinsight.amazon.TokenStatus getTokenStatus() {
        return tokenStatus;
    }


    /**
     * Sets the tokenStatus value for this GetTokensRequest.
     * 
     * @param tokenStatus
     */
    public void setTokenStatus(com.easyinsight.amazon.TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }


    /**
     * Gets the callerReference value for this GetTokensRequest.
     * 
     * @return callerReference
     */
    public java.lang.String getCallerReference() {
        return callerReference;
    }


    /**
     * Sets the callerReference value for this GetTokensRequest.
     * 
     * @param callerReference
     */
    public void setCallerReference(java.lang.String callerReference) {
        this.callerReference = callerReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetTokensRequest)) return false;
        GetTokensRequest other = (GetTokensRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tokenFriendlyName==null && other.getTokenFriendlyName()==null) || 
             (this.tokenFriendlyName!=null &&
              this.tokenFriendlyName.equals(other.getTokenFriendlyName()))) &&
            ((this.tokenStatus==null && other.getTokenStatus()==null) || 
             (this.tokenStatus!=null &&
              this.tokenStatus.equals(other.getTokenStatus()))) &&
            ((this.callerReference==null && other.getCallerReference()==null) || 
             (this.callerReference!=null &&
              this.callerReference.equals(other.getCallerReference())));
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
        if (getTokenFriendlyName() != null) {
            _hashCode += getTokenFriendlyName().hashCode();
        }
        if (getTokenStatus() != null) {
            _hashCode += getTokenStatus().hashCode();
        }
        if (getCallerReference() != null) {
            _hashCode += getCallerReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetTokensRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokensRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenFriendlyName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenFriendlyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerReference"));
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
