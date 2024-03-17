package cn.hjh.ahrpc.core.util;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @ClassName : MethodUtils
 * @Description : 方法工具类
 * @Author : hujh
 * @Date: 2024-03-16 17:37
 */
public class MethodUtils {

    /**
     * 是否是Object 的本地方法
     *
     * @param methodName
     * @return
     */
    public static boolean checkLocalMethod(final String methodName) {
        if (methodName.equals("toString") ||
                methodName.equals("getClass") ||
                methodName.equals("hashCode") ||
                methodName.equals("equals") ||
                methodName.equals("clone") ||
                methodName.equals("notify") ||
                methodName.equals("notifyAll") ||
                methodName.equals("wait")
        ) {
            return true;
        } else {
            return false;
        }
    }

    public static String methodSign(Method method){
        StringBuilder sb = new StringBuilder(method.getName());
        sb.append("@").append(method.getParameterCount());
        Arrays.stream(method.getParameters()).forEach(parameter ->
                sb.append("_").append(parameter.getParameterizedType()));
        return sb.toString();
    }

}
