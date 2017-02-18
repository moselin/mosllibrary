package com.moselin.rmlib.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Description 泛型工具类
 * @Author MoseLin
 * @Date 2016/7/8.
 */
public class TUitl
{
    /**
     * 根据泛型获取对象实例
     * @param o 超类的对象
     * @param i 此泛型参数的位置
     * @param <T> 泛型对象
     * @return 对象实例
     */
    public static <T> T getT(Object o, int i) {
        try {
            ParameterizedType type = (ParameterizedType) o.getClass().getGenericSuperclass();
            Type[] types = type.getActualTypeArguments();
            if (types.length > 0)
            {
                Class<T> tClass = (Class<T>) types[i];
                if (tClass != null)
                    return tClass.newInstance();
                return null;
            }else return null;

        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
