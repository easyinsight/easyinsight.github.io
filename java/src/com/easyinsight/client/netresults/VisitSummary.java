/**
 * VisitSummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class VisitSummary  implements java.io.Serializable {
    /* The Visit Id of the Visit. */
    private int visit_id;

    /* The DomainGroup Id of the Visit. */
    private int domain_group_id;

    /* The number of Page Views counted for the Visit. */
    private int access_count;

    /* The time of the last recorded Page View. */
    private java.util.Calendar last_access;

    /* The total duration of the Visit in seconds. If this is null,
     * the value is unknown. */
    private java.lang.Integer access_duration;

    /* The IP address of the Contact when the performmed the visit.
     * This has been converted to long (binary) format. */
    private int ip;

    /* The type Id of the Traffic Source. */
    private int traffic_source_type_id;

    /* The Contact Id associated with the Visit. */
    private int contact_id;

    private com.easyinsight.client.netresults.Visit visit;

    public VisitSummary() {
    }

    public VisitSummary(
           int visit_id,
           int domain_group_id,
           int access_count,
           java.util.Calendar last_access,
           java.lang.Integer access_duration,
           int ip,
           int traffic_source_type_id,
           int contact_id,
           com.easyinsight.client.netresults.Visit visit) {
           this.visit_id = visit_id;
           this.domain_group_id = domain_group_id;
           this.access_count = access_count;
           this.last_access = last_access;
           this.access_duration = access_duration;
           this.ip = ip;
           this.traffic_source_type_id = traffic_source_type_id;
           this.contact_id = contact_id;
           this.visit = visit;
    }


    /**
     * Gets the visit_id value for this VisitSummary.
     * 
     * @return visit_id   * The Visit Id of the Visit.
     */
    public int getVisit_id() {
        return visit_id;
    }


    /**
     * Sets the visit_id value for this VisitSummary.
     * 
     * @param visit_id   * The Visit Id of the Visit.
     */
    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }


    /**
     * Gets the domain_group_id value for this VisitSummary.
     * 
     * @return domain_group_id   * The DomainGroup Id of the Visit.
     */
    public int getDomain_group_id() {
        return domain_group_id;
    }


    /**
     * Sets the domain_group_id value for this VisitSummary.
     * 
     * @param domain_group_id   * The DomainGroup Id of the Visit.
     */
    public void setDomain_group_id(int domain_group_id) {
        this.domain_group_id = domain_group_id;
    }


    /**
     * Gets the access_count value for this VisitSummary.
     * 
     * @return access_count   * The number of Page Views counted for the Visit.
     */
    public int getAccess_count() {
        return access_count;
    }


    /**
     * Sets the access_count value for this VisitSummary.
     * 
     * @param access_count   * The number of Page Views counted for the Visit.
     */
    public void setAccess_count(int access_count) {
        this.access_count = access_count;
    }


    /**
     * Gets the last_access value for this VisitSummary.
     * 
     * @return last_access   * The time of the last recorded Page View.
     */
    public java.util.Calendar getLast_access() {
        return last_access;
    }


    /**
     * Sets the last_access value for this VisitSummary.
     * 
     * @param last_access   * The time of the last recorded Page View.
     */
    public void setLast_access(java.util.Calendar last_access) {
        this.last_access = last_access;
    }


    /**
     * Gets the access_duration value for this VisitSummary.
     * 
     * @return access_duration   * The total duration of the Visit in seconds. If this is null,
     * the value is unknown.
     */
    public java.lang.Integer getAccess_duration() {
        return access_duration;
    }


    /**
     * Sets the access_duration value for this VisitSummary.
     * 
     * @param access_duration   * The total duration of the Visit in seconds. If this is null,
     * the value is unknown.
     */
    public void setAccess_duration(java.lang.Integer access_duration) {
        this.access_duration = access_duration;
    }


    /**
     * Gets the ip value for this VisitSummary.
     * 
     * @return ip   * The IP address of the Contact when the performmed the visit.
     * This has been converted to long (binary) format.
     */
    public int getIp() {
        return ip;
    }


    /**
     * Sets the ip value for this VisitSummary.
     * 
     * @param ip   * The IP address of the Contact when the performmed the visit.
     * This has been converted to long (binary) format.
     */
    public void setIp(int ip) {
        this.ip = ip;
    }


    /**
     * Gets the traffic_source_type_id value for this VisitSummary.
     * 
     * @return traffic_source_type_id   * The type Id of the Traffic Source.
     */
    public int getTraffic_source_type_id() {
        return traffic_source_type_id;
    }


    /**
     * Sets the traffic_source_type_id value for this VisitSummary.
     * 
     * @param traffic_source_type_id   * The type Id of the Traffic Source.
     */
    public void setTraffic_source_type_id(int traffic_source_type_id) {
        this.traffic_source_type_id = traffic_source_type_id;
    }


    /**
     * Gets the contact_id value for this VisitSummary.
     * 
     * @return contact_id   * The Contact Id associated with the Visit.
     */
    public int getContact_id() {
        return contact_id;
    }


    /**
     * Sets the contact_id value for this VisitSummary.
     * 
     * @param contact_id   * The Contact Id associated with the Visit.
     */
    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }


    /**
     * Gets the visit value for this VisitSummary.
     * 
     * @return visit
     */
    public com.easyinsight.client.netresults.Visit getVisit() {
        return visit;
    }


    /**
     * Sets the visit value for this VisitSummary.
     * 
     * @param visit
     */
    public void setVisit(com.easyinsight.client.netresults.Visit visit) {
        this.visit = visit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VisitSummary)) return false;
        VisitSummary other = (VisitSummary) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.visit_id == other.getVisit_id() &&
            this.domain_group_id == other.getDomain_group_id() &&
            this.access_count == other.getAccess_count() &&
            ((this.last_access==null && other.getLast_access()==null) || 
             (this.last_access!=null &&
              this.last_access.equals(other.getLast_access()))) &&
            ((this.access_duration==null && other.getAccess_duration()==null) || 
             (this.access_duration!=null &&
              this.access_duration.equals(other.getAccess_duration()))) &&
            this.ip == other.getIp() &&
            this.traffic_source_type_id == other.getTraffic_source_type_id() &&
            this.contact_id == other.getContact_id() &&
            ((this.visit==null && other.getVisit()==null) || 
             (this.visit!=null &&
              this.visit.equals(other.getVisit())));
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
        _hashCode += getDomain_group_id();
        _hashCode += getAccess_count();
        if (getLast_access() != null) {
            _hashCode += getLast_access().hashCode();
        }
        if (getAccess_duration() != null) {
            _hashCode += getAccess_duration().hashCode();
        }
        _hashCode += getIp();
        _hashCode += getTraffic_source_type_id();
        _hashCode += getContact_id();
        if (getVisit() != null) {
            _hashCode += getVisit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VisitSummary.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "VisitSummary"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visit_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visit_id"));
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
        elemField.setFieldName("access_count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "access_count"));
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
        elemField.setFieldName("access_duration");
        elemField.setXmlName(new javax.xml.namespace.QName("", "access_duration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip"));
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
        elemField.setFieldName("contact_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contact_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Visit"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1/NRAPI.xsd", "Visit"));
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
