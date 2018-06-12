package edu.wfu.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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
                if (isWriteable(target, name)) {
                    Object value = entry.getValue();
                    copyProperty(target, name, value);

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
    private static boolean isWriteable(Object bean, String name) throws IntrospectionException {

        if (name == null) {

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
