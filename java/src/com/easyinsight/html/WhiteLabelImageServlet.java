package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.PreferencesService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: 5/24/12
 * Time: 2:50 PM
 */
public class WhiteLabelImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String subdomain = req.getParameter("subdomain");
        if (subdomain != null) {
            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);
                PreparedStatement queryStmt = conn.prepareStatement("SELECT IMAGE_BYTES FROM ACCOUNT LEFT JOIN USER_IMAGE ON ACCOUNT.login_image = USER_IMAGE.user_image_id where account.subdomain = ?");
                queryStmt.setString(1, subdomain);
                ResultSet rs = queryStmt.executeQuery();
                if(rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    conn.commit();
                    resp.setContentType("image/png");
                    resp.setHeader("Content-disposition","inline; filename=" + subdomain + ".png" );
                    OutputStream out = resp.getOutputStream();

                    // Copy the contents of the file to the output stream
                    byte[] buf = new byte[1024];
                    int byteCount = 0;
                    int count;
                    while ((count = in.read(buf)) >= 0) {
                        out.write(buf, 0, count);
                        byteCount += count;
                    }
                    resp.setContentLength(byteCount);
                    resp.getOutputStream().flush();
                }
                queryStmt.close();
            } catch (Exception e) {
                LogClass.error(e);
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }
        }
    }
}
