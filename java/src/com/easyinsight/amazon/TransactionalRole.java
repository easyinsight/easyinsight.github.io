/**
 * TransactionalRole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class TransactionalRole implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TransactionalRole(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Sender = "Sender";
    public static final java.lang.String _Caller = "Caller";
    public static final java.lang.String _Recipient = "Recipient";
    public static final java.lang.String _Amazon = "Amazon";
    public static final java.lang.String _CreditToken = "CreditToken";
    public static final java.lang.String _Creditor = "Creditor";
    public static final java.lang.String _DebtTracker = "DebtTracker";
    public static final java.lang.String _LiabilityTracker = "LiabilityTracker";
    public static final java.lang.String _PrepaidToken = "PrepaidToken";
    public static final java.lang.String _PrepaidLiabilityHolder = "PrepaidLiabilityHolder";
    public static final TransactionalRole Sender = new TransactionalRole(_Sender);
    public static final TransactionalRole Caller = new TransactionalRole(_Caller);
    public static final TransactionalRole Recipient = new TransactionalRole(_Recipient);
    public static final TransactionalRole Amazon = new TransactionalRole(_Amazon);
    public static final TransactionalRole CreditToken = new TransactionalRole(_CreditToken);
    public static final TransactionalRole Creditor = new TransactionalRole(_Creditor);
    public static final TransactionalRole DebtTracker = new TransactionalRole(_DebtTracker);
    public static final TransactionalRole LiabilityTracker = new TransactionalRole(_LiabilityTracker);
    public static final TransactionalRole PrepaidToken = new TransactionalRole(_PrepaidToken);
    public static final TransactionalRole PrepaidLiabilityHolder = new TransactionalRole(_PrepaidLiabilityHolder);
    public java.lang.String getValue() { return _value_;}
    public static TransactionalRole fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TransactionalRole enumeration = (TransactionalRole)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TransactionalRole fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TransactionalRole.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionalRole"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
