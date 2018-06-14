package edu.wfu.utils;

import java.util.*;

public class JsonUtils {


    public static String toJSONString(Object object) throws Exception {

        if (object == null) {
            return "{}";
        }

        if (object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (map.isEmpty()) {
                return "{}";
            }
            StringBuilder jsonStr = new StringBuilder();
            jsonStr.append("{");
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Object value = map.get(key);
                StringBuilder tempStr = new StringBuilder();
                tempStr.append("\"" + key + "\":");
                // 如果是 List 遍历每一个类型 增加 [],
                if (value instanceof List) {
                    tempStr.append("[");
                    List list = (List) value;
                    for (Object tmp : list) {
                        if (tmp instanceof Map) {
                            tempStr.append(toJSONString(tmp));
                            tempStr.append(",");
                        } else {
                            tempStr.append("\"" + tmp + "\",");
                        }

                    }
                    tempStr.delete(tempStr.length() - 1, tempStr.length());
                    tempStr.append("],");

                    // 如果是Map  递归 返回一个对象 {},
                } else if (value instanceof Map) {
                    tempStr.append(toJSONString(value));
                    tempStr.append(",");
                    // 基础属性 直接增加，，还要判断类型
                } else {
                    Object tempValue = map.get(key);
                    if (tempValue instanceof String) {
                        tempStr.append("\"" + map.get(key) + "\",");
                    } else {
                        tempStr.append(map.get(key) + ",");
                    }


                }

                jsonStr.append(tempStr.toString());
            }
            String str = jsonStr.substring(0, jsonStr.length() - 1);
            str += "}";
            return str;
        } else {
            Map<String, Object> temp = new HashMap<>();
            BeanUtils.copyProperties(object, temp);
            return toJSONString(temp);

        }

    }
}
