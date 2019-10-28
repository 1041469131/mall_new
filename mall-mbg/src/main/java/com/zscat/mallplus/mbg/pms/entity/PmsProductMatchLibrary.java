package com.zscat.mallplus.mbg.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 搭配库
 */
@TableName("pms_product_match_library")
public class PmsProductMatchLibrary implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**商品组合，多个商品id用逗号隔开*/
    @TableField("product_ids")
    @ApiModelProperty(value = "商品组合，多个商品id用逗号隔开")
    private String productIds;

    /**标题*/
    @TableField("title")
    @ApiModelProperty(value = "标题")
    private String title;

    /**标题描述*/
    @TableField("title_descr")
    @ApiModelProperty(value = "标题描述")
    private String titleDescr;

    /**标题的图片地址*/
    @TableField("title_pic_url")
    @ApiModelProperty(value = "标题的图片地址")
    private String titlePicUrl;

    /**操作人id*/
    @TableField("operate_id")
    @ApiModelProperty(value = "操作人id")
    private Long operateId;

    /**搭配库的归属类型 0-个人 1-公司*/
    @TableField("match_ower")
    @ApiModelProperty(value = "搭配库的归属类型 0-个人 1-公司")
    private String matchOwer;

    /**搭配的系列 0-组合 1-系列*/
    @TableField("match_type")
    @ApiModelProperty(value = "搭配的系列 0-组合 1-系列")
    private String matchType;

    /**收藏的状态 0-为收藏 1-已收藏*/
    @TableField("collect_status")
    @ApiModelProperty(value = "收藏的状态 0-为收藏 1-已收藏")
    private String collectStatus;

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

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleDescr() {
        return titleDescr;
    }

    public void setTitleDescr(String titleDescr) {
        this.titleDescr = titleDescr;
    }

    public String getTitlePicUrl() {
        return titlePicUrl;
    }

    public void setTitlePicUrl(String titlePicUrl) {
        this.titlePicUrl = titlePicUrl;
    }

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public String getMatchOwer() {
        return matchOwer;
    }

    public void setMatchOwer(String matchOwer) {
        this.matchOwer = matchOwer;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(String collectStatus) {
        this.collectStatus = collectStatus;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productIds=").append(productIds);
        sb.append(", title=").append(title);
        sb.append(", titleDescr=").append(titleDescr);
        sb.append(", titlePicUrl=").append(titlePicUrl);
        sb.append(", operateId=").append(operateId);
        sb.append(", matchOwer=").append(matchOwer);
        sb.append(", matchType=").append(matchType);
        sb.append(", collectStatus=").append(collectStatus);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}