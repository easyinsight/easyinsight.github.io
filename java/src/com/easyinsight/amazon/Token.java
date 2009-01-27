/**
 * Token.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class Token  implements java.io.Serializable {
    private java.lang.String tokenId;

    private java.lang.String friendlyName;

    private com.easyinsight.amazon.TokenStatus status;

    private java.util.Calendar dateInstalled;

    private java.lang.String callerInstalled;

    private java.lang.String callerReference;

    private com.easyinsight.amazon.TokenType tokenType;

    private java.lang.String oldTokenId;

    private java.lang.String paymentReason;

    public Token() {
    }

    public Token(
           java.lang.String tokenId,
           java.lang.String friendlyName,
           com.easyinsight.amazon.TokenStatus status,
           java.util.Calendar dateInstalled,
           java.lang.String callerInstalled,
           java.lang.String callerReference,
           com.easyinsight.amazon.TokenType tokenType,
           java.lang.String oldTokenId,
           java.lang.String paymentReason) {
           this.tokenId = tokenId;
           this.friendlyName = friendlyName;
           this.status = status;
           this.dateInstalled = dateInstalled;
           this.callerInstalled = callerInstalled;
           this.callerReference = callerReference;
           this.tokenType = tokenType;
           this.oldTokenId = oldTokenId;
           this.paymentReason = paymentReason;
    }


    /**
     * Gets the tokenId value for this Token.
     * 
     * @return tokenId
     */
    public java.lang.String getTokenId() {
        return tokenId;
    }


    /**
     * Sets the tokenId value for this Token.
     * 
     * @param tokenId
     */
    public void setTokenId(java.lang.String tokenId) {
        this.tokenId = tokenId;
    }


    /**
     * Gets the friendlyName value for this Token.
     * 
     * @return friendlyName
     */
    public java.lang.String getFriendlyName() {
        return friendlyName;
    }


    /**
     * Sets the friendlyName value for this Token.
     * 
     * @param friendlyName
     */
    public void setFriendlyName(java.lang.String friendlyName) {
        this.friendlyName = friendlyName;
    }


    /**
     * Gets the status value for this Token.
     * 
     * @return status
     */
    public com.easyinsight.amazon.TokenStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Token.
     * 
     * @param status
     */
    public void setStatus(com.easyinsight.amazon.TokenStatus status) {
        this.status = status;
    }


    /**
     * Gets the dateInstalled value for this Token.
     * 
     * @return dateInstalled
     */
    public java.util.Calendar getDateInstalled() {
        return dateInstalled;
    }


    /**
     * Sets the dateInstalled value for this Token.
     * 
     * @param dateInstalled
     */
    public void setDateInstalled(java.util.Calendar dateInstalled) {
        this.dateInstalled = dateInstalled;
    }


    /**
     * Gets the callerInstalled value for this Token.
     * 
     * @return callerInstalled
     */
    public java.lang.String getCallerInstalled() {
        return callerInstalled;
    }


    /**
     * Sets the callerInstalled value for this Token.
     * 
     * @param callerInstalled
     */
    public void setCallerInstalled(java.lang.String callerInstalled) {
        this.callerInstalled = callerInstalled;
    }


    /**
     * Gets the callerReference value for this Token.
     * 
     * @return callerReference
     */
    public java.lang.String getCallerReference() {
        return callerReference;
    }


    /**
     * Sets the callerReference value for this Token.
     * 
     * @param callerReference
     */
    public void setCallerReference(java.lang.String callerReference) {
        this.callerReference = callerReference;
    }


    /**
     * Gets the tokenType value for this Token.
     * 
     * @return tokenType
     */
    public com.easyinsight.amazon.TokenType getTokenType() {
        return tokenType;
    }


    /**
     * Sets the tokenType value for this Token.
     * 
     * @param tokenType
     */
    public void setTokenType(com.easyinsight.amazon.TokenType tokenType) {
        this.tokenType = tokenType;
    }


    /**
     * Gets the oldTokenId value for this Token.
     * 
     * @return oldTokenId
     */
    public java.lang.String getOldTokenId() {
        return oldTokenId;
    }


    /**
     * Sets the oldTokenId value for this Token.
     * 
     * @param oldTokenId
     */
    public void setOldTokenId(java.lang.String oldTokenId) {
        this.oldTokenId = oldTokenId;
    }


    /**
     * Gets the paymentReason value for this Token.
     * 
     * @return paymentReason
     */
    public java.lang.String getPaymentReason() {
        return paymentReason;
    }


    /**
     * Sets the paymentReason value for this Token.
     * 
     * @param paymentReason
     */
    public void setPaymentReason(java.lang.String paymentReason) {
        this.paymentReason = paymentReason;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Token)) return false;
        Token other = (Token) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tokenId==null && other.getTokenId()==null) || 
             (this.tokenId!=null &&
              this.tokenId.equals(other.getTokenId()))) &&
            ((this.friendlyName==null && other.getFriendlyName()==null) || 
             (this.friendlyName!=null &&
              this.friendlyName.equals(other.getFriendlyName()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.dateInstalled==null && other.getDateInstalled()==null) || 
             (this.dateInstalled!=null &&
              this.dateInstalled.equals(other.getDateInstalled()))) &&
            ((this.callerInstalled==null && other.getCallerInstalled()==null) || 
             (this.callerInstalled!=null &&
              this.callerInstalled.equals(other.getCallerInstalled()))) &&
            ((this.callerReference==null && other.getCallerReference()==null) || 
             (this.callerReference!=null &&
              this.callerReference.equals(other.getCallerReference()))) &&
            ((this.tokenType==null && other.getTokenType()==null) || 
             (this.tokenType!=null &&
              this.tokenType.equals(other.getTokenType()))) &&
            ((this.oldTokenId==null && other.getOldTokenId()==null) || 
             (this.oldTokenId!=null &&
              this.oldTokenId.equals(other.getOldTokenId()))) &&
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
        if (getTokenId() != null) {
            _hashCode += getTokenId().hashCode();
        }
        if (getFriendlyName() != null) {
            _hashCode += getFriendlyName().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getDateInstalled() != null) {
            _hashCode += getDateInstalled().hashCode();
        }
        if (getCallerInstalled() != null) {
            _hashCode += getCallerInstalled().hashCode();
        }
        if (getCallerReference() != null) {
            _hashCode += getCallerReference().hashCode();
        }
        if (getTokenType() != null) {
            _hashCode += getTokenType().hashCode();
        }
        if (getOldTokenId() != null) {
            _hashCode += getOldTokenId().hashCode();
        }
        if (getPaymentReason() != null) {
            _hashCode += getPaymentReason().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Token.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Token"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("friendlyName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FriendlyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateInstalled");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DateInstalled"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerInstalled");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerInstalled"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("oldTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OldTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
