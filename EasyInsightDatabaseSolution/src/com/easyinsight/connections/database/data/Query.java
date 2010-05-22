package com.easyinsight.connections.database.data;

import com.easyinsight.connections.database.DataConnection;
import com.easyinsight.api.unchecked.*;

import javax.persistence.*;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.util.*;
import java.util.Date;
import java.sql.*;

import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 27, 2010
 * Time: 9:17:54 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Query {

    public static Timer getScheduler() {
        return scheduler;
    }
    
    private static Timer scheduler = startTimer();

    private static Timer startTimer() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timer t = new Timer(true);
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Session s = null;
                try {
                    s = DataConnection.getSession();
                    List<Query> queries = s.createQuery("from Query where schedule = true").list();
                    for(Query query : queries) {
                         try {
                            query.doUpload();
                        } catch (DatatypeConfigurationException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if(s != null)
                        s.close();
                }
            }
        };
        t.scheduleAtFixedRate(tt, c.getTime(), 24 * 60 * 60 * 1000);
        return t;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @Column(length = 1024, precision = 1024)
    private String query;
    private boolean schedule;
    private boolean append;
    private String dataSource;
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private ConnectionInfo connectionInfo;

    public Query() {
    }

    public Query(Map<String, String[]> parameterMap) {
        update(parameterMap);
    }

    public void update(Map<String, String[]> parameterMap) {
        setQuery(parameterMap.get("queryValue")[0]);
        setDataSource(parameterMap.get("queryDataSource")[0]);
        setName(parameterMap.get("queryName")[0]);
        if(parameterMap.get("schedule") != null && parameterMap.get("schedule")[0].equals("on")) {
            setSchedule(true);
        } else {
            setSchedule(false);
        }
        if(parameterMap.get("uploadType") != null && parameterMap.get("uploadType")[0].equals("append")) {
            setAppend(true);
        } else {
            setAppend(false);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ResultSet executeQuery(Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(getQuery());
        return statement.executeQuery();
    }

    public ResultSet executeQuery(Connection conn, int limit) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(getQuery());
        statement.setMaxRows(limit);
        return statement.executeQuery();
    }


    public static List<Query> all() throws SQLException {
        Session session = DataConnection.getSession();
        try {
            return session.createQuery("from Query").list();
        }
        finally {
            session.close();
        }
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void doUpload() throws SQLException, DatatypeConfigurationException {
        Connection conn = null;
        ResultSet rs = null;
        try {
            List<Row> rows = new LinkedList<Row>();

            conn = this.getConnectionInfo().createConnection();
            rs = this.executeQuery(conn);
            while(rs.next()) {
                Row row = new Row();
                for(int column = 1;column <= rs.getMetaData().getColumnCount();column++) {
                    String key = rs.getMetaData().getColumnName(column);
                    switch (rs.getMetaData().getColumnType(column)) {
                        case Types.BIGINT:
                        case Types.TINYINT:
                        case Types.SMALLINT:
                        case Types.INTEGER:
                        case Types.NUMERIC:
                        case Types.FLOAT:
                        case Types.DOUBLE:
                        case Types.DECIMAL:
                        case Types.REAL:    
                            NumberPair np = new NumberPair();
                            np.setKey(key);
                            np.setValue(rs.getDouble(column));
                            row.getNumberPairs().add(np);
                            break;

                        case Types.BOOLEAN:
                        case Types.BIT:
                            StringPair bp = new StringPair();
                            bp.setKey(key);
                            bp.setValue(String.valueOf(rs.getBoolean(column)));
                            row.getStringPairs().add(bp);
                            break;

                        case Types.CHAR:
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.VARCHAR:
                            StringPair sp = new StringPair();
                            sp.setKey(key);
                            sp.setValue(rs.getString(column));
                            row.getStringPairs().add(sp);
                            break;

                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP:
                            Date date = rs.getDate(column);
                            DatePair dp = new DatePair();
                            if(date != null) {
                                GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
                                cal.setTime(date);
                                XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                                dp.setValue(xmlCal);
                            } else {
                                dp.setValue(null);
                            }
                            dp.setKey(key);
                            row.getDatePairs().add(dp);
                            break;
                        default:
                            throw new RuntimeException("This data type (" + rs.getMetaData().getColumnTypeName(column) + ") is not supported in Easy Insight.");
                    }
                }
                rows.add(row);
            }
            BasicAuthUncheckedPublishServiceService service = new BasicAuthUncheckedPublishServiceService();
            BasicAuthUncheckedPublish port = service.getBasicAuthUncheckedPublishServicePort();
            EIUser user = EIUser.instance();
            if(user == null)
                throw new RuntimeException("You need to enter your credentials first!");
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getPublicKey());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getSecretKey());
            if(isAppend()) {
                port.addRows(this.getDataSource(), rows);
            } else {
                port.replaceRows(this.getDataSource(), rows);
            }

        } finally {
            if(rs != null)
                rs.close();
            if(conn != null)
                conn.close();
        }

    }
}
