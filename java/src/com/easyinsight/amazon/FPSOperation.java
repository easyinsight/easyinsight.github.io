/**
 * FPSOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class FPSOperation implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FPSOperation(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Pay = "Pay";
    public static final java.lang.String _Refund = "Refund";
    public static final java.lang.String _Settle = "Settle";
    public static final java.lang.String _SettleDebt = "SettleDebt";
    public static final java.lang.String _WriteOffDebt = "WriteOffDebt";
    public static final java.lang.String _FundPrepaid = "FundPrepaid";
    public static final java.lang.String _DepositFunds = "DepositFunds";
    public static final java.lang.String _WithdrawFunds = "WithdrawFunds";
    public static final java.lang.String _Reserve = "Reserve";
    public static final FPSOperation Pay = new FPSOperation(_Pay);
    public static final FPSOperation Refund = new FPSOperation(_Refund);
    public static final FPSOperation Settle = new FPSOperation(_Settle);
    public static final FPSOperation SettleDebt = new FPSOperation(_SettleDebt);
    public static final FPSOperation WriteOffDebt = new FPSOperation(_WriteOffDebt);
    public static final FPSOperation FundPrepaid = new FPSOperation(_FundPrepaid);
    public static final FPSOperation DepositFunds = new FPSOperation(_DepositFunds);
    public static final FPSOperation WithdrawFunds = new FPSOperation(_WithdrawFunds);
    public static final FPSOperation Reserve = new FPSOperation(_Reserve);
    public java.lang.String getValue() { return _value_;}
    public static FPSOperation fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        FPSOperation enumeration = (FPSOperation)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FPSOperation fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(FPSOperation.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperation"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
