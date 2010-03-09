/**
 * SubmitContactImportArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class SubmitContactImportArgs  implements java.io.Serializable {
    private com.easyinsight.client.netresults.FileSpec file;

    /* An array of the Email List Ids you would like the submitted
     * contacts to become members of. If the overwrite flag is set, this
     * will only add the contacts to the lists they are not already a member
     * of. */
    private int[] add_email_list_ids;

    /* An array of the Email List Ids you would like the submitted
     * contacts to removed from. If any submitted contact is not already
     * member of the list, no action will be taken. This argument will only
     * apply to existing contacts in the system, it will have no effect on
     * new contacts, or contacts who are not a member of any specified list. */
    private int[] remove_email_list_ids;

    /* This argument allows you to specify that duplicates (Determined
     * off, in order of precedence: Email Address, Net-Results Contact Id,
     * or your Company's Primary Custom Field) should not be skipped - rather
     * any information in the file for duplicate contacts will overwrite
     * current information. This is especially useful if you'd like to batch
     * change list membership for a number of contacts. */
    private boolean overwrite_duplicates;

    /* This argument is not yet implemented and will be ignored. */
    private boolean clobber_lists;

    /* An array of ContactImportMapping, specifying which field in
     * the CSV maps to which field in the Net-Results database. */
    private com.easyinsight.client.netresults.ContactImportMapping[] contact_import_mappings;

    /* An array of email addresses that will be notified upon completion
     * of the ContactImport job. */
    private java.lang.String[] notification_recipients;

    public SubmitContactImportArgs() {
    }

    public SubmitContactImportArgs(
           com.easyinsight.client.netresults.FileSpec file,
           int[] add_email_list_ids,
           int[] remove_email_list_ids,
           boolean overwrite_duplicates,
           boolean clobber_lists,
           com.easyinsight.client.netresults.ContactImportMapping[] contact_import_mappings,
           java.lang.String[] notification_recipients) {
           this.file = file;
           this.add_email_list_ids = add_email_list_ids;
           this.remove_email_list_ids = remove_email_list_ids;
           this.overwrite_duplicates = overwrite_duplicates;
           this.clobber_lists = clobber_lists;
           this.contact_import_mappings = contact_import_mappings;
           this.notification_recipients = notification_recipients;
    }


    /**
     * Gets the file value for this SubmitContactImportArgs.
     * 
     * @return file
     */
    public com.easyinsight.client.netresults.FileSpec getFile() {
        return file;
    }


    /**
     * Sets the file value for this SubmitContactImportArgs.
     * 
     * @param file
     */
    public void setFile(com.easyinsight.client.netresults.FileSpec file) {
        this.file = file;
    }


    /**
     * Gets the add_email_list_ids value for this SubmitContactImportArgs.
     * 
     * @return add_email_list_ids   * An array of the Email List Ids you would like the submitted
     * contacts to become members of. If the overwrite flag is set, this
     * will only add the contacts to the lists they are not already a member
     * of.
     */
    public int[] getAdd_email_list_ids() {
        return add_email_list_ids;
    }


    /**
     * Sets the add_email_list_ids value for this SubmitContactImportArgs.
     * 
     * @param add_email_list_ids   * An array of the Email List Ids you would like the submitted
     * contacts to become members of. If the overwrite flag is set, this
     * will only add the contacts to the lists they are not already a member
     * of.
     */
    public void setAdd_email_list_ids(int[] add_email_list_ids) {
        this.add_email_list_ids = add_email_list_ids;
    }


    /**
     * Gets the remove_email_list_ids value for this SubmitContactImportArgs.
     * 
     * @return remove_email_list_ids   * An array of the Email List Ids you would like the submitted
     * contacts to removed from. If any submitted contact is not already
     * member of the list, no action will be taken. This argument will only
     * apply to existing contacts in the system, it will have no effect on
     * new contacts, or contacts who are not a member of any specified list.
     */
    public int[] getRemove_email_list_ids() {
        return remove_email_list_ids;
    }


    /**
     * Sets the remove_email_list_ids value for this SubmitContactImportArgs.
     * 
     * @param remove_email_list_ids   * An array of the Email List Ids you would like the submitted
     * contacts to removed from. If any submitted contact is not already
     * member of the list, no action will be taken. This argument will only
     * apply to existing contacts in the system, it will have no effect on
     * new contacts, or contacts who are not a member of any specified list.
     */
    public void setRemove_email_list_ids(int[] remove_email_list_ids) {
        this.remove_email_list_ids = remove_email_list_ids;
    }


    /**
     * Gets the overwrite_duplicates value for this SubmitContactImportArgs.
     * 
     * @return overwrite_duplicates   * This argument allows you to specify that duplicates (Determined
     * off, in order of precedence: Email Address, Net-Results Contact Id,
     * or your Company's Primary Custom Field) should not be skipped - rather
     * any information in the file for duplicate contacts will overwrite
     * current information. This is especially useful if you'd like to batch
     * change list membership for a number of contacts.
     */
    public boolean isOverwrite_duplicates() {
        return overwrite_duplicates;
    }


    /**
     * Sets the overwrite_duplicates value for this SubmitContactImportArgs.
     * 
     * @param overwrite_duplicates   * This argument allows you to specify that duplicates (Determined
     * off, in order of precedence: Email Address, Net-Results Contact Id,
     * or your Company's Primary Custom Field) should not be skipped - rather
     * any information in the file for duplicate contacts will overwrite
     * current information. This is especially useful if you'd like to batch
     * change list membership for a number of contacts.
     */
    public void setOverwrite_duplicates(boolean overwrite_duplicates) {
        this.overwrite_duplicates = overwrite_duplicates;
    }


    /**
     * Gets the clobber_lists value for this SubmitContactImportArgs.
     * 
     * @return clobber_lists   * This argument is not yet implemented and will be ignored.
     */
    public boolean isClobber_lists() {
        return clobber_lists;
    }


    /**
     * Sets the clobber_lists value for this SubmitContactImportArgs.
     * 
     * @param clobber_lists   * This argument is not yet implemented and will be ignored.
     */
    public void setClobber_lists(boolean clobber_lists) {
        this.clobber_lists = clobber_lists;
    }


    /**
     * Gets the contact_import_mappings value for this SubmitContactImportArgs.
     * 
     * @return contact_import_mappings   * An array of ContactImportMapping, specifying which field in
     * the CSV maps to which field in the Net-Results database.
     */
    public com.easyinsight.client.netresults.ContactImportMapping[] getContact_import_mappings() {
        return contact_import_mappings;
    }


    /**
     * Sets the contact_import_mappings value for this SubmitContactImportArgs.
     * 
     * @param contact_import_mappings   * An array of ContactImportMapping, specifying which field in
     * the CSV maps to which field in the Net-Results database.
     */
    public void setContact_import_mappings(com.easyinsight.client.netresults.ContactImportMapping[] contact_import_mappings) {
        this.contact_import_mappings = contact_import_mappings;
    }


    /**
     * Gets the notification_recipients value for this SubmitContactImportArgs.
     * 
     * @return notification_recipients   * An array of email addresses that will be notified upon completion
     * of the ContactImport job.
     */
    public java.lang.String[] getNotification_recipients() {
        return notification_recipients;
    }


    /**
     * Sets the notification_recipients value for this SubmitContactImportArgs.
     * 
     * @param notification_recipients   * An array of email addresses that will be notified upon completion
     * of the ContactImport job.
     */
    public void setNotification_recipients(java.lang.String[] notification_recipients) {
        this.notification_recipients = notification_recipients;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubmitContactImportArgs)) return false;
        SubmitContactImportArgs other = (SubmitContactImportArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.file==null && other.getFile()==null) || 
             (this.file!=null &&
              this.file.equals(other.getFile()))) &&
            ((this.add_email_list_ids==null && other.getAdd_email_list_ids()==null) || 
             (this.add_email_list_ids!=null &&
              java.util.Arrays.equals(this.add_email_list_ids, other.getAdd_email_list_ids()))) &&
            ((this.remove_email_list_ids==null && other.getRemove_email_list_ids()==null) || 
             (this.remove_email_list_ids!=null &&
              java.util.Arrays.equals(this.remove_email_list_ids, other.getRemove_email_list_ids()))) &&
            this.overwrite_duplicates == other.isOverwrite_duplicates() &&
            this.clobber_lists == other.isClobber_lists() &&
            ((this.contact_import_mappings==null && other.getContact_import_mappings()==null) || 
             (this.contact_import_mappings!=null &&
              java.util.Arrays.equals(this.contact_import_mappings, other.getContact_import_mappings()))) &&
            ((this.notification_recipients==null && other.getNotification_recipients()==null) || 
             (this.notification_recipients!=null &&
              java.util.Arrays.equals(this.notification_recipients, other.getNotification_recipients())));
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
        if (getFile() != null) {
            _hashCode += getFile().hashCode();
        }
        if (getAdd_email_list_ids() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAdd_email_list_ids());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAdd_email_list_ids(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRemove_email_list_ids() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRemove_email_list_ids());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRemove_email_list_ids(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isOverwrite_duplicates() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isClobber_lists() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getContact_import_mappings() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContact_import_mappings());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContact_import_mappings(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNotification_recipients() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNotification_recipients());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNotification_recipients(), i);
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
        new org.apache.axis.description.TypeDesc(SubmitContactImportArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "submitContactImportArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("file");
        elemField.setXmlName(new javax.xml.namespace.QName("", "file"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "fileSpec"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("add_email_list_ids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "add_email_list_ids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remove_email_list_ids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "remove_email_list_ids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overwrite_duplicates");
        elemField.setXmlName(new javax.xml.namespace.QName("", "overwrite_duplicates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clobber_lists");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clobber_lists"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_import_mappings");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_import_mappings"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "ContactImportMapping"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notification_recipients");
        elemField.setXmlName(new javax.xml.namespace.QName("", "notification_recipients"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
