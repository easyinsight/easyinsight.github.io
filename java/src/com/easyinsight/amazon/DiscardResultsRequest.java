/**
 * DiscardResultsRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class DiscardResultsRequest  implements java.io.Serializable {
    private java.lang.String[] transactionIds;

    private java.lang.String unused;

    public DiscardResultsRequest() {
    }

    public DiscardResultsRequest(
           java.lang.String[] transactionIds,
           java.lang.String unused) {
           this.transactionIds = transactionIds;
           this.unused = unused;
    }


    /**
     * Gets the transactionIds value for this DiscardResultsRequest.
     * 
     * @return transactionIds
     */
    public java.lang.String[] getTransactionIds() {
        return transactionIds;
    }


    /**
     * Sets the transactionIds value for this DiscardResultsRequest.
     * 
     * @param transactionIds
     */
    public void setTransactionIds(java.lang.String[] transactionIds) {
        this.transactionIds = transactionIds;
    }

    public java.lang.String getTransactionIds(int i) {
        return this.transactionIds[i];
    }

    public void setTransactionIds(int i, java.lang.String _value) {
        this.transactionIds[i] = _value;
    }


    /**
     * Gets the unused value for this DiscardResultsRequest.
     * 
     * @return unused
     */
    public java.lang.String getUnused() {
        return unused;
    }


    /**
     * Sets the unused value for this DiscardResultsRequest.
     * 
     * @param unused
     */
    public void setUnused(java.lang.String unused) {
        this.unused = unused;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DiscardResultsRequest)) return false;
        DiscardResultsRequest other = (DiscardResultsRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionIds==null && other.getTransactionIds()==null) || 
             (this.transactionIds!=null &&
              java.util.Arrays.equals(this.transactionIds, other.getTransactionIds()))) &&
            ((this.unused==null && other.getUnused()==null) || 
             (this.unused!=null &&
              this.unused.equals(other.getUnused())));
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
        if (getTransactionIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransactionIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransactionIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUnused() != null) {
            _hashCode += getUnused().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DiscardResultsRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">DiscardResultsRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionIds");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unused");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Unused"));
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
