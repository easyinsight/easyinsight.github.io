/**
 * ContactImportStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class ContactImportStatus  implements java.io.Serializable {
    /* The status of the ContactImport. */
    private java.lang.String status;

    /* The number of contacts added by the ContactImport. Only returned
     * upon a successful ContactImport. */
    private java.lang.Integer added;

    /* The number of contacts overwritten by the ContactImport. Only
     * returned upon a successful ContactImport. */
    private java.lang.Integer overwritten;

    /* The number of contacts skipped due to duplication by the ContactImport.
     * Only returned upon a successful ContactImport. */
    private java.lang.Integer duplicates;

    /* The number of contacts skipped due to invalid data by the ContactImport.
     * Only returned upon a successful ContactImport. */
    private java.lang.Integer invalid;

    /* An array of Contact Ids that were added or overwritten by this
     * ContactImport. */
    private int[] affected_contact_ids;

    public ContactImportStatus() {
    }

    public ContactImportStatus(
           java.lang.String status,
           java.lang.Integer added,
           java.lang.Integer overwritten,
           java.lang.Integer duplicates,
           java.lang.Integer invalid,
           int[] affected_contact_ids) {
           this.status = status;
           this.added = added;
           this.overwritten = overwritten;
           this.duplicates = duplicates;
           this.invalid = invalid;
           this.affected_contact_ids = affected_contact_ids;
    }


    /**
     * Gets the status value for this ContactImportStatus.
     * 
     * @return status   * The status of the ContactImport.
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ContactImportStatus.
     * 
     * @param status   * The status of the ContactImport.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the added value for this ContactImportStatus.
     * 
     * @return added   * The number of contacts added by the ContactImport. Only returned
     * upon a successful ContactImport.
     */
    public java.lang.Integer getAdded() {
        return added;
    }


    /**
     * Sets the added value for this ContactImportStatus.
     * 
     * @param added   * The number of contacts added by the ContactImport. Only returned
     * upon a successful ContactImport.
     */
    public void setAdded(java.lang.Integer added) {
        this.added = added;
    }


    /**
     * Gets the overwritten value for this ContactImportStatus.
     * 
     * @return overwritten   * The number of contacts overwritten by the ContactImport. Only
     * returned upon a successful ContactImport.
     */
    public java.lang.Integer getOverwritten() {
        return overwritten;
    }


    /**
     * Sets the overwritten value for this ContactImportStatus.
     * 
     * @param overwritten   * The number of contacts overwritten by the ContactImport. Only
     * returned upon a successful ContactImport.
     */
    public void setOverwritten(java.lang.Integer overwritten) {
        this.overwritten = overwritten;
    }


    /**
     * Gets the duplicates value for this ContactImportStatus.
     * 
     * @return duplicates   * The number of contacts skipped due to duplication by the ContactImport.
     * Only returned upon a successful ContactImport.
     */
    public java.lang.Integer getDuplicates() {
        return duplicates;
    }


    /**
     * Sets the duplicates value for this ContactImportStatus.
     * 
     * @param duplicates   * The number of contacts skipped due to duplication by the ContactImport.
     * Only returned upon a successful ContactImport.
     */
    public void setDuplicates(java.lang.Integer duplicates) {
        this.duplicates = duplicates;
    }


    /**
     * Gets the invalid value for this ContactImportStatus.
     * 
     * @return invalid   * The number of contacts skipped due to invalid data by the ContactImport.
     * Only returned upon a successful ContactImport.
     */
    public java.lang.Integer getInvalid() {
        return invalid;
    }


    /**
     * Sets the invalid value for this ContactImportStatus.
     * 
     * @param invalid   * The number of contacts skipped due to invalid data by the ContactImport.
     * Only returned upon a successful ContactImport.
     */
    public void setInvalid(java.lang.Integer invalid) {
        this.invalid = invalid;
    }


    /**
     * Gets the affected_contact_ids value for this ContactImportStatus.
     * 
     * @return affected_contact_ids   * An array of Contact Ids that were added or overwritten by this
     * ContactImport.
     */
    public int[] getAffected_contact_ids() {
        return affected_contact_ids;
    }


    /**
     * Sets the affected_contact_ids value for this ContactImportStatus.
     * 
     * @param affected_contact_ids   * An array of Contact Ids that were added or overwritten by this
     * ContactImport.
     */
    public void setAffected_contact_ids(int[] affected_contact_ids) {
        this.affected_contact_ids = affected_contact_ids;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContactImportStatus)) return false;
        ContactImportStatus other = (ContactImportStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.added==null && other.getAdded()==null) || 
             (this.added!=null &&
              this.added.equals(other.getAdded()))) &&
            ((this.overwritten==null && other.getOverwritten()==null) || 
             (this.overwritten!=null &&
              this.overwritten.equals(other.getOverwritten()))) &&
            ((this.duplicates==null && other.getDuplicates()==null) || 
             (this.duplicates!=null &&
              this.duplicates.equals(other.getDuplicates()))) &&
            ((this.invalid==null && other.getInvalid()==null) || 
             (this.invalid!=null &&
              this.invalid.equals(other.getInvalid()))) &&
            ((this.affected_contact_ids==null && other.getAffected_contact_ids()==null) || 
             (this.affected_contact_ids!=null &&
              java.util.Arrays.equals(this.affected_contact_ids, other.getAffected_contact_ids())));
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getAdded() != null) {
            _hashCode += getAdded().hashCode();
        }
        if (getOverwritten() != null) {
            _hashCode += getOverwritten().hashCode();
        }
        if (getDuplicates() != null) {
            _hashCode += getDuplicates().hashCode();
        }
        if (getInvalid() != null) {
            _hashCode += getInvalid().hashCode();
        }
        if (getAffected_contact_ids() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAffected_contact_ids());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAffected_contact_ids(), i);
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
        new org.apache.axis.description.TypeDesc(ContactImportStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "ContactImportStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("added");
        elemField.setXmlName(new javax.xml.namespace.QName("", "added"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overwritten");
        elemField.setXmlName(new javax.xml.namespace.QName("", "overwritten"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duplicates");
        elemField.setXmlName(new javax.xml.namespace.QName("", "duplicates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invalid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "invalid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("affected_contact_ids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "affected_contact_ids"));
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
