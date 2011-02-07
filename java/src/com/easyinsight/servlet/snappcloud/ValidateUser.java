package com.easyinsight.servlet.snappcloud;

import com.easyinsight.users.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Nov 22, 2010
 * Time: 11:06:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValidateUser extends HttpServlet {

    private static final String RESPONSE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\t<response>\n" +
            "\t\t<isValid>{0}</isCreated>\n" +
            "\t</response>";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("username");
        System.out.println(name);
        UserService service = new UserService();
        boolean success = service.doesAccountExist(name);
        response.setStatus(success ? 422 : 200);
        response.getWriter().print(MessageFormat.format(RESPONSE_XML, success));
        response.flushBuffer();
    }
}
