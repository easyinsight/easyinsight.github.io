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

// 	        // This code is from OW Celtix ServletServerTransport class (that was
// 	        // updating the address of the Port binding)
// 	        // And this is not done anymore in CXF
// 	        QName serviceName = endpointInfo.getService().getName();
// 	        javax.wsdl.Service serv = definition.getService(serviceName);
// 	        Collection<Port> ports = serv.getPorts().values();
// 	        for (Port port : ports) {
// 	            List<?> exts = port.getExtensibilityElements();
// 	            if (exts != null && exts.size() > 0) {
// 	                ExtensibilityElement el = (ExtensibilityElement) exts.get(0);
// 	                if (SOAPBindingUtil.isSOAPAddress(el)) {
// 	                    SoapAddress add = SOAPBindingUtil.getSoapAddress(el);
//                         String existingURI = add.getLocationURI();
//
//                         int indexHttp = existingURI.indexOf("8080");
//                         int indexHttps = existingURI.indexOf("8443");
//                         String newURI;
//                         if(indexHttp != -1)
//                            newURI = "http://www.easy-insight.com";
//                         else
//                            newURI = "https://www.easy-insight.com";
//                         newURI = newURI + existingURI.substring(indexHttps + 4);
//
//                        add.setLocationURI(newURI);
//	              }
// 	            }
// 	        }
//
// 	        super.updateDefinition(definition, done, doneSchemas, base, endpointInfo);

 	    }

 	}
