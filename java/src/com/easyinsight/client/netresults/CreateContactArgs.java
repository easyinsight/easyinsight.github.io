/**
 * CreateContactArgs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class CreateContactArgs  implements java.io.Serializable {
    /* The Contacts properly formed email address. */
    private java.lang.String contact_email_address;

    /* The Contact's title. */
    private java.lang.String contact_title;

    /* The Contact's first name. */
    private java.lang.String contact_first_name;

    /* The Contact's last name. */
    private java.lang.String contact_last_name;

    /* The Contact's Company name. */
    private java.lang.String company_name;

    /* The Contact's Address 1. */
    private java.lang.String contact_address_1;

    /* The Contact's Address 2. */
    private java.lang.String contact_address_2;

    /* The Contact's Mobile Phone2. */
    private java.lang.String contact_mobile_phone;

    /* The Contact's Home Phone. */
    private java.lang.String contact_home_phone;

    /* The Contact's Work Phone. */
    private java.lang.String contact_work_phone;

    /* The Contact's Fax. */
    private java.lang.String contact_fax;

    /* The Contact's City name. */
    private java.lang.String city_name;

    /* The Contact's postal code. */
    private java.lang.String contact_postalcode;

    /* The Contact's Country name. */
    private java.lang.String country_name;

    /* The Contact's State name. */
    private java.lang.String state_name;

    /* The Contact's Lead score. */
    private java.lang.String contact_lead_score;

    /* The Contact's Twitter Username/handle. */
    private java.lang.String contact_twitter_username;

    /* The Role Id of the Contact. */
    private java.math.BigInteger role_id;

    public CreateContactArgs() {
    }

    public CreateContactArgs(
           java.lang.String contact_email_address,
           java.lang.String contact_title,
           java.lang.String contact_first_name,
           java.lang.String contact_last_name,
           java.lang.String company_name,
           java.lang.String contact_address_1,
           java.lang.String contact_address_2,
           java.lang.String contact_mobile_phone,
           java.lang.String contact_home_phone,
           java.lang.String contact_work_phone,
           java.lang.String contact_fax,
           java.lang.String city_name,
           java.lang.String contact_postalcode,
           java.lang.String country_name,
           java.lang.String state_name,
           java.lang.String contact_lead_score,
           java.lang.String contact_twitter_username,
           java.math.BigInteger role_id) {
           this.contact_email_address = contact_email_address;
           this.contact_title = contact_title;
           this.contact_first_name = contact_first_name;
           this.contact_last_name = contact_last_name;
           this.company_name = company_name;
           this.contact_address_1 = contact_address_1;
           this.contact_address_2 = contact_address_2;
           this.contact_mobile_phone = contact_mobile_phone;
           this.contact_home_phone = contact_home_phone;
           this.contact_work_phone = contact_work_phone;
           this.contact_fax = contact_fax;
           this.city_name = city_name;
           this.contact_postalcode = contact_postalcode;
           this.country_name = country_name;
           this.state_name = state_name;
           this.contact_lead_score = contact_lead_score;
           this.contact_twitter_username = contact_twitter_username;
           this.role_id = role_id;
    }


    /**
     * Gets the contact_email_address value for this CreateContactArgs.
     * 
     * @return contact_email_address   * The Contacts properly formed email address.
     */
    public java.lang.String getContact_email_address() {
        return contact_email_address;
    }


    /**
     * Sets the contact_email_address value for this CreateContactArgs.
     * 
     * @param contact_email_address   * The Contacts properly formed email address.
     */
    public void setContact_email_address(java.lang.String contact_email_address) {
        this.contact_email_address = contact_email_address;
    }


    /**
     * Gets the contact_title value for this CreateContactArgs.
     * 
     * @return contact_title   * The Contact's title.
     */
    public java.lang.String getContact_title() {
        return contact_title;
    }


    /**
     * Sets the contact_title value for this CreateContactArgs.
     * 
     * @param contact_title   * The Contact's title.
     */
    public void setContact_title(java.lang.String contact_title) {
        this.contact_title = contact_title;
    }


    /**
     * Gets the contact_first_name value for this CreateContactArgs.
     * 
     * @return contact_first_name   * The Contact's first name.
     */
    public java.lang.String getContact_first_name() {
        return contact_first_name;
    }


    /**
     * Sets the contact_first_name value for this CreateContactArgs.
     * 
     * @param contact_first_name   * The Contact's first name.
     */
    public void setContact_first_name(java.lang.String contact_first_name) {
        this.contact_first_name = contact_first_name;
    }


    /**
     * Gets the contact_last_name value for this CreateContactArgs.
     * 
     * @return contact_last_name   * The Contact's last name.
     */
    public java.lang.String getContact_last_name() {
        return contact_last_name;
    }


    /**
     * Sets the contact_last_name value for this CreateContactArgs.
     * 
     * @param contact_last_name   * The Contact's last name.
     */
    public void setContact_last_name(java.lang.String contact_last_name) {
        this.contact_last_name = contact_last_name;
    }


    /**
     * Gets the company_name value for this CreateContactArgs.
     * 
     * @return company_name   * The Contact's Company name.
     */
    public java.lang.String getCompany_name() {
        return company_name;
    }


    /**
     * Sets the company_name value for this CreateContactArgs.
     * 
     * @param company_name   * The Contact's Company name.
     */
    public void setCompany_name(java.lang.String company_name) {
        this.company_name = company_name;
    }


    /**
     * Gets the contact_address_1 value for this CreateContactArgs.
     * 
     * @return contact_address_1   * The Contact's Address 1.
     */
    public java.lang.String getContact_address_1() {
        return contact_address_1;
    }


    /**
     * Sets the contact_address_1 value for this CreateContactArgs.
     * 
     * @param contact_address_1   * The Contact's Address 1.
     */
    public void setContact_address_1(java.lang.String contact_address_1) {
        this.contact_address_1 = contact_address_1;
    }


    /**
     * Gets the contact_address_2 value for this CreateContactArgs.
     * 
     * @return contact_address_2   * The Contact's Address 2.
     */
    public java.lang.String getContact_address_2() {
        return contact_address_2;
    }


    /**
     * Sets the contact_address_2 value for this CreateContactArgs.
     * 
     * @param contact_address_2   * The Contact's Address 2.
     */
    public void setContact_address_2(java.lang.String contact_address_2) {
        this.contact_address_2 = contact_address_2;
    }


    /**
     * Gets the contact_mobile_phone value for this CreateContactArgs.
     * 
     * @return contact_mobile_phone   * The Contact's Mobile Phone2.
     */
    public java.lang.String getContact_mobile_phone() {
        return contact_mobile_phone;
    }


    /**
     * Sets the contact_mobile_phone value for this CreateContactArgs.
     * 
     * @param contact_mobile_phone   * The Contact's Mobile Phone2.
     */
    public void setContact_mobile_phone(java.lang.String contact_mobile_phone) {
        this.contact_mobile_phone = contact_mobile_phone;
    }


    /**
     * Gets the contact_home_phone value for this CreateContactArgs.
     * 
     * @return contact_home_phone   * The Contact's Home Phone.
     */
    public java.lang.String getContact_home_phone() {
        return contact_home_phone;
    }


    /**
     * Sets the contact_home_phone value for this CreateContactArgs.
     * 
     * @param contact_home_phone   * The Contact's Home Phone.
     */
    public void setContact_home_phone(java.lang.String contact_home_phone) {
        this.contact_home_phone = contact_home_phone;
    }


    /**
     * Gets the contact_work_phone value for this CreateContactArgs.
     * 
     * @return contact_work_phone   * The Contact's Work Phone.
     */
    public java.lang.String getContact_work_phone() {
        return contact_work_phone;
    }


    /**
     * Sets the contact_work_phone value for this CreateContactArgs.
     * 
     * @param contact_work_phone   * The Contact's Work Phone.
     */
    public void setContact_work_phone(java.lang.String contact_work_phone) {
        this.contact_work_phone = contact_work_phone;
    }


    /**
     * Gets the contact_fax value for this CreateContactArgs.
     * 
     * @return contact_fax   * The Contact's Fax.
     */
    public java.lang.String getContact_fax() {
        return contact_fax;
    }


    /**
     * Sets the contact_fax value for this CreateContactArgs.
     * 
     * @param contact_fax   * The Contact's Fax.
     */
    public void setContact_fax(java.lang.String contact_fax) {
        this.contact_fax = contact_fax;
    }


    /**
     * Gets the city_name value for this CreateContactArgs.
     * 
     * @return city_name   * The Contact's City name.
     */
    public java.lang.String getCity_name() {
        return city_name;
    }


    /**
     * Sets the city_name value for this CreateContactArgs.
     * 
     * @param city_name   * The Contact's City name.
     */
    public void setCity_name(java.lang.String city_name) {
        this.city_name = city_name;
    }


    /**
     * Gets the contact_postalcode value for this CreateContactArgs.
     * 
     * @return contact_postalcode   * The Contact's postal code.
     */
    public java.lang.String getContact_postalcode() {
        return contact_postalcode;
    }


    /**
     * Sets the contact_postalcode value for this CreateContactArgs.
     * 
     * @param contact_postalcode   * The Contact's postal code.
     */
    public void setContact_postalcode(java.lang.String contact_postalcode) {
        this.contact_postalcode = contact_postalcode;
    }


    /**
     * Gets the country_name value for this CreateContactArgs.
     * 
     * @return country_name   * The Contact's Country name.
     */
    public java.lang.String getCountry_name() {
        return country_name;
    }


    /**
     * Sets the country_name value for this CreateContactArgs.
     * 
     * @param country_name   * The Contact's Country name.
     */
    public void setCountry_name(java.lang.String country_name) {
        this.country_name = country_name;
    }


    /**
     * Gets the state_name value for this CreateContactArgs.
     * 
     * @return state_name   * The Contact's State name.
     */
    public java.lang.String getState_name() {
        return state_name;
    }


    /**
     * Sets the state_name value for this CreateContactArgs.
     * 
     * @param state_name   * The Contact's State name.
     */
    public void setState_name(java.lang.String state_name) {
        this.state_name = state_name;
    }


    /**
     * Gets the contact_lead_score value for this CreateContactArgs.
     * 
     * @return contact_lead_score   * The Contact's Lead score.
     */
    public java.lang.String getContact_lead_score() {
        return contact_lead_score;
    }


    /**
     * Sets the contact_lead_score value for this CreateContactArgs.
     * 
     * @param contact_lead_score   * The Contact's Lead score.
     */
    public void setContact_lead_score(java.lang.String contact_lead_score) {
        this.contact_lead_score = contact_lead_score;
    }


    /**
     * Gets the contact_twitter_username value for this CreateContactArgs.
     * 
     * @return contact_twitter_username   * The Contact's Twitter Username/handle.
     */
    public java.lang.String getContact_twitter_username() {
        return contact_twitter_username;
    }


    /**
     * Sets the contact_twitter_username value for this CreateContactArgs.
     * 
     * @param contact_twitter_username   * The Contact's Twitter Username/handle.
     */
    public void setContact_twitter_username(java.lang.String contact_twitter_username) {
        this.contact_twitter_username = contact_twitter_username;
    }


    /**
     * Gets the role_id value for this CreateContactArgs.
     * 
     * @return role_id   * The Role Id of the Contact.
     */
    public java.math.BigInteger getRole_id() {
        return role_id;
    }


    /**
     * Sets the role_id value for this CreateContactArgs.
     * 
     * @param role_id   * The Role Id of the Contact.
     */
    public void setRole_id(java.math.BigInteger role_id) {
        this.role_id = role_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateContactArgs)) return false;
        CreateContactArgs other = (CreateContactArgs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.contact_email_address==null && other.getContact_email_address()==null) || 
             (this.contact_email_address!=null &&
              this.contact_email_address.equals(other.getContact_email_address()))) &&
            ((this.contact_title==null && other.getContact_title()==null) || 
             (this.contact_title!=null &&
              this.contact_title.equals(other.getContact_title()))) &&
            ((this.contact_first_name==null && other.getContact_first_name()==null) || 
             (this.contact_first_name!=null &&
              this.contact_first_name.equals(other.getContact_first_name()))) &&
            ((this.contact_last_name==null && other.getContact_last_name()==null) || 
             (this.contact_last_name!=null &&
              this.contact_last_name.equals(other.getContact_last_name()))) &&
            ((this.company_name==null && other.getCompany_name()==null) || 
             (this.company_name!=null &&
              this.company_name.equals(other.getCompany_name()))) &&
            ((this.contact_address_1==null && other.getContact_address_1()==null) || 
             (this.contact_address_1!=null &&
              this.contact_address_1.equals(other.getContact_address_1()))) &&
            ((this.contact_address_2==null && other.getContact_address_2()==null) || 
             (this.contact_address_2!=null &&
              this.contact_address_2.equals(other.getContact_address_2()))) &&
            ((this.contact_mobile_phone==null && other.getContact_mobile_phone()==null) || 
             (this.contact_mobile_phone!=null &&
              this.contact_mobile_phone.equals(other.getContact_mobile_phone()))) &&
            ((this.contact_home_phone==null && other.getContact_home_phone()==null) || 
             (this.contact_home_phone!=null &&
              this.contact_home_phone.equals(other.getContact_home_phone()))) &&
            ((this.contact_work_phone==null && other.getContact_work_phone()==null) || 
             (this.contact_work_phone!=null &&
              this.contact_work_phone.equals(other.getContact_work_phone()))) &&
            ((this.contact_fax==null && other.getContact_fax()==null) || 
             (this.contact_fax!=null &&
              this.contact_fax.equals(other.getContact_fax()))) &&
            ((this.city_name==null && other.getCity_name()==null) || 
             (this.city_name!=null &&
              this.city_name.equals(other.getCity_name()))) &&
            ((this.contact_postalcode==null && other.getContact_postalcode()==null) || 
             (this.contact_postalcode!=null &&
              this.contact_postalcode.equals(other.getContact_postalcode()))) &&
            ((this.country_name==null && other.getCountry_name()==null) || 
             (this.country_name!=null &&
              this.country_name.equals(other.getCountry_name()))) &&
            ((this.state_name==null && other.getState_name()==null) || 
             (this.state_name!=null &&
              this.state_name.equals(other.getState_name()))) &&
            ((this.contact_lead_score==null && other.getContact_lead_score()==null) || 
             (this.contact_lead_score!=null &&
              this.contact_lead_score.equals(other.getContact_lead_score()))) &&
            ((this.contact_twitter_username==null && other.getContact_twitter_username()==null) || 
             (this.contact_twitter_username!=null &&
              this.contact_twitter_username.equals(other.getContact_twitter_username()))) &&
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
        if (getContact_email_address() != null) {
            _hashCode += getContact_email_address().hashCode();
        }
        if (getContact_title() != null) {
            _hashCode += getContact_title().hashCode();
        }
        if (getContact_first_name() != null) {
            _hashCode += getContact_first_name().hashCode();
        }
        if (getContact_last_name() != null) {
            _hashCode += getContact_last_name().hashCode();
        }
        if (getCompany_name() != null) {
            _hashCode += getCompany_name().hashCode();
        }
        if (getContact_address_1() != null) {
            _hashCode += getContact_address_1().hashCode();
        }
        if (getContact_address_2() != null) {
            _hashCode += getContact_address_2().hashCode();
        }
        if (getContact_mobile_phone() != null) {
            _hashCode += getContact_mobile_phone().hashCode();
        }
        if (getContact_home_phone() != null) {
            _hashCode += getContact_home_phone().hashCode();
        }
        if (getContact_work_phone() != null) {
            _hashCode += getContact_work_phone().hashCode();
        }
        if (getContact_fax() != null) {
            _hashCode += getContact_fax().hashCode();
        }
        if (getCity_name() != null) {
            _hashCode += getCity_name().hashCode();
        }
        if (getContact_postalcode() != null) {
            _hashCode += getContact_postalcode().hashCode();
        }
        if (getCountry_name() != null) {
            _hashCode += getCountry_name().hashCode();
        }
        if (getState_name() != null) {
            _hashCode += getState_name().hashCode();
        }
        if (getContact_lead_score() != null) {
            _hashCode += getContact_lead_score().hashCode();
        }
        if (getContact_twitter_username() != null) {
            _hashCode += getContact_twitter_username().hashCode();
        }
        if (getRole_id() != null) {
            _hashCode += getRole_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateContactArgs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "createContactArgs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_email_address");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_email_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_first_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_first_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_last_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_last_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("company_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "company_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_address_1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_address_1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_address_2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_address_2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_mobile_phone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_mobile_phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_home_phone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_home_phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_work_phone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_work_phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_fax");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_fax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("city_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "city_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_postalcode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_postalcode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "country_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state_name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "state_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_lead_score");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_lead_score"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_twitter_username");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_twitter_username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
