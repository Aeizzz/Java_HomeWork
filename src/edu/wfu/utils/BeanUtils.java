package edu.wfu.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtils {


    /**
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        if (source == null) {
            throw new Exception("源对象为空");
        }

        if (target == null) {
            throw new Exception("目标对象为空");
        }

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
        } else {
            BeanInfo info = Introspector.getBeanInfo(source.getClass());
            PropertyDescriptor[] sourceDescriptors =
                    info.getPropertyDescriptors();

            for (PropertyDescriptor pd : sourceDescriptors) {
                String name = pd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (isWriteable(target, name)) {
                    Method method = pd.getReadMethod();
                    Object value = method.invoke(source);

                    copyProperty(target, name, value);
                }

            }

        }


    }


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

    private static Object getObgect(Object target, String name) throws Exception {
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

        throw new Exception("没有键" + name);

    }


    public static void copyProperties(Object source, Map<String, Object> target) throws Exception {
        if (source == null) {
            throw new Exception("源对象为空");
        }

        if (target == null) {
            throw new Exception("目标对象为空");
        }
        if (source instanceof Map) {
            Map<String, Object> propMap = (Map<String, Object>) source;
            for (Map.Entry entry : propMap.entrySet()) {
                target.put((String) entry.getKey(), entry.getValue());
            }

        } else {
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
        }

    }


    /**
     * 复制属性到目标对象
     *
     * @param target 目标对象
     * @param name   键
     * @param value  值
     */
    private static void copyProperty(Object target, String name, Object value) throws Exception {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(target.getClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for (PropertyDescriptor ps : pds) {
                if (ps.getName().equals(name)) {
                    Method method = ps.getWriteMethod();
                    if (value instanceof Long) {
                        // 暂时 未处理
                        // TODO 处理整数类型
                        method.invoke(target, value);
                    } else {
                        method.invoke(target, value);
                    }
                    return;
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        throw new Exception("未获取到set方法");

    }


    /**
     * 判断是否存在该属性
     *
     * @param bean
     * @param name
     * @return
     * @throws IntrospectionException
     */
    private static boolean isWriteable(Object bean, String name) throws Exception {

        if (name == null) {
            throw new Exception("键不能为null");
        }

        BeanInfo info = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        for (PropertyDescriptor ps : pds) {
            if (ps.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}
