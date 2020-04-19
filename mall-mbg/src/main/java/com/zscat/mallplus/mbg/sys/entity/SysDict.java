package com.zscat.mallplus.mbg.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@TableName("sys_dict")
public class SysDict implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**表示属于那些模块*/
    @TableField("module")
    @ApiModelProperty(value = "表示属于那些模块")
    private String module;

    /**表示属于什么类型*/
    @TableField("type")
    @ApiModelProperty(value = "表示属于什么类型")
    private String type;

    /**键值*/
    @TableField("key")
    @ApiModelProperty(value = "键值")
    private String key;

    /**对应的名称*/
    @TableField("value")
    @ApiModelProperty(value = "对应的名称")
    private String value;

    /**排序*/
    @TableField("sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    private Date updateDate;

    private Long updateTime;

    private Date createDate;

    private Long createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", module=").append(module);
        sb.append(", type=").append(type);
        sb.append(", key=").append(key);
        sb.append(", value=").append(value);
        sb.append(", sort=").append(sort);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createDate=").append(createDate);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}