
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IssueRelationship.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IssueRelationship">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_blocks"/>
 *     &lt;enumeration value="_dependsOn"/>
 *     &lt;enumeration value="_duplicatedBy"/>
 *     &lt;enumeration value="_duplicates"/>
 *     &lt;enumeration value="_followedUpBy"/>
 *     &lt;enumeration value="_followUpFor"/>
 *     &lt;enumeration value="_injectedBy"/>
 *     &lt;enumeration value="_injects"/>
 *     &lt;enumeration value="_isBlockedBy"/>
 *     &lt;enumeration value="_isRequiredFor"/>
 *     &lt;enumeration value="_relatedTo"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IssueRelationship", namespace = "urn:types.support_2014_1.lists.webservices.netsuite.com")
@XmlEnum
public enum IssueRelationship {

    @XmlEnumValue("_blocks")
    BLOCKS("_blocks"),
    @XmlEnumValue("_dependsOn")
    DEPENDS_ON("_dependsOn"),
    @XmlEnumValue("_duplicatedBy")
    DUPLICATED_BY("_duplicatedBy"),
    @XmlEnumValue("_duplicates")
    DUPLICATES("_duplicates"),
    @XmlEnumValue("_followedUpBy")
    FOLLOWED_UP_BY("_followedUpBy"),
    @XmlEnumValue("_followUpFor")
    FOLLOW_UP_FOR("_followUpFor"),
    @XmlEnumValue("_injectedBy")
    INJECTED_BY("_injectedBy"),
    @XmlEnumValue("_injects")
    INJECTS("_injects"),
    @XmlEnumValue("_isBlockedBy")
    IS_BLOCKED_BY("_isBlockedBy"),
    @XmlEnumValue("_isRequiredFor")
    IS_REQUIRED_FOR("_isRequiredFor"),
    @XmlEnumValue("_relatedTo")
    RELATED_TO("_relatedTo");
    private final String value;

    IssueRelationship(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IssueRelationship fromValue(String v) {
        for (IssueRelationship c: IssueRelationship.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
