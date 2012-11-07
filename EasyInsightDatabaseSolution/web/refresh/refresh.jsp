<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.data.Query" %>
<%@ page import="com.easyinsight.connections.database.data.FieldInfo" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<%
    String token = request.getParameter("refreshKey");
    final String callDataID = request.getParameter("callDataID");
    System.out.println("call data ID = " + callDataID);
    Session dataSession = DataConnection.getSession();
    try {
        final Query q = Query.byRefreshToken(dataSession, token);
        q.setFieldInfos(new ArrayList<FieldInfo>(q.getFieldInfos()));

        Thread t = new Thread(new Runnable() {

            public void run() {
                Session ds = DataConnection.getSession();
                try {
                    ds.beginTransaction();
                    q.doUpload(ds, callDataID);
                    ds.getTransaction().commit();
                } catch (Exception e) {
                    ds.getTransaction().rollback();
                    throw new RuntimeException(e);
                } finally {
                    if (ds != null)
                        ds.close();
                }

            }
        });
        t.setDaemon(false);
        t.start();


    } catch (Exception e) {
        e.printStackTrace();
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