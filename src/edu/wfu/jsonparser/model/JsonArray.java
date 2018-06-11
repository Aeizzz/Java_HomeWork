package edu.wfu.jsonparser.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray implements Iterable {

    private List list = new ArrayList();

    public void add(Object obj) {
        list.add(obj);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public JsonObject getJsonObject(int index) throws Exception {
        Object obj = list.get(index);
        if (!(obj instanceof JsonObject)) {
            throw new Exception("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    public JsonArray getJsonArray(int index) throws Exception {
        Object obj = list.get(index);
        if (!(obj instanceof JsonArray)) {
            throw new Exception("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return "" + list;

    }

    @Override
    public Iterator iterator() {
        return list.iterator();
    }


}
