/**
 * Visit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class Visit  implements java.io.Serializable {
    /* The Visit Id of the Visit. */
    private int visit_id;

    /* The IP address of the Contact when the performmed the visit.
     * This has been converted to long (binary) format. */
    private int ip;

    /* The time of the last recorded Page View. */
    private java.util.Calendar last_access;

    /* The UserAgent Id of the Contact. */
    private int user_agent_id;

    /* The type Id of the Traffic Source. */
    private int traffic_source_type_id;

    /* The DomainGroup Id of the Visit. */
    private int domain_group_id;

    /* The Contact Id associated with the Visit. */
    private int contact_id;

    private com.easyinsight.client.netresults.DirectAccess directAccess;

    private com.easyinsight.client.netresults.Search search;

    private com.easyinsight.client.netresults.Referral referral;

    private com.easyinsight.client.netresults.Adsense adsense;

    public Visit() {
    }

    public Visit(
           int visit_id,
           int ip,
           java.util.Calendar last_access,
           int user_agent_id,
           int traffic_source_type_id,
           int domain_group_id,
           int contact_id,
           com.easyinsight.client.netresults.DirectAccess directAccess,
           com.easyinsight.client.netresults.Search search,
           com.easyinsight.client.netresults.Referral referral,
           com.easyinsight.client.netresults.Adsense adsense) {
           this.visit_id = visit_id;
           this.ip = ip;
           this.last_access = last_access;
           this.user_agent_id = user_agent_id;
           this.traffic_source_type_id = traffic_source_type_id;
           this.domain_group_id = domain_group_id;
           this.contact_id = contact_id;
           this.directAccess = directAccess;
           this.search = search;
           this.referral = referral;
           this.adsense = adsense;
    }


    /**
     * Gets the visit_id value for this Visit.
     * 
     * @return visit_id   * The Visit Id of the Visit.
     */
    public int getVisit_id() {
        return visit_id;
    }


    /**
     * Sets the visit_id value for this Visit.
     * 
     * @param visit_id   * The Visit Id of the Visit.
     */
    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }


    /**
     * Gets the ip value for this Visit.
     * 
     * @return ip   * The IP address of the Contact when the performmed the visit.
     * This has been converted to long (binary) format.
     */
    public int getIp() {
        return ip;
    }


    /**
     * Sets the ip value for this Visit.
     * 
     * @param ip   * The IP address of the Contact when the performmed the visit.
     * This has been converted to long (binary) format.
     */
    public void setIp(int ip) {
        this.ip = ip;
    }


    /**
     * Gets the last_access value for this Visit.
     * 
     * @return last_access   * The time of the last recorded Page View.
     */
    public java.util.Calendar getLast_access() {
        return last_access;
    }


    /**
     * Sets the last_access value for this Visit.
     * 
     * @param last_access   * The time of the last recorded Page View.
     */
    public void setLast_access(java.util.Calendar last_access) {
        this.last_access = last_access;
    }


    /**
     * Gets the user_agent_id value for this Visit.
     * 
     * @return user_agent_id   * The UserAgent Id of the Contact.
     */
    public int getUser_agent_id() {
        return user_agent_id;
    }


    /**
     * Sets the user_agent_id value for this Visit.
     * 
     * @param user_agent_id   * The UserAgent Id of the Contact.
     */
    public void setUser_agent_id(int user_agent_id) {
        this.user_agent_id = user_agent_id;
    }


    /**
     * Gets the traffic_source_type_id value for this Visit.
     * 
     * @return traffic_source_type_id   * The type Id of the Traffic Source.
     */
    public int getTraffic_source_type_id() {
        return traffic_source_type_id;
    }


    /**
     * Sets the traffic_source_type_id value for this Visit.
     * 
     * @param traffic_source_type_id   * The type Id of the Traffic Source.
     */
    public void setTraffic_source_type_id(int traffic_source_type_id) {
        this.traffic_source_type_id = traffic_source_type_id;
    }


    /**
     * Gets the domain_group_id value for this Visit.
     * 
     * @return domain_group_id   * The DomainGroup Id of the Visit.
     */
    public int getDomain_group_id() {
        return domain_group_id;
    }


    /**
     * Sets the domain_group_id value for this Visit.
     * 
     * @param domain_group_id   * The DomainGroup Id of the Visit.
     */
    public void setDomain_group_id(int domain_group_id) {
        this.domain_group_id = domain_group_id;
    }


    /**
     * Gets the contact_id value for this Visit.
     * 
     * @return contact_id   * The Contact Id associated with the Visit.
     */
    public int getContact_id() {
        return contact_id;
    }


    /**
     * Sets the contact_id value for this Visit.
     * 
     * @param contact_id   * The Contact Id associated with the Visit.
     */
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }


    /**
     * Gets the directAccess value for this Visit.
     * 
     * @return directAccess
     */
    public com.easyinsight.client.netresults.DirectAccess getDirectAccess() {
        return directAccess;
    }


    /**
     * Sets the directAccess value for this Visit.
     * 
     * @param directAccess
     */
    public void setDirectAccess(com.easyinsight.client.netresults.DirectAccess directAccess) {
        this.directAccess = directAccess;
    }


    /**
     * Gets the search value for this Visit.
     * 
     * @return search
     */
    public com.easyinsight.client.netresults.Search getSearch() {
        return search;
    }


    /**
     * Sets the search value for this Visit.
     * 
     * @param search
     */
    public void setSearch(com.easyinsight.client.netresults.Search search) {
        this.search = search;
    }


    /**
     * Gets the referral value for this Visit.
     * 
     * @return referral
     */
    public com.easyinsight.client.netresults.Referral getReferral() {
        return referral;
    }


    /**
     * Sets the referral value for this Visit.
     * 
     * @param referral
     */
    public void setReferral(com.easyinsight.client.netresults.Referral referral) {
        this.referral = referral;
    }


    /**
     * Gets the adsense value for this Visit.
     * 
     * @return adsense
     */
    public com.easyinsight.client.netresults.Adsense getAdsense() {
        return adsense;
    }


    /**
     * Sets the adsense value for this Visit.
     * 
     * @param adsense
     */
    public void setAdsense(com.easyinsight.client.netresults.Adsense adsense) {
        this.adsense = adsense;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Visit)) return false;
        Visit other = (Visit) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.visit_id == other.getVisit_id() &&
            this.ip == other.getIp() &&
            ((this.last_access==null && other.getLast_access()==null) || 
             (this.last_access!=null &&
              this.last_access.equals(other.getLast_access()))) &&
            this.user_agent_id == other.getUser_agent_id() &&
            this.traffic_source_type_id == other.getTraffic_source_type_id() &&
            this.domain_group_id == other.getDomain_group_id() &&
            this.contact_id == other.getContact_id() &&
            ((this.directAccess==null && other.getDirectAccess()==null) || 
             (this.directAccess!=null &&
              this.directAccess.equals(other.getDirectAccess()))) &&
            ((this.search==null && other.getSearch()==null) || 
             (this.search!=null &&
              this.search.equals(other.getSearch()))) &&
            ((this.referral==null && other.getReferral()==null) || 
             (this.referral!=null &&
              this.referral.equals(other.getReferral()))) &&
            ((this.adsense==null && other.getAdsense()==null) || 
             (this.adsense!=null &&
              this.adsense.equals(other.getAdsense())));
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
        _hashCode += getVisit_id();
        _hashCode += getIp();
        if (getLast_access() != null) {
            _hashCode += getLast_access().hashCode();
        }
        _hashCode += getUser_agent_id();
        _hashCode += getTraffic_source_type_id();
        _hashCode += getDomain_group_id();
        _hashCode += getContact_id();
        if (getDirectAccess() != null) {
            _hashCode += getDirectAccess().hashCode();
        }
        if (getSearch() != null) {
            _hashCode += getSearch().hashCode();
        }
        if (getReferral() != null) {
            _hashCode += getReferral().hashCode();
        }
        if (getAdsense() != null) {
            _hashCode += getAdsense().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Visit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Visit"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visit_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visit_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("last_access");
        elemField.setXmlName(new javax.xml.namespace.QName("", "last_access"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user_agent_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user_agent_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("traffic_source_type_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "traffic_source_type_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domain_group_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domain_group_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("directAccess");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DirectAccess"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "DirectAccess"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Search"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Search"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referral");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Referral"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Referral"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adsense");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Adsense"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Adsense"));
        elemField.setMinOccurs(0);
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
