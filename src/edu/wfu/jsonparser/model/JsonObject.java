package edu.wfu.jsonparser.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 输出类
 */
public class JsonObject {
    private Map<String, Object> map = new HashMap<String, Object>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public List<Map.Entry<String, Object>> getAllKeyValue() {
        return new ArrayList<>(map.entrySet());
    }

    public JsonObject getJsonObject(String key) throws Exception {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }
        Object object = map.get(key);
        if (!(object instanceof JsonObject)) {
            throw new Exception("Type of value is not JsonObject");
        }
        return (JsonObject) object;
    }

    public JsonArray getJsonArray(String key) throws Exception {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JsonArray)) {
            throw new Exception("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "map=" + map +
                '}';
    }
}
