package com.easyinsight.servlet;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transports.http.QueryHandlerRegistry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.easyinsight.logging.LogClass;
import com.easyinsight.api.EIWSDLQueryHandler;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 1:02:40 PM
 */
public class EICXFServlet extends CXFServlet {
    private static EICXFServlet servlet;

    public static EICXFServlet instance() {
        return servlet;
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        QueryHandlerRegistry registry = bus.getExtension(QueryHandlerRegistry.class);
        EIWSDLQueryHandler handler = new EIWSDLQueryHandler();
        handler.setBus(bus);
        registry.registerHandler(handler, 0);
    }
}
