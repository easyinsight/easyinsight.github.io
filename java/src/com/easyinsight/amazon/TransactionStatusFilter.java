/**
 * TransactionStatusFilter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class TransactionStatusFilter implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TransactionStatusFilter(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Success = "Success";
    public static final java.lang.String _Failure = "Failure";
    public static final java.lang.String _Initiated = "Initiated";
    public static final java.lang.String _Reinitiated = "Reinitiated";
    public static final java.lang.String _TemporaryDecline = "TemporaryDecline";
    public static final TransactionStatusFilter Success = new TransactionStatusFilter(_Success);
    public static final TransactionStatusFilter Failure = new TransactionStatusFilter(_Failure);
    public static final TransactionStatusFilter Initiated = new TransactionStatusFilter(_Initiated);
    public static final TransactionStatusFilter Reinitiated = new TransactionStatusFilter(_Reinitiated);
    public static final TransactionStatusFilter TemporaryDecline = new TransactionStatusFilter(_TemporaryDecline);
    public java.lang.String getValue() { return _value_;}
    public static TransactionStatusFilter fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TransactionStatusFilter enumeration = (TransactionStatusFilter)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TransactionStatusFilter fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransactionStatusFilter.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatusFilter"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
