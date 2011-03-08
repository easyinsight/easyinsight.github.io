package com.easyinsight.datafeeds.cleardb;

import com.cleardb.app.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/30/10
 * Time: 10:36 AM
 */
public class ClearDBTest {

    private static final String[] customerNames = { "Bross Design", "Crestone Engineering", "Uncompahgre Systems",
            "Sneffels Technology", "Sunlight Architecture", "Pyramid Research", "Little Bear Consulting", "Torreys Inc" };

    private static final String[] postalCodes = { "80424", "81252", "81235", "81432", "81122", "81624", "81101", "80435"};
    private static final String[] industries = { "Design", "Engineering", "Consulting" };

    private static final String[] productNames = { "Widget1", "Widget2", "Widget3", "Widget4", "Widget5"};

    private static final int[] costs = { 50, 80, 120, 140, 180 };

    public static final String APP_ID = "f05f4690c2214e5083c6fb5374c71075";
    public static final String API_KEY = "ca3d30a4f40cb44dbc57eaadc97c18add65ef47c";

    public static void main2(String[] args) throws Exception {
        Client client = new Client(API_KEY, APP_ID);
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('Acme', 500, 1000)");
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('XYZ', 800, 150)");
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('ABC', 200, 700)");
        client.query("INSERT INTO Blah (Customer, Amount, Revenue) values ('DEF', 300, 400)");
    }

    public static void main3(String[] args) throws Exception {
        Client client = new Client(API_KEY, APP_ID);
        client.query("DELETE FROM Customer");
        client.query("INSERT INTO Customer (CustomerName, CustomerRegion, CustomerID) values ('Acme', 'Northwest', 1)");
        client.query("INSERT INTO Customer (CustomerName, CustomerRegion, CustomerID) values ('XYZ', 'Northwest', 2)");
        client.query("INSERT INTO Customer (CustomerName, CustomerRegion, CustomerID) values ('ABC', 'Southwest', 3)");
        client.query("INSERT INTO Customer (CustomerName, CustomerRegion, CustomerID) values ('DEF', 'Southwest', 4)");
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(API_KEY, APP_ID);
        client.query("DELETE FROM Customer");
        client.query("DELETE FROM ProductCost");
        client.query("DELETE FROM Sales");
        for (int i = 0; i < 8; i++) {
            String customerName = customerNames[i];
            String industry = industries[i % industries.length];
            String postalCode = postalCodes[i];
            System.out.println(customerName + " - " + industry + " - " + postalCode);
            customers.add(customerName);
            client.query("INSERT INTO Customer (CustomerName, Industry, PostalCode) values ('" +customerName+"', '"+industry+"','"+postalCode+"')");
        }
        for (int i = 0; i < 5; i++) {
            String productName = productNames[i];
            int productCost = costs[i];
            System.out.println(productName + " - " + productCost);
            products.add(productName);
            client.query("INSERT INTO ProductCost (ProductName, ProductCost) values ('" +productName+"', '"+productCost+ "')");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (int i = 0; i < 100; i++) {
            String customer = customers.get((int) (Math.random() * 8));
            String product = products.get((int) (Math.random() * 5));
            int quantity = (int) (Math.random() * 3);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            cal.set(Calendar.DAY_OF_YEAR, (int) (Math.random() * 365));
            String date = dateFormat.format(cal.getTime());
            System.out.println(customer + " - " + product + " - " + quantity + " - " + date);
            client.query("INSERT INTO Sales (Customer, Product, Quantity, OrderDate) values ('" +customer+"', '"+product+"','"+quantity+"','"+date+"')");
        }
    }

    private static List<String> customers = new ArrayList<String>();
    private static List<String> products = new ArrayList<String>();
}
