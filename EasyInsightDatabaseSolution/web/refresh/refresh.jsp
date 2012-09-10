<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<!DOCTYPE html>
<%
    String token = request.getParameter("refreshKey");
    Session dataSession = null;
    try {
        dataSession = DataConnection.getSession();
        final Query q = Query.byRefreshToken(dataSession, token);
        if(q != null) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    Session ds = null;
                    try {
                        ds = DataConnection.getSession();
                    q.doUpload(ds);
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        if(ds != null)
                            ds.close();
                    }

                }
            });
            t.setDaemon(false);
            t.start();
        }

    } catch(Exception e) {
        throw new RuntimeException(e);
    } finally {
        dataSession.close();
    }
%>
<html>
<head>
    <title>Refresh</title>
</head>
<body>
Refreshing....
</body>
</html>