/**
 * Row.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.dbservice.validated;

public class Row  implements java.io.Serializable {
    private com.easyinsight.dbservice.validated.DatePair[] datePairs;

    private com.easyinsight.dbservice.validated.NumberPair[] numberPairs;

    private com.easyinsight.dbservice.validated.StringPair[] stringPairs;

    public Row() {
    }

    public Row(
           com.easyinsight.dbservice.validated.DatePair[] datePairs,
           com.easyinsight.dbservice.validated.NumberPair[] numberPairs,
           com.easyinsight.dbservice.validated.StringPair[] stringPairs) {
           this.datePairs = datePairs;
           this.numberPairs = numberPairs;
           this.stringPairs = stringPairs;
    }


    /**
     * Gets the datePairs value for this Row.
     * 
     * @return datePairs
     */
    public com.easyinsight.dbservice.validated.DatePair[] getDatePairs() {
        return datePairs;
    }


    /**
     * Sets the datePairs value for this Row.
     * 
     * @param datePairs
     */
    public void setDatePairs(com.easyinsight.dbservice.validated.DatePair[] datePairs) {
        this.datePairs = datePairs;
    }

    public com.easyinsight.dbservice.validated.DatePair getDatePairs(int i) {
        return this.datePairs[i];
    }

    public void setDatePairs(int i, com.easyinsight.dbservice.validated.DatePair _value) {
        this.datePairs[i] = _value;
    }


    /**
     * Gets the numberPairs value for this Row.
     * 
     * @return numberPairs
     */
    public com.easyinsight.dbservice.validated.NumberPair[] getNumberPairs() {
        return numberPairs;
    }


    /**
     * Sets the numberPairs value for this Row.
     * 
     * @param numberPairs
     */
    public void setNumberPairs(com.easyinsight.dbservice.validated.NumberPair[] numberPairs) {
        this.numberPairs = numberPairs;
    }

    public com.easyinsight.dbservice.validated.NumberPair getNumberPairs(int i) {
        return this.numberPairs[i];
    }

    public void setNumberPairs(int i, com.easyinsight.dbservice.validated.NumberPair _value) {
        this.numberPairs[i] = _value;
    }


    /**
     * Gets the stringPairs value for this Row.
     * 
     * @return stringPairs
     */
    public com.easyinsight.dbservice.validated.StringPair[] getStringPairs() {
        return stringPairs;
    }


    /**
     * Sets the stringPairs value for this Row.
     * 
     * @param stringPairs
     */
    public void setStringPairs(com.easyinsight.dbservice.validated.StringPair[] stringPairs) {
        this.stringPairs = stringPairs;
    }

    public com.easyinsight.dbservice.validated.StringPair getStringPairs(int i) {
        return this.stringPairs[i];
    }

    public void setStringPairs(int i, com.easyinsight.dbservice.validated.StringPair _value) {
        this.stringPairs[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Row)) return false;
        Row other = (Row) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datePairs==null && other.getDatePairs()==null) || 
             (this.datePairs!=null &&
              java.util.Arrays.equals(this.datePairs, other.getDatePairs()))) &&
            ((this.numberPairs==null && other.getNumberPairs()==null) || 
             (this.numberPairs!=null &&
              java.util.Arrays.equals(this.numberPairs, other.getNumberPairs()))) &&
            ((this.stringPairs==null && other.getStringPairs()==null) || 
             (this.stringPairs!=null &&
              java.util.Arrays.equals(this.stringPairs, other.getStringPairs())));
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
        if (getDatePairs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatePairs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatePairs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumberPairs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNumberPairs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNumberPairs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStringPairs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStringPairs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStringPairs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Row.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "row"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datePairs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datePairs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "datePair"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberPairs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberPairs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "numberPair"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stringPairs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stringPairs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "stringPair"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
