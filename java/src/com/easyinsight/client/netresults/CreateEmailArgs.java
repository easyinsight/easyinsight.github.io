/**
 * CreateEmailArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class CreateEmailArgs  implements java.io.Serializable {
    /* The Name of the Email. */
    private java.lang.String email_name;

    /* The Text Content of the Email. */
    private java.lang.String email_content_text;

    /* The HTML Content of the Email. */
    private java.lang.String email_content_html;

    /* The Reply To Address for the Email. */
    private java.lang.String email_reply_to_address;

    /* The Reply To Address's Label. (Full name or Company Name as
     * required by CAN-SPAM) */
    private java.lang.String email_reply_to_label;

    /* The Subject of the Email. */
    private java.lang.String email_subject;

    /* The Role Id of the Email */
    private java.math.BigInteger role_id;

    public CreateEmailArgs() {
    }

    public CreateEmailArgs(
           java.lang.String email_name,
           java.lang.String email_content_text,
           java.lang.String email_content_html,
           java.lang.String email_reply_to_address,
           java.lang.String email_reply_to_label,
           java.lang.String email_subject,
           java.math.BigInteger role_id) {
           this.email_name = email_name;
           this.email_content_text = email_content_text;
           this.email_content_html = email_content_html;
           this.email_reply_to_address = email_reply_to_address;
           this.email_reply_to_label = email_reply_to_label;
           this.email_subject = email_subject;
           this.role_id = role_id;
    }


    /**
     * Gets the email_name value for this CreateEmailArgs.
     * 
     * @return email_name   * The Name of the Email.
     */
    public java.lang.String getEmail_name() {
        return email_name;
    }


    /**
     * Sets the email_name value for this CreateEmailArgs.
     * 
     * @param email_name   * The Name of the Email.
     */
    public void setEmail_name(java.lang.String email_name) {
        this.email_name = email_name;
    }


    /**
     * Gets the email_content_text value for this CreateEmailArgs.
     * 
     * @return email_content_text   * The Text Content of the Email.
     */
    public java.lang.String getEmail_content_text() {
        return email_content_text;
    }


    /**
     * Sets the email_content_text value for this CreateEmailArgs.
     * 
     * @param email_content_text   * The Text Content of the Email.
     */
    public void setEmail_content_text(java.lang.String email_content_text) {
        this.email_content_text = email_content_text;
    }


    /**
     * Gets the email_content_html value for this CreateEmailArgs.
     * 
     * @return email_content_html   * The HTML Content of the Email.
     */
    public java.lang.String getEmail_content_html() {
        return email_content_html;
    }


    /**
     * Sets the email_content_html value for this CreateEmailArgs.
     * 
     * @param email_content_html   * The HTML Content of the Email.
     */
    public void setEmail_content_html(java.lang.String email_content_html) {
        this.email_content_html = email_content_html;
    }


    /**
     * Gets the email_reply_to_address value for this CreateEmailArgs.
     * 
     * @return email_reply_to_address   * The Reply To Address for the Email.
     */
    public java.lang.String getEmail_reply_to_address() {
        return email_reply_to_address;
    }


    /**
     * Sets the email_reply_to_address value for this CreateEmailArgs.
     * 
     * @param email_reply_to_address   * The Reply To Address for the Email.
     */
    public void setEmail_reply_to_address(java.lang.String email_reply_to_address) {
        this.email_reply_to_address = email_reply_to_address;
    }


    /**
     * Gets the email_reply_to_label value for this CreateEmailArgs.
     * 
     * @return email_reply_to_label   * The Reply To Address's Label. (Full name or Company Name as
     * required by CAN-SPAM)
     */
    public java.lang.String getEmail_reply_to_label() {
        return email_reply_to_label;
    }


    /**
     * Sets the email_reply_to_label value for this CreateEmailArgs.
     * 
     * @param email_reply_to_label   * The Reply To Address's Label. (Full name or Company Name as
     * required by CAN-SPAM)
     */
    public void setEmail_reply_to_label(java.lang.String email_reply_to_label) {
        this.email_reply_to_label = email_reply_to_label;
    }


    /**
     * Gets the email_subject value for this CreateEmailArgs.
     * 
     * @return email_subject   * The Subject of the Email.
     */
    public java.lang.String getEmail_subject() {
        return email_subject;
    }


    /**
     * Sets the email_subject value for this CreateEmailArgs.
     * 
     * @param email_subject   * The Subject of the Email.
     */
    public void setEmail_subject(java.lang.String email_subject) {
        this.email_subject = email_subject;
    }


    /**
     * Gets the role_id value for this CreateEmailArgs.
     * 
     * @return role_id   * The Role Id of the Email
     */
    public java.math.BigInteger getRole_id() {
        return role_id;
    }


    /**
     * Sets the role_id value for this CreateEmailArgs.
     * 
     * @param role_id   * The Role Id of the Email
     */
    public void setRole_id(java.math.BigInteger role_id) {
        this.role_id = role_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateEmailArgs)) return false;
        CreateEmailArgs other = (CreateEmailArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.email_name==null && other.getEmail_name()==null) || 
             (this.email_name!=null &&
              this.email_name.equals(other.getEmail_name()))) &&
            ((this.email_content_text==null && other.getEmail_content_text()==null) || 
             (this.email_content_text!=null &&
              this.email_content_text.equals(other.getEmail_content_text()))) &&
            ((this.email_content_html==null && other.getEmail_content_html()==null) || 
             (this.email_content_html!=null &&
              this.email_content_html.equals(other.getEmail_content_html()))) &&
            ((this.email_reply_to_address==null && other.getEmail_reply_to_address()==null) || 
             (this.email_reply_to_address!=null &&
              this.email_reply_to_address.equals(other.getEmail_reply_to_address()))) &&
            ((this.email_reply_to_label==null && other.getEmail_reply_to_label()==null) || 
             (this.email_reply_to_label!=null &&
              this.email_reply_to_label.equals(other.getEmail_reply_to_label()))) &&
            ((this.email_subject==null && other.getEmail_subject()==null) || 
             (this.email_subject!=null &&
              this.email_subject.equals(other.getEmail_subject()))) &&
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
        if (getEmail_name() != null) {
            _hashCode += getEmail_name().hashCode();
        }
        if (getEmail_content_text() != null) {
            _hashCode += getEmail_content_text().hashCode();
        }
        if (getEmail_content_html() != null) {
            _hashCode += getEmail_content_html().hashCode();
        }
        if (getEmail_reply_to_address() != null) {
            _hashCode += getEmail_reply_to_address().hashCode();
        }
        if (getEmail_reply_to_label() != null) {
            _hashCode += getEmail_reply_to_label().hashCode();
        }
        if (getEmail_subject() != null) {
            _hashCode += getEmail_subject().hashCode();
        }
        if (getRole_id() != null) {
            _hashCode += getRole_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateEmailArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "createEmailArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_content_text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_content_text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_content_html");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_content_html"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_reply_to_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_reply_to_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_reply_to_label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_reply_to_label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email_subject");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email_subject"));
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
