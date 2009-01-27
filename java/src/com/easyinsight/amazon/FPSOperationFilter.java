/**
 * FPSOperationFilter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class FPSOperationFilter implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FPSOperationFilter(java.lang.String value) {
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
    public static final FPSOperationFilter Pay = new FPSOperationFilter(_Pay);
    public static final FPSOperationFilter Refund = new FPSOperationFilter(_Refund);
    public static final FPSOperationFilter Settle = new FPSOperationFilter(_Settle);
    public static final FPSOperationFilter SettleDebt = new FPSOperationFilter(_SettleDebt);
    public static final FPSOperationFilter WriteOffDebt = new FPSOperationFilter(_WriteOffDebt);
    public static final FPSOperationFilter FundPrepaid = new FPSOperationFilter(_FundPrepaid);
    public static final FPSOperationFilter DepositFunds = new FPSOperationFilter(_DepositFunds);
    public static final FPSOperationFilter WithdrawFunds = new FPSOperationFilter(_WithdrawFunds);
    public java.lang.String getValue() { return _value_;}
    public static FPSOperationFilter fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        FPSOperationFilter enumeration = (FPSOperationFilter)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FPSOperationFilter fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(FPSOperationFilter.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperationFilter"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
