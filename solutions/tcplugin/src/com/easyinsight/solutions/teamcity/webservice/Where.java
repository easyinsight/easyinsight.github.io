/**
 * Where.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.solutions.teamcity.webservice;

public class Where  implements java.io.Serializable {
    private com.easyinsight.solutions.teamcity.webservice.DateWhere[] dateWheres;

    private com.easyinsight.solutions.teamcity.webservice.DayWhere[] dayWheres;

    private com.easyinsight.solutions.teamcity.webservice.NumberWhere[] numberWheres;

    private com.easyinsight.solutions.teamcity.webservice.StringWhere[] stringWheres;

    public Where() {
    }

    public Where(
           com.easyinsight.solutions.teamcity.webservice.DateWhere[] dateWheres,
           com.easyinsight.solutions.teamcity.webservice.DayWhere[] dayWheres,
           com.easyinsight.solutions.teamcity.webservice.NumberWhere[] numberWheres,
           com.easyinsight.solutions.teamcity.webservice.StringWhere[] stringWheres) {
           this.dateWheres = dateWheres;
           this.dayWheres = dayWheres;
           this.numberWheres = numberWheres;
           this.stringWheres = stringWheres;
    }


    /**
     * Gets the dateWheres value for this Where.
     * 
     * @return dateWheres
     */
    public com.easyinsight.solutions.teamcity.webservice.DateWhere[] getDateWheres() {
        return dateWheres;
    }


    /**
     * Sets the dateWheres value for this Where.
     * 
     * @param dateWheres
     */
    public void setDateWheres(com.easyinsight.solutions.teamcity.webservice.DateWhere[] dateWheres) {
        this.dateWheres = dateWheres;
    }

    public com.easyinsight.solutions.teamcity.webservice.DateWhere getDateWheres(int i) {
        return this.dateWheres[i];
    }

    public void setDateWheres(int i, com.easyinsight.solutions.teamcity.webservice.DateWhere _value) {
        this.dateWheres[i] = _value;
    }


    /**
     * Gets the dayWheres value for this Where.
     * 
     * @return dayWheres
     */
    public com.easyinsight.solutions.teamcity.webservice.DayWhere[] getDayWheres() {
        return dayWheres;
    }


    /**
     * Sets the dayWheres value for this Where.
     * 
     * @param dayWheres
     */
    public void setDayWheres(com.easyinsight.solutions.teamcity.webservice.DayWhere[] dayWheres) {
        this.dayWheres = dayWheres;
    }

    public com.easyinsight.solutions.teamcity.webservice.DayWhere getDayWheres(int i) {
        return this.dayWheres[i];
    }

    public void setDayWheres(int i, com.easyinsight.solutions.teamcity.webservice.DayWhere _value) {
        this.dayWheres[i] = _value;
    }


    /**
     * Gets the numberWheres value for this Where.
     * 
     * @return numberWheres
     */
    public com.easyinsight.solutions.teamcity.webservice.NumberWhere[] getNumberWheres() {
        return numberWheres;
    }


    /**
     * Sets the numberWheres value for this Where.
     * 
     * @param numberWheres
     */
    public void setNumberWheres(com.easyinsight.solutions.teamcity.webservice.NumberWhere[] numberWheres) {
        this.numberWheres = numberWheres;
    }

    public com.easyinsight.solutions.teamcity.webservice.NumberWhere getNumberWheres(int i) {
        return this.numberWheres[i];
    }

    public void setNumberWheres(int i, com.easyinsight.solutions.teamcity.webservice.NumberWhere _value) {
        this.numberWheres[i] = _value;
    }


    /**
     * Gets the stringWheres value for this Where.
     * 
     * @return stringWheres
     */
    public com.easyinsight.solutions.teamcity.webservice.StringWhere[] getStringWheres() {
        return stringWheres;
    }


    /**
     * Sets the stringWheres value for this Where.
     * 
     * @param stringWheres
     */
    public void setStringWheres(com.easyinsight.solutions.teamcity.webservice.StringWhere[] stringWheres) {
        this.stringWheres = stringWheres;
    }

    public com.easyinsight.solutions.teamcity.webservice.StringWhere getStringWheres(int i) {
        return this.stringWheres[i];
    }

    public void setStringWheres(int i, com.easyinsight.solutions.teamcity.webservice.StringWhere _value) {
        this.stringWheres[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Where)) return false;
        Where other = (Where) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dateWheres==null && other.getDateWheres()==null) || 
             (this.dateWheres!=null &&
              java.util.Arrays.equals(this.dateWheres, other.getDateWheres()))) &&
            ((this.dayWheres==null && other.getDayWheres()==null) || 
             (this.dayWheres!=null &&
              java.util.Arrays.equals(this.dayWheres, other.getDayWheres()))) &&
            ((this.numberWheres==null && other.getNumberWheres()==null) || 
             (this.numberWheres!=null &&
              java.util.Arrays.equals(this.numberWheres, other.getNumberWheres()))) &&
            ((this.stringWheres==null && other.getStringWheres()==null) || 
             (this.stringWheres!=null &&
              java.util.Arrays.equals(this.stringWheres, other.getStringWheres())));
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
        if (getDateWheres() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDateWheres());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDateWheres(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDayWheres() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDayWheres());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDayWheres(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumberWheres() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNumberWheres());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNumberWheres(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStringWheres() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStringWheres());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStringWheres(), i);
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
        new org.apache.axis.description.TypeDesc(Where.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "where"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateWheres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateWheres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "dateWhere"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dayWheres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dayWheres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "dayWhere"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberWheres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberWheres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "numberWhere"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stringWheres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stringWheres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "stringWhere"));
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
