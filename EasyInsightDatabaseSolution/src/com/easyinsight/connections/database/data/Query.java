package com.easyinsight.connections.database.data;

import com.easyinsight.connections.database.DataConnection;

import javax.persistence.*;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.Date;
import java.sql.*;

import com.easyinsight.helper.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 27, 2010
 * Time: 9:17:54 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Query {

    private static SecureRandom random = new SecureRandom();

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
                    for (Query query : queries) {
                        try {
                            Transaction t = s.beginTransaction();
                            try {
                                query.doUpload(s);
                            } finally {
                                t.commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (s != null)
                        s.close();
                }
            }
        };
        t.scheduleAtFixedRate(tt, c.getTime(), 24 * 60 * 60 * 1000);
        return t;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 30000, precision = 30000)
    private String query;
    private boolean schedule;
    private boolean append;
    private String dataSource;
    private String name;

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
    }

    private String refreshKey;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private ConnectionInfo connectionInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "query", cascade = CascadeType.ALL)
    @OrderBy("startTime desc")
    private List<UploadResult> uploadResults;

    public Query() {
    }

    public Query(Map<String, String[]> parameterMap) {
        update(parameterMap);
    }

    public void update(Map<String, String[]> parameterMap) {
        setQuery(parameterMap.get("queryValue")[0]);
        setDataSource(parameterMap.get("queryDataSource")[0]);
        setName(parameterMap.get("queryDataSource")[0]);
        if (parameterMap.get("schedule") != null && parameterMap.get("schedule")[0].equals("on")) {
            setSchedule(true);
        } else {
            setSchedule(false);
        }
        if (parameterMap.get("uploadType") != null && parameterMap.get("uploadType")[0].equals("append")) {
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
        statement.setFetchSize(Integer.MIN_VALUE);
        return statement.executeQuery();
    }

    public ResultSet executeQuery(Connection conn, int limit) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(getQuery());
        statement.setMaxRows(limit);
        return statement.executeQuery();
    }


    public static List<Query> all(Session session) throws SQLException {
        return session.createQuery("from Query").list();
    }

    public static Query byRefreshToken(Session session, String refreshKey) throws SQLException {
        List<Query> list = session.createQuery("from Query where refreshKey = ?").setString(0, refreshKey).list();
        if (list.size() == 0) return null;
        return list.get(0);
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "query", cascade = CascadeType.ALL)
    private List<FieldInfo> fieldInfos;

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
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

    public List<UploadResult> getUploadResults() {
        return uploadResults;
    }

    public void setUploadResults(List<UploadResult> uploadResults) {
        this.uploadResults = uploadResults;
    }

    public void doUpload(Session session) throws SQLException, DatatypeConfigurationException {
        Connection conn = null;
        ResultSet rs = null;
        TransactionTarget dataSourceTarget = null;
        UploadResult result = new UploadResult();
        result.setStartTime(new Date());
        result.setQuery(this);

        Map<String, FieldInfo> map = new HashMap<String, FieldInfo>();
        for (FieldInfo f : getFieldInfos()) {
            map.put(f.getColumnName(), f);
        }

        try {
            EIUser user = EIUser.instance();
            if (user == null)
                throw new RuntimeException("You need to enter your credentials first!");
            conn = this.getConnectionInfo().createConnection();
            rs = this.executeQuery(conn);
            if (this.getRefreshKey() == null) {
                this.setRefreshKey(new BigInteger(130, random).toString(32));
            }
            System.out.println(this.getRefreshKey());
            DataSourceFactory dataSourceFactory = APIUtil.defineDataSource(this.getDataSource(), user.getPublicKey(), user.getSecretKey());
            dataSourceFactory.setRefreshUrl(user.getCurrentUrl());
            dataSourceFactory.setRefreshKey(this.getRefreshKey());
            for (int column = 1; column <= rs.getMetaData().getColumnCount(); column++) {
                String key = rs.getMetaData().getColumnName(column);
                String fieldName = key;
                if(map.containsKey(key))
                    fieldName = map.get(key).getFieldName();

                if (map.containsKey(key) && map.get(key).getType() != FieldInfo.DEFAULT) {

                    switch (map.get(key).getType()) {
                        case FieldInfo.MEASURE:
                            dataSourceFactory.addMeasure(fieldName);

                            break;
                        case FieldInfo.GROUPING:
                            dataSourceFactory.addGrouping(fieldName);
                            break;
                        case FieldInfo.DATE:
                            dataSourceFactory.addDate(fieldName);
                            break;
                        default:
                            break;
                    }
                } else {
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
                            dataSourceFactory.addMeasure(fieldName);
                            break;

                        case Types.BOOLEAN:
                        case Types.BIT:
                        case Types.CHAR:
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                            dataSourceFactory.addGrouping(fieldName);
                            break;

                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP:
                            dataSourceFactory.addDate(fieldName);
                            break;
                        default:
                            throw new RuntimeException("This data type (" + rs.getMetaData().getColumnTypeName(column) + ") is not supported in Easy Insight. Type value: " + rs.getMetaData().getColumnType(column));
                    }
                }
            }

            DataSourceOperationFactory operationFactory = dataSourceFactory.defineDataSource();
            dataSourceTarget = this.isAppend() ? operationFactory.addRowsTransaction() : operationFactory.replaceRowsTransaction();
            dataSourceTarget.beginTransaction();

            while (rs.next()) {
                DataRow row = dataSourceTarget.newRow();
                for (int column = 1; column <= rs.getMetaData().getColumnCount(); column++) {
                    String key = rs.getMetaData().getColumnName(column);
                    String fieldName = key;
                    if(map.containsKey(key))
                        fieldName = map.get(key).getFieldName();
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
                            row.addValue(fieldName, rs.getDouble(column));
                            break;

                        case Types.BOOLEAN:
                        case Types.BIT:
                            row.addValue(fieldName, String.valueOf(rs.getBoolean(column)));
                            break;

                        case Types.CHAR:
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                            row.addValue(fieldName, rs.getString(column));
                            break;

                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIMESTAMP:
                            Date date;
                            try {
                                date = rs.getTimestamp(column);
                            } catch (SQLException e) {
                                // catching bad dates
                                date = null;
                            }
                            row.addValue(fieldName, date);
                            break;
                        default:
                            throw new RuntimeException("This data type (" + rs.getMetaData().getColumnTypeName(column) + ") is not supported in Easy Insight. Type value: " + rs.getMetaData().getColumnType(column));
                    }
                }
            }
            dataSourceTarget.commit();
            result.setEndTime(new Date());
            result.setSuccess(true);
            session.save(result);

        } catch (SQLException e) {
            result.setEndTime(new Date());
            result.setSuccess(false);
            result.setMessage(e.getMessage().substring(0, Math.min(4096, e.getMessage().length())));
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.setStackTrace(sw.toString().substring(0, Math.min(4096, sw.toString().length())));
            session.save(result);
            if (dataSourceTarget != null)
                dataSourceTarget.rollback();
            throw e;
        } catch (RuntimeException e) {
            result.setEndTime(new Date());
            result.setSuccess(false);
            result.setMessage(e.getMessage().substring(0, Math.min(1024 * 1024, e.getMessage().length())));
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.setStackTrace(sw.toString().substring(0, Math.min(1024 * 1024, sw.toString().length())));
            session.save(result);
            if (dataSourceTarget != null)
                dataSourceTarget.rollback();
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (conn != null)
                conn.close();
        }

    }

    public void discoverFields(Session session) throws SQLException {
        Transaction t = session.beginTransaction();
        Map<String, FieldInfo> map = new HashMap<String, FieldInfo>();
        for (FieldInfo f : getFieldInfos()) {
            map.put(f.getColumnName(), f);
        }

        ResultSet rs = executeQuery(getConnectionInfo().createConnection(), 1);
        for (int column = 1; column <= rs.getMetaData().getColumnCount(); column++) {
            String key = rs.getMetaData().getColumnName(column);
            if (!map.containsKey(key)) {
                FieldInfo f = new FieldInfo();
                f.setColumnName(key);
                f.setFieldName(key);
                f.setQuery(this);
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
                        f.setType(FieldInfo.MEASURE);
                        break;

                    case Types.BOOLEAN:
                    case Types.BIT:
                        f.setType(FieldInfo.GROUPING);
                        break;

                    case Types.CHAR:
                    case Types.NCHAR:
                    case Types.NVARCHAR:
                    case Types.VARCHAR:
                    case Types.LONGVARCHAR:
                        f.setType(FieldInfo.GROUPING);
                        break;

                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        f.setType(FieldInfo.DATE);
                        break;
                    default:
                        f.setType(FieldInfo.DEFAULT);
                }
                this.getFieldInfos().add(f);
                session.persist(f);
            }

        }
        session.persist(this);
        t.commit();
    }

}
