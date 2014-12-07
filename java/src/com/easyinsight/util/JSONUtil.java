package com.easyinsight.util;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.JSONException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/1/14
 * Time: 7:05 AM
 */
public class JSONUtil {
    public static JSONArray fromList(List l) {
        JSONArray array = new JSONArray();
        for (Object o : l) {
            array.add(o);
        }
        return array;
    }

    public static JSONObject fromOrg(org.json.JSONObject jo) {
        try {
            JSONObject object = new JSONObject();
            Iterator iter = jo.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Object value = jo.get(key);
                object.put(key, value);
            }
            return object;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject fromMap(Map m) {
        JSONObject object = new JSONObject();
        for (Object entryObject : m.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObject;
            object.put((String) entry.getKey(), entry.getValue());
        }
        return object;
    }
}
