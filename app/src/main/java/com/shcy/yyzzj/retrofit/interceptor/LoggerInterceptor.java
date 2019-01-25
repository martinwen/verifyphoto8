package com.shcy.yyzzj.retrofit.interceptor;


import com.shcy.yyzzj.log.L;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by licong on 2018/9/25.
 */

public final class LoggerInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 日志String
        StringBuilder sb = new StringBuilder();
        boolean isOpen =  L.configAllowLog;
        // 请求request
        Request request = chain.request();
        if (!isOpen) {
            // 日志关闭,直接返回
            return chain.proceed(request);
        }
        sb.append("请求方式: ").append(request.method()).append("\n");
        Connection requestConnection = chain.connection();
        Protocol requestProtocol = requestConnection == null ? Protocol.HTTP_1_1 : requestConnection.protocol();
        sb.append("请求协议: ").append(requestProtocol).append("\n");
        sb.append("请求地址: ").append(request.url()).append("\n");
        // 显示详情
        if (isOpen) {
            // 请求头
            Headers requestHeaders = request.headers();
            int requestHeadersCount = requestHeaders.size();
            if (requestHeaders != null && requestHeadersCount > 0) {
                sb.append("RequestHeaders:").append("\n");
                for (int i = 0; i < requestHeadersCount; i++) {
                    if (!requestHeaders.name(i).equals("Content-Type") && !requestHeaders.name(i).equals("Content-Length")) {
                        sb.append("\t").append(requestHeaders.name(i)).append(": ").append(requestHeaders.value(i)).append("\n");
                    }
                }
            }
            // 请求体
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                sb.append("RequestBody:").append("\n");
                MediaType contentType = requestBody.contentType();
                long contentLength = requestBody.contentLength();
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = null;
                if (contentType != null) {
                    sb.append("\tContent-Type: ").append(contentType).append("\n");
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                if (contentLength != -1) {
                    sb.append("\tContent-Length: ").append(contentLength).append(" byte").append("\n");
                } else {
                    sb.append("\tContent-Length: ").append("unknown-length").append("\n");
                }
                if (charset != null) {
                    if (isPlaintext(buffer)) {
                        sb.append("\tParameters: ").append(buffer.readString(charset)).append("\n");
                    } else {
                        sb.append("\tParameters: ").append("非字符型数据,无法输出为Log形式! isPlaintext(buffer) = false").append("\n");
                    }
                }
            }
        }
        /**
         * 响应response
         */
        long startTime = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception pE) {
            sb.append("-----------------Http Failed!-------------------");
            L.e(sb.toString());
            // 显示详情
            if (isOpen) {
                L.e(pE);
            }
            throw pE;
        }
        long endTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        sb.append("响应状态码: ").append(response.code()).append("\n");
        sb.append("响应消息: ").append(response.message()).append("\n");
        sb.append("响应地址: ").append(response.request().url()).append("\n");
        sb.append("响应时间: ").append(endTime).append(" ms\n");
        // 显示详情
        if (isOpen) {
            // 响应头
            Headers responseHeaders = response.headers();
            int responseHeadersCount = responseHeaders.size();
            if (responseHeaders != null && responseHeadersCount > 0) {
                sb.append("ResponseHeaders:").append("\n");
                for (int i = 0; i < responseHeadersCount; i++) {
                    if (!responseHeaders.name(i).equals("Content-Type") && !responseHeaders.name(i).equals("Content-Length")) {
                        sb.append("\t").append(responseHeaders.name(i)).append(": ").append(responseHeaders.value(i)).append("\n");
                    }
                }
            }
        }
        // 响应体
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            sb.append("ResponseBody:").append("\n");
            MediaType contentType = responseBody.contentType();
            long contentLength = responseBody.contentLength();
            if (contentType != null) {
                sb.append("\tContent-Type: ").append(contentType).append("\n");
            }
            if (contentLength != -1) {
                sb.append("\tContent-Length: ").append(contentLength).append(" byte").append("\n");
            }
            if (HttpHeaders.hasBody(response)) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                Charset charset = null;
                if (contentType != null) {
                    try {
                        charset = contentType.charset(Charset.forName("UTF-8"));
                    } catch (Exception pE) {
                        sb.append("无法解析的响应体,字符集可能是异常的!");
                        L.e(sb.toString());
                        // 显示详情
                        if (isOpen) {
                            L.e(pE);
                        }
                        return response;
                    }
                }
                sb.append("\t").append("Size: ").append(buffer.size()).append(" byte").append("\n");
                if (charset != null && contentLength != 0) {
                    if (isPlaintext(buffer)) {
                        sb.append("\tParameters: ").append(buffer.clone().readString(charset)).append("\n");
                    } else {
                        sb.append("\tParameters: ").append("非字符型数据,无法输出为Log形式! isPlaintext(buffer) = false").append("\n");
                    }
                }
            }
        }
        String inStr = sb.toString();
        if (inStr.endsWith("\n")) {
            inStr = inStr.substring(0, inStr.length() - 1);
        }
        L.e(inStr);
        return response;
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

}
