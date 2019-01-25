package com.shcy.yyzzj.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.Wang on 2018/4/11.
 * 工具类 对应POST请求 封装对应要的jsonObject对象
 */

public class LoadDataPostJsonObject implements Serializable {

    private static LoadDataPostJsonObject mInstance;

    private LoadDataPostJsonObject() {

    }

    public static LoadDataPostJsonObject getInstance() {
        if (mInstance == null)
            mInstance = new LoadDataPostJsonObject();
        return mInstance;
    }

    /**
     * MAP  转 JSON
     *
     * @param map
     * @return
     */
    public JSONObject MapToJson(Map<String, Object> map) {
        return new JSONObject(map);
    }

    /**
     * 多个key 转成JsonObject String
     *
     * @param lis,
     * @param key
     * @return
     */
    public JSONObject GetStringJsonObj(List<String> lis, String... key) {
        if (lis == null || lis.size() < 1 || key == null || lis.size() != key.length )
            return null;
        Map map = new HashMap();
        for (int i = 0; i < lis.size(); i++) {
            map.put(lis.get(i), key[i]);
        }
        return MapToJson(map);
    }

    /**
     * 多个key 对应多个 object 生成JsonObject
     *
     * @param key
     * @return
     */
    public List<String> GetStringToList(String... key) {
        List lit = new ArrayList();
        for (String s : key) {
            lit.add(s);
        }
        return lit;
    }
}
