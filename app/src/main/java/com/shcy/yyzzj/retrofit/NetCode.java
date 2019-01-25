package com.shcy.yyzzj.retrofit;

import android.net.ParseException;

import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.retrofit.exception.NetNoDataCodeException;
import com.shcy.yyzzj.retrofit.exception.NetNoDataTypeException;
import com.shcy.yyzzj.retrofit.exception.NetResponseBodyException;
import com.shcy.yyzzj.retrofit.exception.NetResponseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by licong on 2018/9/25.
 */

public final class NetCode {
    /**
     * 响应状态码
     */
    public static final int SUCCESS_RESPONSE = 200;// 响应正常

    /**
     * 请求方式
     */
    public static final String GET = "GET";
    public static final String POST = "POST";

    /**
     * 数据状态码
     */
    public static final int SUCCESS_DATA = 0; // 数据正常
    /**
     * 异常状态码 -> 系统异常
     */
    public static final int ERROR_UNAUTHORIZED = 4001; //未授权的请求
    public static final int ERROR_FORBIDDEN = 4002; //禁止访问
    public static final int ERROR_NOT_FOUND = 4003; //服务器地址未找到
    public static final int ERROR_REQUEST_TIMEOUT = 4004; //请求超时
    public static final int ERROR_INTERNAL_SERVER_ERROR = 4020; //服务器出错
    public static final int ERROR_BAD_GATEWAY = 502; //无效的请求
    public static final int ERROR_SERVICE_UNAVAILABLE = 503; //服务器不可用
    public static final int ERROR_GATEWAY_TIMEOUT = 504; //网关响应超时
    public static final int ERROR_ACCESS_DENIED = 302; //网络错误
    public static final int ERROR_HANDEL_ERRROR = 417; //接口处理失败


    /**
     * 异常状态码 -> 自定义异常
     */
    public static final int ERROR = -10; // 统一异常
    public static final int ERROR_Http = -100; // 网络异常
    public static final int ERROR_SocketTimeout = -101; // socket连接超时异常
    public static final int ERROR_Connect = -102; // 连接异常
    public static final int ERROR_Parse = -103; // 解析异常
    public static final int ERROR_SSL = -104; // 证书验证失败
    public static final int ERROR_RESPONSE = -1000; // 响应异常
    public static final int ERROR_NO_DATA_CODE = -1001; // 数据无匹配code异常
    public static final int ERROR_NO_DATA_TYPE = -1002; // 数据无匹配type异常
    public static final int ERROR_RESPONSE_BODY = -1003; // 数据解析空异常


    /**
     * 分解异常情况
     */
    public static NetException parseException(Throwable pE) {
        int code = NetCode.ERROR;
        String msg = "未知错误";
        String toastMsg = "网络跟着你的bug私奔了";
        /**
         * 系统异常
         */
        if (pE instanceof HttpException) {
            HttpException httpException = (HttpException) pE;
            switch (httpException.code()) {
                case ERROR_UNAUTHORIZED:
                    code = ERROR_UNAUTHORIZED;
                    msg = "未授权的请求";
                    break;
                case ERROR_FORBIDDEN:
                    code = ERROR_FORBIDDEN;
                    msg = "禁止访问";
                    break;
                case ERROR_NOT_FOUND:
                    code = ERROR_NOT_FOUND;
                    msg = "操作无法完成";
                    break;
                case ERROR_REQUEST_TIMEOUT:
                    code = ERROR_REQUEST_TIMEOUT;
                    msg = "请求超时";
                    break;
                case ERROR_INTERNAL_SERVER_ERROR:
                    code = ERROR_INTERNAL_SERVER_ERROR;
                    msg = "服务器出错";
                    break;
                case ERROR_BAD_GATEWAY:
                    code = ERROR_BAD_GATEWAY;
                    msg = "无效的请求";
                    break;
                case ERROR_SERVICE_UNAVAILABLE:
                    code = ERROR_SERVICE_UNAVAILABLE;
                    msg = "服务器不可用";
                    break;
                case ERROR_GATEWAY_TIMEOUT:
                    code = ERROR_GATEWAY_TIMEOUT;
                    msg = "操作无法完成";
                    break;
                case ERROR_ACCESS_DENIED:
                    code = ERROR_ACCESS_DENIED;
                    msg = "网络错误";
                    break;
                case ERROR_HANDEL_ERRROR:
                    code = ERROR_HANDEL_ERRROR;
                    msg = "接口处理失败";
                    break;
                default:
                    code = NetCode.ERROR_Http;
                    msg = "网络错误";
                    break;
            }
            toastMsg = "网络跟着你的bug私奔了";
            /**
             * 自定义异常
             */
        } else if (pE instanceof SocketTimeoutException || pE instanceof ConnectTimeoutException) {
            code = NetCode.ERROR_SocketTimeout;
            msg = "连接超时";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof ConnectException) {
            code = NetCode.ERROR_Connect;
            msg = "连接失败";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof JSONException || pE instanceof ParseException) {
            code = NetCode.ERROR_Parse;
            msg = "解析错误";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof SSLHandshakeException) {
            code = NetCode.ERROR_SSL;
            msg = "证书验证失败";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof NetResponseException) {
            code = NetCode.ERROR_RESPONSE;
            msg = "响应异常";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof NetNoDataCodeException) {
            code = NetCode.ERROR_NO_DATA_CODE;
            msg = "数据无匹配code异常";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof NetNoDataTypeException) {
            code = NetCode.ERROR_NO_DATA_TYPE;
            msg = "数据无匹配type异常";
            toastMsg = "网络跟着你的bug私奔了";
        } else if (pE instanceof NetResponseBodyException) {
            code = NetCode.ERROR_RESPONSE_BODY;
            msg = "数据解析空异常";
            toastMsg = "网络跟着你的bug私奔了";
        }
        toastMsg = "网络跟着你的bug私奔了";
        msg = "网络跟着你的bug私奔了";
        return new NetException(pE, code, msg, toastMsg);
    }

}
