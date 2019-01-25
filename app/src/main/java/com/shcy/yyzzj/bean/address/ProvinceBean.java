package com.shcy.yyzzj.bean.address;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Created by licong on 2018/12/8.
 */

public class ProvinceBean implements IPickerViewData{
    private int id;
    private int level;
    private String name;
    private int pid;
    private List<CityBean> nodes;

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

    public List<CityBean> getNodes() {
        return nodes;
    }

    public void setNodes(List<CityBean> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
