package com.easyinsight.analysis;

import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 27, 2010
 * Time: 10:55:55 AM
 */
public class ZipUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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

            String userName = req.getParameter("userName");
            String password = req.getParameter("password");

            //if (SecurityUtil.authenticateToResponse(userName, password).getAccountType() == Account.ADMINISTRATOR) {

            // Iterate through the incoming request data
			while( iter.hasNext() )
			{
				// Get the current item in the iteration
				item = ( FileItem )iter.next();

				// If the current item is an HTML form field
				if( !item.isFormField() )
				{
                    byte[] bytes = item.get();

                    new ZipGeocodeCache().saveFile(bytes);

                    out.print("<successful/>");

                    // throw the content into the database?
                    // at this point, it's NOT a data set, it's a blob of some text

                    // and for that matter, it could be a CSV, it could be an excel file, etc...

                    // so where do we store the damn thing, given that we'll need to do some asynchronous steps from here...

                    // store in a "user_upload" table in the db, one that'll get purged frequently

                    // so we should return in the response the ID of what was added to the databas

                    // then THAT is what's used in the upload service for doing the data prep work

                    /*String initDirectory = "../webapps/DMS/WEB-INF/uploads";
                    File initDir = new File(initDirectory);
                    if (!initDir.exists()) {
                        initDir.mkdir();
                    }
                    String userDirectory = initDir.getAbsolutePath() + File.separator + accountID;
                    File userDir = new File(userDirectory);
                    if (!userDir.exists()) {
                        userDir.mkdir();
                    }
                    String path = userDir.getAbsolutePath() + File.separator + item.getName();
                    disk = new File(path);
                    LogClass.debug("writing out " + disk.getName());
                    item.write( disk );*/
                    // Return an XML node with the field name and value
				}
			}
            //}

			// Close off the response XML data and stream
			out.print( "</response>" );
			out.close();
		// Rudimentary handling of any exceptions
		// TODO: Something useful if an error occurs
		} catch( FileUploadException fue ) {
			LogClass.error(fue);
		} catch( IOException ioe ) {
			LogClass.error(ioe);
		} catch( Throwable e ) {
			LogClass.error(e);
		}
    }
}
