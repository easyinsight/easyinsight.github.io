package com.easyinsight.billing;

/**
 * User: James Boe
 * Date: Aug 3, 2008
 * Time: 1:56:43 PM
 */
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.log4j.Logger;

/**
 * This class implements an Axis handler which logs the SOAP requests sent
 * from the client to FPS and the reponses received from FPS.
 */
public class SOAPPrintHandler extends BasicHandler {

	private static final long serialVersionUID = 1L;

	// Initializing the log4j Logger object.
	private static Logger log = Logger.getLogger(SOAPPrintHandler.class.getName());

	public void invoke(MessageContext msgCxt) throws AxisFault {
		Message request = msgCxt.getRequestMessage();
		Message response = msgCxt.getResponseMessage();

		if (request != null && response == null) {
			log.info("\n\n************SOAP Request: ********\n" +
				request.getSOAPPartAsString());
		}
		if (response != null) {
			log.info("\n\n***************SOAP Response: *************\n" +
				response.getSOAPPartAsString());
		}
	}
}