package com.shcy.yyzzj.bean;

import java.io.Serializable;

/**
 * Created by licong on 2018/9/25.
 */

public class Base_Bean extends Strong_BaseBean implements Serializable{
    int nextCursor;
    int pageNo;
    int pageSize;
    int pageTotal;
    int previousCursor;
    int total;
    int totalNum;

    private int itemViewType;

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public int getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(int nextCursor) {
        this.nextCursor = nextCursor;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(int previousCursor) {
        this.previousCursor = previousCursor;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    int request_id = 0;
    int code = CODE_TIME_OUT;
    String message = "";
    String result = "";



    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSucess() {
        return code == 0;
    }

    public boolean isTimeOut() {
        return code == CODE_TIME_OUT;
    }

    @Override
    public String toString() {
        return "Base_Bean [request_id=" + request_id + ", code=" + code
                + ", message=" + message + "]";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}