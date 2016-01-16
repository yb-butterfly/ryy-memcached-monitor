package com.ryy.data.service.bean;

/**
 * Created by xujb on 2016/1/15.
 */
public class StatBean {
    private String key;
    private String value;
    private String desc;
    private String type;
    private int weight;

    public StatBean(String key, String value, String desc, String type, int weight) {
        this.key = key;
        this.value = value;
        this.desc = desc;
        this.type = type;
        this.weight = weight;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
