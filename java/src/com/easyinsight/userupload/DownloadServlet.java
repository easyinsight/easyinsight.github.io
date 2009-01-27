package com.easyinsight.userupload;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.solutions.SolutionArchiveOperation;
import com.easyinsight.export.ExcelOperation;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import java.io.*;

/**
 * User: James Boe
 * Date: Oct 11, 2008
 * Time: 1:15:47 PM
 */
public class DownloadServlet extends HttpServlet {

    public static final int BUFSIZE = 512;

    public static final int PNG_EXPORT_OPERATION = 1;
    public static final int SOLUTION_OPERATION = 2;
    public static final int EXCEL_OPERATION = 3;

    public DownloadServlet() {
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {
            String userName = req.getParameter("userName");
            String password = req.getParameter("password");
            int operation = Integer.parseInt(req.getParameter("operation"));
            long fileID = Integer.parseInt(req.getParameter("fileID"));

            ServletOutputStream op;
            DataInputStream in;
            FileOperation fileOperation = getFileOperation(operation);
            long userID = SecurityUtil.authenticate(userName, password);
            byte[] file = fileOperation.retrieve(fileID, userID);

            int                 length   = 0;
            op = resp.getOutputStream();
            ServletContext context  = getServletConfig().getServletContext();
            //String              mimetype = context.getMimeType( filename );

            //
            //  Set the response and go!
            //
            //
            //resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
            resp.setContentType("application/octet-stream");
            resp.setContentLength( (int)file.length );
            //resp.setHeader( "Content-Disposition", "attachment; filename=\"" + originalFilename + "\"" );

            //
            //  Stream to the requester.
            //
            byte[] bbuf = new byte[BUFSIZE];
            in = new DataInputStream(new ByteArrayInputStream(file));

            while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                op.write(bbuf,0,length);
            }
            in.close();
            op.flush();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }


        //op.close();

    }

    private FileOperation getFileOperation(int operation) {
        FileOperation fileOperation = null;
        switch (operation) {
            case PNG_EXPORT_OPERATION:
                fileOperation = new PNGExportOperation();
                break;
            case SOLUTION_OPERATION:
                fileOperation = new SolutionArchiveOperation();
                break;
            case EXCEL_OPERATION:
                fileOperation = new ExcelOperation();
                break;
        }
        return fileOperation;
    }
}
