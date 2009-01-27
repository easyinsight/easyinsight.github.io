package com.easyinsight.billing;

/**
 * User: James Boe
 * Date: Aug 3, 2008
 * Time: 1:04:32 PM
 */
import javax.xml.rpc.JAXRPCException;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.addressing.Action;
import org.apache.axis.message.addressing.AddressingHeaders;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.axis.message.addressing.MessageID;
import org.apache.axis.message.addressing.uuid.AxisUUIdGenerator;
import org.apache.axis.types.URI;
import org.apache.ws.addressing.uuid.UUIdGeneratorFactory;

/**
 * This class adds the addressing part of the SOAP header.
 *
 * @author FPS SDK Team
 *
 */
public class AmazonFPSAddressingHandler extends BasicHandler {

	/**
	 * Invoke is called to do the actual work of the Handler object.
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext msgCtxt) throws AxisFault {

		AddressingHeaders headers = new AddressingHeaders();
		try {
			headers.setAction(new Action(new URI("urn:aws:actions:AmazonFPS:2007:01:08:" +
					msgCtxt.getOperation().getName())));
			headers.setReplyTo(new EndpointReference("http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous"));

			String toUrl = (String) msgCtxt.getProperty(org.apache.axis.MessageContext.TRANS_URL);
			headers.setTo(new AttributedURI(toUrl));

			String msgId = UUIdGeneratorFactory.createUUIdGenerator(AxisUUIdGenerator.class).generateUUId();
			headers.setMessageID(new MessageID(new URI("uuid:" + msgId)));
			headers.toEnvelope(msgCtxt.getMessage().getSOAPPart().getEnvelope());
		} catch (Exception e) {
            throw new JAXRPCException(e);
		}
	}
}

