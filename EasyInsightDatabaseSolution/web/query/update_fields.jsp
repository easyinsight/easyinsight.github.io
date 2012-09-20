<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.easyinsight.connections.database.DataConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.connections.database.data.FieldInfo" %>
<%@ page import="org.hibernate.Transaction" %>
<%
    Set<String> set = (Set<String>) request.getParameterMap().keySet();
    Session dataSession = DataConnection.getSession();
    Transaction t = dataSession.beginTransaction();
    try {
        for (String s : set) {
            if (s.startsWith("field_id_")) {
                int val = Integer.parseInt(s.substring("field_id_".length(), s.length()));
                FieldInfo ff = (FieldInfo) dataSession.get(FieldInfo.class, Long.parseLong(request.getParameter(s)));
                ff.setType(Byte.parseByte(request.getParameter("field_type_" + val)));
                ff.setFieldName(request.getParameter("field_name_" + val));
                dataSession.save(ff);

            }
        }
        t.commit(); %>
              <script type="text/javascript">jSuccess("Success!", {HorizontalPosition : 'center', VerticalPosition : 'center'}); </script>
        <script type="text/javascript">refreshQueries();$('#editQuery')[0].reset();$('#editQuery').hide();$('#newQueryButton').show();$("#queryList").show();$("#queryInfoText").show();$("#updateFields").hide();</script>
    <%
    } catch(Exception e) {
        t.rollback();
        %><script type="text/javascript">jError("An error occured: <pre><%= e.getMessage() %></pre>", {HorizontalPosition : 'center', VerticalPosition : 'center'});</script><%
    } finally {

        dataSession.close();
    }
%>