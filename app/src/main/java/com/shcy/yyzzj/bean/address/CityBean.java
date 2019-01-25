package com.shcy.yyzzj.bean.address;

import java.util.List;

/**
 * Created by licong on 2018/12/8.
 */

public class CityBean {
    private int id;
    private int level;
    private String name;
    private int pid;
    private List<AreaBean> nodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<AreaBean> getNodes() {
        return nodes;
    }

    public void setNodes(List<AreaBean> nodes) {
        this.nodes = nodes;
    }
}
