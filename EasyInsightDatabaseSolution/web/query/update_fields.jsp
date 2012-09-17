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
        t.commit();
    } catch(Exception e) {
        t.rollback();
    } finally {

        dataSession.close();
    }
%> Success!