package com.verify.photo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastJsonHelper
{
    /**
     * 使用JSON工具把数据转换成json对象
     * 
     * @param value
     */
    public static String createJsonString(Object value)
    {
        String str = JSON.toJSONString(value);
        return str;
    }

    /**
     * 对单个javabean进行解析
     * 数据样例：
     * {"id":1001,"mobile":"1351111111","password":"1234","username":"傻逼"}
     * 
     * @param <T>
     * @param json
     * @param cls
     * @return
     */
    public static <T> T getObject(String json, Class<T> cls)
    {
        T t = null;
        try
        {
            t = JSON.parseObject(json, cls);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 对list类型进行解析
     * 数据样例：
     * [{"id":1002,"mobile":"1361111111","password":"123654","username":"张三"},
     * {"id":1003,"mobile":"1371111111","password":"654321","username":"李四"},
     * {"id":1004,"mobile":"1381111111","password":"147369","username":"王二"}]
     * 
     * @param <T>
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> getListObject(String json, Class<T> cls)
    {
        List<T> list = new ArrayList<T>();
        try
        {
            list = JSON.parseArray(json, cls);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对MapString类型数据进行解析
     * 
     * @param json
     * @return
     */
    public static Map<String, String> getMapStr(String json)
    {
        Map<String, String> mapStr = new HashMap<String, String>();
        try
        {
            mapStr = JSON.parseObject(json,
                    new TypeReference<Map<String, String>>()
                    {
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mapStr;
    }

    /**
     * 对MapObject类型数据进行解析
     * 
     * @param json
     * @return
     */
    public static Map<String, Object> getMapObj(String json)
    {
        Map<String, Object> mapStr = new HashMap<String, Object>();
        try
        {
            mapStr = JSON.parseObject(json,
                    new TypeReference<Map<String, Object>>()
                    {
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mapStr;
    }

    /**
     * 对listmap类型进行解析
     * 
     * @param json
     * @return
     */
    public static List<Map<String, Object>> getListMap(String json)
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try
        {
            list = JSON.parseObject(json,
                    new TypeReference<List<Map<String, Object>>>()
                    {
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

}
