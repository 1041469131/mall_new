package com.zscat.mallplus.manage.utils.applet;
/**
 * 模板详细信息
 * 根据需求自己更改
 */
public class TemplateData {
    private String value;
    private String color;
    public TemplateData(){}
    public TemplateData(String value, String color){
        this.value = value;
        this.color = color;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * @return the Color
     */
    public String getColor() {
        return color;
    }
    /**
     * @param color the Color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

}
