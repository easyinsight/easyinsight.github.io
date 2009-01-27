package com.easyinsight.solutions;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.logging.LogClass;

/**
 * User: James Boe
 * Date: Oct 11, 2008
 * Time: 6:28:43 PM
 */
public class SolutionUploadServlet extends HttpServlet {
    public void doPost( HttpServletRequest req, HttpServletResponse res )
	{
        // Setup the various objects used during this upload operation
		// Commons file upload classes are specifically instantiated
		FileItem item;
		FileItemFactory factory = new DiskFileItemFactory();
		Iterator iter;
		List items;
		ServletFileUpload upload = new ServletFileUpload( factory );
		ServletOutputStream out;

		try
		{
			// Parse the incoming HTTP request
			// Commons takes over incoming request at this point
			// Get an iterator for all the data that was sent
			items = upload.parseRequest( req );
			iter = items.iterator();

			// Set a response content type
			res.setContentType( "text/xml" );

			// Setup the output stream for the return XML data
			out = res.getOutputStream();
			out.print( "<response>" );

            //String userName = req.getParameter("userName");
            //String password = req.getParameter("password");
            long solutionID = Long.parseLong(req.getParameter("solutionID"));

            //long userID = SecurityUtil.authenticate(userName, password);

            // Iterate through the incoming request data
			while( iter.hasNext() )
			{
				// Get the current item in the iteration
				item = ( FileItem )iter.next();



                // If the current item is an HTML form field
				if( !item.isFormField() )
				{
                    byte[] bytes = item.get();

                    new SolutionService().addSolutionArchive(bytes, solutionID, item.getName());

                    out.print("<successful/>");
				}
			}

			// Close off the response XML data and stream
			out.print( "</response>" );
			out.close();
		// Rudimentary handling of any exceptions
		// TODO: Something useful if an error occurs
		} catch( FileUploadException fue ) {
			LogClass.error(fue);
		} catch( IOException ioe ) {
			LogClass.error(ioe);
		} catch( Exception e ) {
			LogClass.error(e);
		}
	}
}
