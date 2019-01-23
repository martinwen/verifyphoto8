package com.verify.photo.retrofit.exception;

/**
 * Created by licong on 2018/9/25.
 */

public class NetResponseException extends Exception {
    public NetResponseException(String errerStr) {
        super(errerStr);
    }
}
