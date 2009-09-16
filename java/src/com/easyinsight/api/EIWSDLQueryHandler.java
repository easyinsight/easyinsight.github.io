package com.easyinsight.api;

/**
 * User: James Boe
 * Date: Mar 12, 2009
 * Time: 3:07:56 PM
 */
import java.util.Collection;
 	import java.util.List;
 	import java.util.Map;

 	import javax.wsdl.Definition;
 	import javax.wsdl.Port;
 	import javax.wsdl.extensions.ExtensibilityElement;
 	import javax.wsdl.extensions.schema.SchemaReference;
 	import javax.xml.namespace.QName;

 	import org.apache.cxf.Bus;
 	import org.apache.cxf.service.model.EndpointInfo;
 	import org.apache.cxf.tools.common.extensions.soap.SoapAddress;
 	import org.apache.cxf.tools.util.SOAPBindingUtil;
 	import org.apache.cxf.transport.http.WSDLQueryHandler;

 	/**
 	 * This WSDLQuery handler will update the SOAP address of the WSDL by replacing
 	 * the existing address by the given URL.
 	 * @author Florent Benoit
 	 */
 	public class EIWSDLQueryHandler extends WSDLQueryHandler {

 	    /**
 	     * URI of the WSDL port binding.
 	     */
 	    private String portBindingURI = null;



 	    /**
 	     * Build a new WSDL Query Handler that will update the port binding with the
 	     * given URL.
 	     * @param bus the bus used by this handler.
 	     * @param portBindingURI the URI of the port binding
 	     */
 	    public EIWSDLQueryHandler() {
 	        //super(bus);
 	    }

         @Override
	    @SuppressWarnings("unchecked")
	    protected void updateDefinition(final Definition definition, final Map<String, Definition> done,
 	            final Map<String, SchemaReference> doneSchemas, final String base, final EndpointInfo endpointInfo) {


 	    }

 	}
