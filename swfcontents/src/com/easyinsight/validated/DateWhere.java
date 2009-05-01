/**
 * DateWhere.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.validated;

public class DateWhere  implements java.io.Serializable {
    private com.easyinsight.validated.Comparison comparison;

    private java.lang.String key;

    private java.util.Calendar value;

    public DateWhere() {
    }

    public DateWhere(
           com.easyinsight.validated.Comparison comparison,
           java.lang.String key,
           java.util.Calendar value) {
           this.comparison = comparison;
           this.key = key;
           this.value = value;
    }


    /**
     * Gets the comparison value for this DateWhere.
     * 
     * @return comparison
     */
    public com.easyinsight.validated.Comparison getComparison() {
        return comparison;
    }


    /**
     * Sets the comparison value for this DateWhere.
     * 
     * @param comparison
     */
    public void setComparison(com.easyinsight.validated.Comparison comparison) {
        this.comparison = comparison;
    }


    /**
     * Gets the key value for this DateWhere.
     * 
     * @return key
     */
    public java.lang.String getKey() {
        return key;
    }


    /**
     * Sets the key value for this DateWhere.
     * 
     * @param key
     */
    public void setKey(java.lang.String key) {
        this.key = key;
    }


    /**
     * Gets the value value for this DateWhere.
     * 
     * @return value
     */
    public java.util.Calendar getValue() {
        return value;
    }


    /**
     * Sets the value value for this DateWhere.
     * 
     * @param value
     */
    public void setValue(java.util.Calendar value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DateWhere)) return false;
        DateWhere other = (DateWhere) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.comparison==null && other.getComparison()==null) || 
             (this.comparison!=null &&
              this.comparison.equals(other.getComparison()))) &&
            ((this.key==null && other.getKey()==null) || 
             (this.key!=null &&
              this.key.equals(other.getKey()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
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
        if (getComparison() != null) {
            _hashCode += getComparison().hashCode();
        }
        if (getKey() != null) {
            _hashCode += getKey().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DateWhere.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "dateWhere"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comparison");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comparison"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "comparison"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key");
        elemField.setXmlName(new javax.xml.namespace.QName("", "key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
