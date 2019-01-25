package com.shcy.yyzzj.retrofit.exception;

/**
 * Created by licong on 2018/9/25.
 */

public class NetException extends Exception {

    public int code;
    public String msg;
    public String toastMsg;

    public NetException(Throwable pThrowable, int pCode, String pMsg , String pToastMsg) {
        super(pThrowable);
        this.code = pCode;
        this.msg = pMsg;
        this.toastMsg = pToastMsg;
    }

    public NetException( int pCode, String pMsg) {
        this.code = pCode;
        this.msg = pMsg;
    }
}
