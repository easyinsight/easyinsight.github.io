
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransactionPaymentEventHoldReason.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TransactionPaymentEventHoldReason">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="_authorizationDecline"/>
 *     &lt;enumeration value="_cardExpired"/>
 *     &lt;enumeration value="_cardInvalid"/>
 *     &lt;enumeration value="_externalFraudRejection"/>
 *     &lt;enumeration value="_externalFraudReview"/>
 *     &lt;enumeration value="_forwardedToPayerAuthentication"/>
 *     &lt;enumeration value="_gatewayError"/>
 *     &lt;enumeration value="_overridenBy"/>
 *     &lt;enumeration value="_partnerDecline"/>
 *     &lt;enumeration value="_systemError"/>
 *     &lt;enumeration value="_verbalAuthorizationRequested"/>
 *     &lt;enumeration value="_verificationRejection"/>
 *     &lt;enumeration value="_verificationRequired"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TransactionPaymentEventHoldReason", namespace = "urn:types.sales_2014_1.transactions.webservices.netsuite.com")
@XmlEnum
public enum TransactionPaymentEventHoldReason {

    @XmlEnumValue("_authorizationDecline")
    AUTHORIZATION_DECLINE("_authorizationDecline"),
    @XmlEnumValue("_cardExpired")
    CARD_EXPIRED("_cardExpired"),
    @XmlEnumValue("_cardInvalid")
    CARD_INVALID("_cardInvalid"),
    @XmlEnumValue("_externalFraudRejection")
    EXTERNAL_FRAUD_REJECTION("_externalFraudRejection"),
    @XmlEnumValue("_externalFraudReview")
    EXTERNAL_FRAUD_REVIEW("_externalFraudReview"),
    @XmlEnumValue("_forwardedToPayerAuthentication")
    FORWARDED_TO_PAYER_AUTHENTICATION("_forwardedToPayerAuthentication"),
    @XmlEnumValue("_gatewayError")
    GATEWAY_ERROR("_gatewayError"),
    @XmlEnumValue("_overridenBy")
    OVERRIDEN_BY("_overridenBy"),
    @XmlEnumValue("_partnerDecline")
    PARTNER_DECLINE("_partnerDecline"),
    @XmlEnumValue("_systemError")
    SYSTEM_ERROR("_systemError"),
    @XmlEnumValue("_verbalAuthorizationRequested")
    VERBAL_AUTHORIZATION_REQUESTED("_verbalAuthorizationRequested"),
    @XmlEnumValue("_verificationRejection")
    VERIFICATION_REJECTION("_verificationRejection"),
    @XmlEnumValue("_verificationRequired")
    VERIFICATION_REQUIRED("_verificationRequired");
    private final String value;

    TransactionPaymentEventHoldReason(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TransactionPaymentEventHoldReason fromValue(String v) {
        for (TransactionPaymentEventHoldReason c: TransactionPaymentEventHoldReason.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
