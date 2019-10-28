package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 喜欢/收藏表
 */
@TableName("ums_collect")
public class UmsCollect implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**会员编号*/
    @TableField("member_id")
    @ApiModelProperty(value = "会员编号")
    private Long memberId;

    /**组合的id*/
    @TableField("assembly_id")
    @ApiModelProperty(value = "组合的id")
    private Long assemblyId;

    /**组合名称*/
    @TableField("assembly_name")
    @ApiModelProperty(value = "组合名称")
    private String assemblyName;

    /**组合的title*/
    @TableField("assembly_title")
    @ApiModelProperty(value = "组合的title")
    private String assemblyTitle;

    /**组合的图片*/
    @TableField("assembly_pic")
    @ApiModelProperty(value = "组合的图片")
    private String assemblyPic;

    /**组合的价格*/
    @TableField("assembly_price")
    @ApiModelProperty(value = "组合的价格")
    private String assemblyPrice;

    /**1 商品 2 文章 3 搭配*/
    @TableField("type")
    @ApiModelProperty(value = "1 商品 2 文章 3 搭配")
    private Integer type;

    /**喜欢的类型 0-不喜欢 1-喜欢*/
    @TableField("favor_type")
    @ApiModelProperty(value = "喜欢的类型 0-不喜欢 1-喜欢")
    private String favorType;

    /**不喜欢的原因*/
    @TableField("dislike_reason")
    @ApiModelProperty(value = "不喜欢的原因")
    private String dislikeReason;

    /**更新时间*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**创建时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(Long assemblyId) {
        this.assemblyId = assemblyId;
    }

    public String getAssemblyName() {
        return assemblyName;
    }

    public void setAssemblyName(String assemblyName) {
        this.assemblyName = assemblyName;
    }

    public String getAssemblyPic() {
        return assemblyPic;
    }

    public void setAssemblyPic(String assemblyPic) {
        this.assemblyPic = assemblyPic;
    }

    public String getAssemblyPrice() {
        return assemblyPrice;
    }

    public void setAssemblyPrice(String assemblyPrice) {
        this.assemblyPrice = assemblyPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFavorType() {
        return favorType;
    }

    public void setFavorType(String favorType) {
        this.favorType = favorType;
    }

    public String getDislikeReason() {
        return dislikeReason;
    }

    public void setDislikeReason(String dislikeReason) {
        this.dislikeReason = dislikeReason;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAssemblyTitle() {
        return assemblyTitle;
    }

    public void setAssemblyTitle(String assemblyTitle) {
        this.assemblyTitle = assemblyTitle;
    }
}