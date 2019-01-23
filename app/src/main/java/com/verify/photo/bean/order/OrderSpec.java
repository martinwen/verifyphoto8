package com.verify.photo.bean.order;

import java.io.Serializable;

/**
 * Created by licong on 2018/10/10.
 */

public class OrderSpec implements Serializable{
    int id;
    String icon;
    String name;
    String instruction;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
