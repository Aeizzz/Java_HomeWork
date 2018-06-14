## JsonUtils
这一个是用于Map 转化成Json
 相比解析Json  这一个就简单了很多，
 
 对于单纯的Map  只包含基本的类型，不包含自定义的Bean 那样就很简单了
 ```java
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
}

```

最简单的类型则使用字符串拼接
对于是Map 则 递归去处理
对于List类型则遍历 去 判断，同样存在Map 则需要 递归

