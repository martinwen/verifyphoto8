package com.verify.photo.bean.preview;

import java.io.Serializable;

/**
 * Created by licong on 2018/11/22.
 */

public class PreviewPrintPhotoSpecBean implements Serializable {
    private String instruction;
    private String icon;
    private String name;
    private int id;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
