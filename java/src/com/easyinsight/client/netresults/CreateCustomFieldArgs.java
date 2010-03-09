/**
 * CreateCustomFieldArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class CreateCustomFieldArgs  implements java.io.Serializable {
    /* The name of the CustomField. */
    private java.lang.String custom_field_name;

    /* The Role ID of the CustomField. */
    private java.math.BigInteger role_id;

    public CreateCustomFieldArgs() {
    }

    public CreateCustomFieldArgs(
           java.lang.String custom_field_name,
           java.math.BigInteger role_id) {
           this.custom_field_name = custom_field_name;
           this.role_id = role_id;
    }


    /**
     * Gets the custom_field_name value for this CreateCustomFieldArgs.
     * 
     * @return custom_field_name   * The name of the CustomField.
     */
    public java.lang.String getCustom_field_name() {
        return custom_field_name;
    }


    /**
     * Sets the custom_field_name value for this CreateCustomFieldArgs.
     * 
     * @param custom_field_name   * The name of the CustomField.
     */
    public void setCustom_field_name(java.lang.String custom_field_name) {
        this.custom_field_name = custom_field_name;
    }


    /**
     * Gets the role_id value for this CreateCustomFieldArgs.
     * 
     * @return role_id   * The Role ID of the CustomField.
     */
    public java.math.BigInteger getRole_id() {
        return role_id;
    }


    /**
     * Sets the role_id value for this CreateCustomFieldArgs.
     * 
     * @param role_id   * The Role ID of the CustomField.
     */
    public void setRole_id(java.math.BigInteger role_id) {
        this.role_id = role_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateCustomFieldArgs)) return false;
        CreateCustomFieldArgs other = (CreateCustomFieldArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.custom_field_name==null && other.getCustom_field_name()==null) || 
             (this.custom_field_name!=null &&
              this.custom_field_name.equals(other.getCustom_field_name()))) &&
            ((this.role_id==null && other.getRole_id()==null) || 
             (this.role_id!=null &&
              this.role_id.equals(other.getRole_id())));
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
        if (getCustom_field_name() != null) {
            _hashCode += getCustom_field_name().hashCode();
        }
        if (getRole_id() != null) {
            _hashCode += getRole_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateCustomFieldArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "createCustomFieldArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("custom_field_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "custom_field_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("role_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "role_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(true);
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
