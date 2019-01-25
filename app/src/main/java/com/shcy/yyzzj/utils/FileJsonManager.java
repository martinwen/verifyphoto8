package com.shcy.yyzzj.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Mzc on 2016/3/11.
 * 用于保存数据至文件
 */
public class FileJsonManager {

    public static String PROVINCE_JSON = "province_json"; //地址列表

    /**
     * 保存对象
     * @param context  上下文
     * @param filename 文件名
     * @param obj      需要保存的对象
     * **/
    public static void saveJsonObject(Context context, String filename, Object obj){
        String str = FastJsonHelper.createJsonString(obj);
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            out.write(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过文件名获取具体保存的对象
     * @param mContext 上下文
     * @param filename 文件名
     * @param cls 返回对象类型
     * **/
    public static <T> T getJsonObject(Context mContext, String filename, Class<T> cls){
        FileInputStream in = null;
        ByteArrayOutputStream bout = null;
        String mContent = "";
        byte[] buf = new byte[1024];
        bout = new ByteArrayOutputStream();
        int length = 0;
        try {
            in = mContext.openFileInput(filename); //获得输入流
            while ((length = in.read(buf)) != -1) {
                bout.write(buf, 0, length);
            }
            byte[] content = bout.toByteArray();
            mContent = new String(content, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            in.close();
            bout.close();
        } catch (Exception e) {
        }
        return FastJsonHelper.getObject(mContent,cls);
    }

    public static<T> List<T> getJsonArray(Context mContext, String filename, Class<T> cls){
        FileInputStream in = null;
        ByteArrayOutputStream bout = null;
        String mContent = "";
        byte[] buf = new byte[1024];
        bout = new ByteArrayOutputStream();
        int length = 0;
        try {
            in = mContext.openFileInput(filename); //获得输入流
            while ((length = in.read(buf)) != -1) {
                bout.write(buf, 0, length);
            }
            byte[] content = bout.toByteArray();
            mContent = new String(content, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            in.close();
            bout.close();
        } catch (Exception e) {
        }
        return FastJsonHelper.getListObject(mContent,cls);
    }
}
