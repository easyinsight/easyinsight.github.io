
package com.easyinsight.datafeeds.netsuite.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AsyncResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AsyncResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AsyncResult", namespace = "urn:messages_2014_1.platform.webservices.netsuite.com")
@XmlSeeAlso({
    AsyncGetListResult.class,
    AsyncAddListResult.class,
    AsyncSearchResult.class,
    AsyncUpsertListResult.class,
    AsyncInitializeListResult.class,
    AsyncDeleteListResult.class,
    AsyncUpdateListResult.class
})
public abstract class AsyncResult {


}
