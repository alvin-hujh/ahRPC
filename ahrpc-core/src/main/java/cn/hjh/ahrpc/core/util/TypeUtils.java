package cn.hjh.ahrpc.core.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName : TypeUtils
 * @Description :
 * @Author : hujh
 * @Date: 2024-03-17 19:48
 */
public class TypeUtils {
    public static Object cast(Object origin, Class<?> type) {
        if (origin == null) return null;
        Class<?> aClass = origin.getClass();
        //如果原始类型是目标类型的子类，直接返回原始对象
        if (type.isAssignableFrom(aClass)) {
            return origin;
        }

        if(type.isArray()) {
            if(origin instanceof List list) {
                origin = list.toArray();
            }
            int length = Array.getLength(origin);
            Class<?> componentType = type.getComponentType();
            Object resultArray = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                if (componentType.isPrimitive() || componentType.getPackageName().startsWith("java")) {
                    Array.set(resultArray, i, Array.get(origin, i));
                } else {
                    Object castObject = cast(Array.get(origin, i), componentType);
                    Array.set(resultArray, i, castObject);
                }
            }
            return resultArray;
        }

        if (origin instanceof HashMap map){
            JSONObject jsonObject = new JSONObject(map);
            return jsonObject.toJavaObject(type);
        }
        if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            return Long.valueOf(origin.toString());
        } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            return Integer.valueOf(origin.toString());
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            return Double.valueOf(origin.toString());
        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            return Float.valueOf(origin.toString());
        } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
            return Byte.valueOf(origin.toString());
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            return Boolean.valueOf(origin.toString());
        } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
            return Short.valueOf(origin.toString());
        } else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
            return Character.valueOf(origin.toString().charAt(0));
        }
        return null;


    }
}
