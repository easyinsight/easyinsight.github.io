package com.easyinsight.salesautomation;

import com.easyinsight.logging.LogClass;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/27/14
 * Time: 1:45 PM
 */
public class ApproveSalesEmailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        JSONObject approvalObject;
        try {
            InputStream is = req.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String jsonString = writer.toString();
            System.out.println(jsonString);
            approvalObject = (JSONObject) parser.parse(new ByteArrayInputStream(jsonString.getBytes()));
            String approvalType = approvalObject.get("approval_type").toString();
            if ("assign".equals(approvalType)) {
                String salesRep = approvalObject.get("sales_rep").toString();
                List ids = (List) approvalObject.get("account_ids");
                List<Long> accountIDs = new ArrayList<>();
                for (Object o : ids) {
                    accountIDs.add(Long.parseLong(o.toString()));
                }
                String email;
                if ("Cendie Lee".equals(salesRep)) {
                    email = "cendie@easy-insight.com";
                } else if ("Alan Baldwin".equals(salesRep)) {
                    email = "abaldwin@easy-insight.com";
                } else if ("James Boe".equals(salesRep)) {
                    email = "jboe@easy-insight.com";
                } else {
                    throw new RuntimeException();
                }
                new SalesAutomation().sendEmailsToSelected(accountIDs, salesRep, email);
            } else if ("ignore".equals(approvalType)) {
                List ids = (List) approvalObject.get("account_ids");
                List<Long> accountIDs = new ArrayList<>();
                for (Object o : ids) {
                    accountIDs.add(Long.parseLong(o.toString()));
                }
                new SalesAutomation().noEmails(accountIDs);
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
