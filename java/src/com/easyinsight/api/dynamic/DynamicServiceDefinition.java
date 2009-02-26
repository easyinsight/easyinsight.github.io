package com.easyinsight.api.dynamic;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.api.APIManager;
import com.easyinsight.api.IAPIManager;

import javax.tools.*;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.sql.*;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 11:02:34 PM
 */
public class DynamicServiceDefinition {
    private long feedID;
    private long serviceID;
    private List<ConfiguredMethod> configuredMethods;

    public DynamicServiceDefinition() {
    }

    @Override
    public DynamicServiceDefinition clone() {
        try {
            DynamicServiceDefinition dynamicServiceDefinition = (DynamicServiceDefinition) super.clone();
            dynamicServiceDefinition.setConfiguredMethods(null);
            return dynamicServiceDefinition;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public DynamicServiceDefinition(long feedID, long serviceID) {
        this.feedID = feedID;
        this.serviceID = serviceID;
    }

    public long getServiceID() {
        return serviceID;
    }

    public void setServiceID(long serviceID) {
        this.serviceID = serviceID;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public List<ConfiguredMethod> getConfiguredMethods() {
        return configuredMethods;
    }

    public void setConfiguredMethods(List<ConfiguredMethod> configuredMethods) {
        this.configuredMethods = configuredMethods;
    }

    protected String getCodeSafeName(String rawString) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = rawString.toCharArray();
        for (char character : chars) {
            if (Character.isLetterOrDigit(character)) {
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }

    public void generateCode(FeedDefinition feedDefinition, Connection conn) throws SQLException, IOException {
        MethodDefinition addMethod = new MethodDefinition(feedDefinition.getDataFeedID(), "addItem", feedDefinition.getFields());
        String safeFeedName = getCodeSafeName(feedDefinition.getFeedName());
        String addArrayMethodName = "add" + Character.toUpperCase(safeFeedName.charAt(0)) + safeFeedName.substring(1) + "s";
        String beanClassName = Character.toUpperCase(safeFeedName.charAt(0)) + safeFeedName.substring(1);
        String beanSingleName = Character.toLowerCase(safeFeedName.charAt(0)) + safeFeedName.substring(1);
        String beanPluralName = beanSingleName + "s";
        MethodFactory addArrayMethod = new ArrayMethodDefinition(feedDefinition.getDataFeedID(), addArrayMethodName, feedDefinition.getFields(),
                beanClassName, beanPluralName, beanSingleName, true);
        String replaceArrayMethodName = "replace" + Character.toUpperCase(safeFeedName.charAt(0)) + safeFeedName.substring(1) + "s";
        MethodFactory updateArrayMethod = new ArrayMethodDefinition(feedDefinition.getDataFeedID(), replaceArrayMethodName, feedDefinition.getFields(),
                beanClassName, beanPluralName, beanSingleName, false);
        List<MethodFactory> methods = new ArrayList<MethodFactory>();
        methods.add(addMethod);
        methods.add(addArrayMethod);
        methods.add(updateArrayMethod);
        if (configuredMethods != null) {
            for (ConfiguredMethod configuredMethod : configuredMethods) {
                methods.add(configuredMethod.createMethodFactory(feedID, feedDefinition.getFields(), beanClassName, beanPluralName, beanSingleName));
            }
        }
        InterfaceDefinition interfaceDefinition = new InterfaceDefinition(methods, safeFeedName + "Service");
        LogClass.debug(interfaceDefinition.toCode());
        ClassDefinition classDefinition = new ClassDefinition(methods, safeFeedName + "Service");
        LogClass.debug(classDefinition.toCode());
        BeanDefinition beanDefinition = new BeanDefinition(feedDefinition.getFields(), beanClassName);
        File file = new File("srcgen");
        if (!file.exists()) {
            file.mkdir();
        }
        long userID = 1;
        File userSrcDir = new File(file.getAbsolutePath() + File.separator + String.valueOf(userID));
        LogClass.debug(userSrcDir.getAbsolutePath());
        if (!userSrcDir.exists()) {
            userSrcDir.mkdir();
        }
        long feedID = 1;
        File feedSrcDir = new File(userSrcDir.getAbsolutePath() + File.separator + String.valueOf(feedID));
        if (feedSrcDir.exists()) {
            File[] existing = feedSrcDir.listFiles();
            for (File existingFile : existing) {
                existingFile.delete();
            }
        } else {
            feedSrcDir.mkdir();
        }

        LogClass.debug(feedSrcDir.getAbsolutePath());



        File interfaceFile = new File(feedSrcDir + File.separator + "IDynamicService.java");

        FileWriter interfaceWriter = new FileWriter(interfaceFile);
        interfaceWriter.write(interfaceDefinition.toCode());
        interfaceWriter.close();

        File classFile = new File(feedSrcDir + File.separator + "DynamicService.java");

        FileWriter classWriter = new FileWriter(classFile);
        classWriter.write(classDefinition.toCode());
        classWriter.close();

        File beanFile = new File(feedSrcDir + File.separator + beanClassName + ".java");

        FileWriter beanWriter = new FileWriter(beanFile);
        beanWriter.write(beanDefinition.toCode());
        beanWriter.close();

        //CharSequenceCompiler compiler = new CharSequenceCompiler(getClass().getClassLoader(), null);
        //compiler.compile()

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        /*StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);
        ForwardingJavaFileManager forwardingManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager) {
            public ClassLoader getClassLoader(Location location) {
                if (location == StandardLocation.CLASS_PATH) {
                    return DynamicServiceDefinition.class.getClassLoader();
                }
                return super.getClassLoader(location);
            }
        };   */

        String path = new File("").getAbsolutePath();
        path = path.substring(0, path.length() - 4);
        path = path + "/webapps/DMS/WEB-INF/classes";
        String classpath = System.getProperty("java.class.path");
        String[] elements = classpath.split(File.pathSeparator);
        for (String element : elements) {
            if (element.contains("production")) {
                path = path + File.pathSeparator + element;
            }
        }

        int compilationResult = javaCompiler.run(null, null, null, "-cp", path, beanFile.getAbsolutePath(), interfaceFile.getAbsolutePath(), classFile.getAbsolutePath());
        if (compilationResult > 0) {
            throw new RuntimeException("Compilation failed.");
        }
        byte[] beanClassFile = loadClassData(feedSrcDir.getAbsolutePath(), beanClassName + ".class");
        byte[] intfClassFile = loadClassData(feedSrcDir.getAbsolutePath(), "IDynamicService.class");
        byte[] implClassFile = loadClassData(feedSrcDir.getAbsolutePath(), "DynamicService.class");

        PreparedStatement byteCodeExistsStmt = conn.prepareStatement("SELECT DYNAMIC_SERVICE_CODE_ID FROM " +
                "DYNAMIC_SERVICE_CODE WHERE FEED_ID = ?");
        byteCodeExistsStmt.setLong(1, feedDefinition.getDataFeedID());
        ResultSet rs = byteCodeExistsStmt.executeQuery();
        if (rs.next()) {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DYNAMIC_SERVICE_CODE SET INTERFACE_BYTECODE = ?," +
                    "IMPL_BYTECODE = ?, BEAN_BYTECODE = ?, BEAN_NAME = ? WHERE DYNAMIC_SERVICE_CODE_ID = ?");
            ByteArrayInputStream intfIS = new ByteArrayInputStream(intfClassFile);
            updateStmt.setBinaryStream(1, intfIS, intfClassFile.length);
            ByteArrayInputStream implIS = new ByteArrayInputStream(implClassFile);
            updateStmt.setBinaryStream(2, implIS, implClassFile.length);
            ByteArrayInputStream beanIS = new ByteArrayInputStream(beanClassFile);
            updateStmt.setBinaryStream(3, beanIS, beanClassFile.length);
            updateStmt.setString(4, beanClassName);
            updateStmt.setLong(5, rs.getLong(1));
            updateStmt.executeUpdate();
        } else {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DYNAMIC_SERVICE_CODE (FEED_ID," +
                    "INTERFACE_BYTECODE, IMPL_BYTECODE, BEAN_BYTECODE, BEAN_NAME) VALUES (?, ?, ?, ?, ?)");
            insertStmt.setLong(1, feedDefinition.getDataFeedID());
            ByteArrayInputStream intfIS = new ByteArrayInputStream(intfClassFile);
            insertStmt.setBinaryStream(2, intfIS, intfClassFile.length);
            ByteArrayInputStream implIS = new ByteArrayInputStream(implClassFile);
            insertStmt.setBinaryStream(3, implIS, implClassFile.length);
            ByteArrayInputStream beanIS = new ByteArrayInputStream(beanClassFile);
            insertStmt.setBinaryStream(4, beanIS, beanClassFile.length);
            insertStmt.setString(5, beanClassName);
            insertStmt.execute();
        }
    }

    public void deploy(Connection conn, IAPIManager apiManager) {
        try {
            Statement byteCodeStmt = conn.createStatement();
            ResultSet rs = byteCodeStmt.executeQuery("SELECT INTERFACE_BYTECODE, IMPL_BYTECODE, BEAN_BYTECODE, BEAN_NAME FROM " +
                    "DYNAMIC_SERVICE_CODE WHERE FEED_ID = " + feedID);
            if (rs.next()) {
                byte[] interfaceBytes = rs.getBytes(1);
                byte[] classBytes = rs.getBytes(2);
                byte[] beanBytes = rs.getBytes(3);
                String beanName = rs.getString(4);
                apiManager.dynamicDeployment(new DynamicDeploymentUnit(new DynamicClassLoader(interfaceBytes, classBytes, beanBytes, beanName, getClass().getClassLoader()), feedID));
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private byte[] loadClassData(String root, String filename)
            throws IOException {

        // Create a file object relative to directory provided
        File f = new File(root, filename);

        // Get size of class file
        int size = (int) f.length();

        // Reserve space to read
        byte buff[] = new byte[size];

        // Get stream to read from
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);

        // Read in data
        dis.readFully(buff);

        // close stream
        dis.close();

        // return data
        return buff;
    }
}
