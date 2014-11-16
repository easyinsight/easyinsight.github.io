package com.easyinsight.scheduler;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.DashboardPDF;
import com.easyinsight.logging.LogClass;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.PreparedStatement;

/**
 * User: jamesboe
 * Date: 7/8/13
 * Time: 1:26 PM
 */
public class UploadExportImageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req);
        /*HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req);
        String imageString = wrappedRequest.getParameter("imgBase64");
        imageString = imageString.substring("data:image/png;base64,".length());*/
        byte[] contentData =  IOUtils.toByteArray(req.getInputStream());
        byte[] decodedData = Base64.decodeBase64(contentData);

        Long id = Long.parseLong(wrappedRequest.getParameter("id"));
        int height = (int) Double.parseDouble(wrappedRequest.getParameter("height"));
        int width = (int) Double.parseDouble(wrappedRequest.getParameter("width"));
        String userName = wrappedRequest.getParameter("userName");
        String password = wrappedRequest.getParameter("password");

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE image_selenium_trigger SET image_state = ?, image_preferred_width = ?," +
                    "image_preferred_height = ?, image_response = ? WHERE image_selenium_trigger_id = ? AND image_selenium_trigger.selenium_username = ? AND " +
                    "image_selenium_trigger.selenium_password = ?");
            updateStmt.setInt(1, DashboardPDF.SUCCESS);
            updateStmt.setInt(2, width);
            updateStmt.setInt(3, height);
            updateStmt.setBytes(4, decodedData);
            updateStmt.setLong(5, id);
            updateStmt.setString(6, userName);
            updateStmt.setString(7, password);
            updateStmt.executeUpdate();
            updateStmt.close();
            MessageQueue messageQueue = SQSUtils.connectToQueue("EISeleniumProgress", "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            System.out.println("Sending response of " + id);
            messageQueue.sendMessage(String.valueOf(id));
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
