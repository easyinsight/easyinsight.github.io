package com.easyinsight.dbservice;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * User: James Boe
 * Date: Mar 23, 2009
 * Time: 11:58:52 AM
 */
public class XMLBackedStorage implements IStorage {

    private Document getXML() {
        File file = new File("eiconfig.xml");
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Document getXMLCredentials() {
        File file = new File("eicredentials.xml");
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<QueryConfiguration> getQueryConfigurations() {
        List<QueryConfiguration> queryList = new ArrayList<QueryConfiguration>();
        NodeList nodes = getXML().getChildNodes().item(0).getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("queries")) {
                NodeList queryNodes = node.getChildNodes();
                for (int j = 0; j < queryNodes.getLength(); j++) {
                    Node queryNode = queryNodes.item(j);
                    if (queryNode.getNodeName().equals("query")) {
                        String queryName = queryNode.getAttributes().getNamedItem("name").getNodeValue();
                        String dataSource = queryNode.getAttributes().getNamedItem("datasource").getNodeValue();
                        int publishMode = Integer.parseInt(queryNode.getAttributes().getNamedItem("publishmode").getNodeValue());
                        String query = queryNode.getFirstChild().getNodeValue();
                        QueryConfiguration queryConfiguration = new QueryConfiguration();
                        queryConfiguration.setQuery(query);
                        queryConfiguration.setName(queryName);
                        queryConfiguration.setPublishMode(publishMode);
                        queryConfiguration.setDataSource(dataSource);
                        queryList.add(queryConfiguration);
                    }
                }
            }
        }
        return queryList;
    }

    public EIConfiguration getEIConfiguration() {
        try {
            StringEncrypter stringEncrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);
            EIConfiguration eiConfiguration = new EIConfiguration();
            NodeList nodes = getXMLCredentials().getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeName().equals("ei")) {
                    String eiUserName = node.getChildNodes().item(1).getFirstChild().getNodeValue();
                    System.out.println("ei user name = " + eiUserName);
                    eiConfiguration.setUserName(eiUserName);
                    System.out.println("value = " + node.getChildNodes().item(3).getFirstChild().getNodeValue());
                    String eiPassword = stringEncrypter.decrypt(node.getChildNodes().item(3).getFirstChild().getNodeValue());
                    System.out.println("ei password = " + eiPassword);
                    eiConfiguration.setPassword(eiPassword);
                }
            }
            return eiConfiguration;
        } catch (StringEncrypter.EncryptionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public DBConfiguration getDBConfiguration() {
        try {
            StringEncrypter stringEncrypter = new StringEncrypter(StringEncrypter.DES_ENCRYPTION_SCHEME);
            DBConfiguration dbConfiguration = null;
            NodeList nodes = getXMLCredentials().getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeName().equals("database")) {
                    String type = node.getAttributes().getNamedItem("type").getNodeValue();
                    if (DBRemote.MYSQL.equals(type)) {
                        dbConfiguration = new MySQLConfiguration();
                    } else if (DBRemote.GENERIC.equals(type)) {
                        dbConfiguration = new GenericDBConfiguration();
                    } else {
                        throw new RuntimeException();
                    }
                    dbConfiguration.loadFromXML(node.getChildNodes().item(1), stringEncrypter);
                }
            }
            return dbConfiguration;
        } catch (StringEncrypter.EncryptionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new XMLBackedStorage().getEIConfiguration();        
        DBConfiguration dBConfiguration = new XMLBackedStorage().getDBConfiguration();
        List<QueryConfiguration> queryList = new XMLBackedStorage().getQueryConfigurations();
        System.out.println(queryList.size());
    }
}
