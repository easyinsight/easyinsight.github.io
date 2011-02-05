package com.easyinsight.datafeeds.cleardb;

import com.cleardb.app.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/30/10
 * Time: 10:36 AM
 */
public class ClearDBTest {

    public static final String APP_ID = "f05f4690c2214e5083c6fb5374c71075";
    public static final String API_KEY = "ca3d30a4f40cb44dbc57eaadc97c18add65ef47c";

    public static void main(String[] args) throws Exception {
        Client client = new Client(API_KEY, APP_ID);
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('Acme', 500, 1000)");
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('XYZ', 800, 150)");
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('ABC', 200, 700)");
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('DEF', 300, 400)");
    }
}
