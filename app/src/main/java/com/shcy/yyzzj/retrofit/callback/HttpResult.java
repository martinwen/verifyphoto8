package com.shcy.yyzzj.retrofit.callback;


import com.shcy.yyzzj.bean.Strong_BaseBean;

/**
 * Created by licong on 2018/9/25.
 */

public class HttpResult<T> extends Strong_BaseBean {
    int request_id = 0;
    String message = "";

    int status;
    T data;
    long serverTime;
    Error error;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean isSucess(){
        return  status == 0;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
