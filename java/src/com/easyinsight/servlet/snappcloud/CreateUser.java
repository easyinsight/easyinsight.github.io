package com.easyinsight.servlet.snappcloud;

import com.easyinsight.users.UserService;
import com.easyinsight.users.UserTransferObject;
import com.easyinsight.users.AccountTransferObject;
import com.easyinsight.users.Account;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Nov 23, 2010
 * Time: 9:01:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateUser extends HttpServlet {

    private static final String RESPONSE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\t<response>\n" +
            "\t\t<isCreated>true</isCreated>\n" +
            "\t\t<DownloadUrl>www.yourloginurl.com</DownloadUrl>\n" +
            "\t</response>";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = true;
        try {
            String userName = request.getParameter("username");
            String firstName = request.getParameter("customerfirstname");
            String lastName = request.getParameter("customerlastname");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String salt = request.getParameter("salt");
            String realPassword = request.getParameter("realPassword");
            String snappCloudId = request.getParameter("snappcloudid");
            UserService service = new UserService();
            UserTransferObject user = new UserTransferObject();
            user.setUserName(userName);
            user.setFirstName(firstName);
            user.setName(lastName);
            user.setEmail(email);
            user.setAccountAdmin(true);
            user.setOptInEmail(true);
            AccountTransferObject account = new AccountTransferObject();
            account.setName(userName);
            account.setAccountType(Account.PROFESSIONAL);
            account.setSnappCloudId(snappCloudId);
            service.createAccount(user, account, password, null, Account.SNAPPCLOUD, salt);
            System.out.println("email: " + email);
            System.out.println("firstname: " + firstName);
            System.out.println("lastname: " + lastName);
            System.out.println("salt: " + salt);
            System.out.println("password: " + password);
            System.out.println("snappcloudid: " + snappCloudId);
            System.out.println("realPassword: " + realPassword);
        } catch(Exception e) {
            success = false;
        }
        response.setStatus(success ? 200 : 422);
        response.getWriter().print(MessageFormat.format(RESPONSE_XML, success));
        response.flushBuffer();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
}
