package com.shcy.yyzzj.bean;

import java.util.Set;

/**
 * Created by licong on 2018/7/18.
 */

public class AliasTagsBean {
    private String alias;
    private Set<String> tags;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
