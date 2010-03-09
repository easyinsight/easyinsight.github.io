/**
 * SubmitContactExportArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class SubmitContactExportArgs  implements java.io.Serializable {
    /* An array of email addresses. Each email address will receive
     * a copy of the ContactExport csv. */
    private java.lang.String[] export_recipients;

    /* An optional array of Contact Ids. If not specified, all Contacts
     * will be included in the ContactExport. */
    private int[] contact_ids;

    public SubmitContactExportArgs() {
    }

    public SubmitContactExportArgs(
           java.lang.String[] export_recipients,
           int[] contact_ids) {
           this.export_recipients = export_recipients;
           this.contact_ids = contact_ids;
    }


    /**
     * Gets the export_recipients value for this SubmitContactExportArgs.
     * 
     * @return export_recipients   * An array of email addresses. Each email address will receive
     * a copy of the ContactExport csv.
     */
    public java.lang.String[] getExport_recipients() {
        return export_recipients;
    }


    /**
     * Sets the export_recipients value for this SubmitContactExportArgs.
     * 
     * @param export_recipients   * An array of email addresses. Each email address will receive
     * a copy of the ContactExport csv.
     */
    public void setExport_recipients(java.lang.String[] export_recipients) {
        this.export_recipients = export_recipients;
    }


    /**
     * Gets the contact_ids value for this SubmitContactExportArgs.
     * 
     * @return contact_ids   * An optional array of Contact Ids. If not specified, all Contacts
     * will be included in the ContactExport.
     */
    public int[] getContact_ids() {
        return contact_ids;
    }


    /**
     * Sets the contact_ids value for this SubmitContactExportArgs.
     * 
     * @param contact_ids   * An optional array of Contact Ids. If not specified, all Contacts
     * will be included in the ContactExport.
     */
    public void setContact_ids(int[] contact_ids) {
        this.contact_ids = contact_ids;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubmitContactExportArgs)) return false;
        SubmitContactExportArgs other = (SubmitContactExportArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.export_recipients==null && other.getExport_recipients()==null) || 
             (this.export_recipients!=null &&
              java.util.Arrays.equals(this.export_recipients, other.getExport_recipients()))) &&
            ((this.contact_ids==null && other.getContact_ids()==null) || 
             (this.contact_ids!=null &&
              java.util.Arrays.equals(this.contact_ids, other.getContact_ids())));
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
        if (getExport_recipients() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExport_recipients());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExport_recipients(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getContact_ids() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContact_ids());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContact_ids(), i);
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
        new org.apache.axis.description.TypeDesc(SubmitContactExportArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "submitContactExportArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("export_recipients");
        elemField.setXmlName(new javax.xml.namespace.QName("", "export_recipients"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_ids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_ids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
