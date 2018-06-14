##  背景介绍
在编程的过程中总是会出现把一个map转化成bean,或者把Bean转化成Map这种情况。

## map 转换成Bean

### 主要思路
遍历map 的key 从bean中找到和key 相同的属性，然后进行赋值


```java
if (source instanceof Map) {
            Map<String, Object> propMap = (Map<String, Object>) source;
            for (Map.Entry<String, Object> entry : propMap.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (isWriteable(target, name)) {
                    // 如果值是Map 的话则有嵌套
                    if (value instanceof Map) {
                        // 获取 对应的对象递归赋值
                        Object temp = getObgect(target, name);
                        copyProperties(value, temp);
                        copyProperty(target, name, temp);
                        // list 类型判断
                    } else if (value instanceof java.util.List) {
                        // 获取泛型类型
                        String tempObjectType = getObgectType(target, name);
                        // 得到类
                        Class t = Class.forName(tempObjectType);

                        List<Map<String, Object>> list = (List<Map<String, Object>>) value;
                        List<? super Object> tempList = new ArrayList<>();
                        for (Map<String, Object> objectMap : list) {
                            // 实例化对象
                            Object tempObject = t.newInstance();
                            copyProperties(objectMap, tempObject);
                            tempList.add(tempObject);
                        }
                        copyProperty(target, name, tempList);
                    } else {
                        copyProperty(target, name, value);
                    }
                }
            }
        
```


在这里有几个注意的点
 - 一个新的对象，在bean中，一个bean 是可以是别的bean 的一个属性。
 - List 类型，也就是泛型，怎么得到泛型中参数的类型，从而new 出一个对象来赋值
 
第一个问题。
我们根据反射
```java
BeanInfo info = Introspector.getBeanInfo(target.getClass());
        PropertyDescriptor[] targetDescriptors =
                info.getPropertyDescriptors();

        for (PropertyDescriptor pd : targetDescriptors) {
            if (pd.getName().equals(name)) {
                // 根据类型来new 出一个新的对象
                String type = pd.getPropertyType().getName();
                Class t = Class.forName(type);
                Object value = t.newInstance();

//                System.out.println(value.getClass().toString());
                return value;
            }
        }

```
我们利用内省从类中得到相同name 的属性，然后获取到类的具体class 例如
`edu.wfu.bean.Student`
这样就可以利用反射new 一个对象
```java
Class t = Class.forName(type);
Object value = t.newInstance();
```

第二个问题
从泛型中得到参数的类型
```java
// 获取object 某个泛型中的参数类型
    private static String getObgectType(Object target, String name) throws Exception {

        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments()[0].getTypeName();
        }
        throw new Exception("获取不到对象");

    }

```
同样也是根据反射  
参考文章  
https://blog.csdn.net/datouniao1/article/details/53788018
ParameterizedType中有一个方法:GetActualTypeArguments()这个方法的返回值是一个Type的数组


## Bean 到 Bean 的复制
这个比较简单
直接根据内省拿到bean 的get  和 set 方法进行取值 赋值，


## Bean  到 Map 的 转化

这里面有一个注意的地方。当bean 的一个属行包含另一个bean 的时候需要我们递归的去处理
```java
BeanInfo info = Introspector.getBeanInfo(source.getClass());
PropertyDescriptor[] sourceDescriptors =
        info.getPropertyDescriptors();
for (PropertyDescriptor pd : sourceDescriptors) {
    String name = pd.getName();
    if ("class".equals(name)) {
        continue;
    }
    Method method = pd.getReadMethod();
    Object value = method.invoke(source);
    String type = pd.getPropertyType().getName();
//                System.out.println(type);
    // 判断是否是基础类型,如果不是则递归去转化
    if (!(type.contains("java.lang") || type.contains("boolean") || type.contains("int")
            || type.contains("double") || type.contains("long") || type.contains("char")
            || type.contains("byte") || type.contains("short") || type.contains("float")
            || type.contains("java.util"))) {
        Map<String, Object> temp = new HashMap<>();
        copyProperties(value, temp);
        target.put(name, temp);
    } else if (type.contains("List")) {
        List tempList = (List) value;
        List<Object> list = new ArrayList<>();
        for (Object object : tempList) {
            Map<String, Object> temp = new HashMap<>();
            copyProperties(object, temp);
            list.add(temp);
        }
        target.put(name, list);


    } else {
        target.put(name, value);
    }
}

```

获取到类名的时候去比较是否是java 基本类型，还是java.util 中的类 
如果不是的话就去递归的转化

对于List类型则需要遍历去转化
