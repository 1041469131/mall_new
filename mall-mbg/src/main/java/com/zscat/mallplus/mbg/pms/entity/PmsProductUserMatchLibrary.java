package com.zscat.mallplus.mbg.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户搭配库
 */
@TableName("pms_product_user_match_library")
public class PmsProductUserMatchLibrary implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**搭配id*/
    @TableField("match_id")
    @ApiModelProperty(value = "搭配师的搭配id，如果有的话就传，没有的话就不传")
    private Long matchId;

    /**sku编号组合，多个用逗号隔开*/
    @TableField("sku_ids")
    @ApiModelProperty(value = "sku编号组合，多个用逗号隔开")
    private String skuIds;

    @TableField("product_ids")
    @ApiModelProperty(value = "产品组合，多个用逗号隔开")
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

    /**搭配师id*/
    @TableField("match_user_id")
    @ApiModelProperty(value = "搭配师id,不传")
    private Long matchUserId;

    /**用户id*/
    @TableField("user_id")
    @ApiModelProperty(value = "用户id，会员id必传")
    private Long userId;

    /**搭配的系列 0-组合 1-系列*/
    @TableField("match_type")
    @ApiModelProperty(value = "搭配的系列 0-组合 1-系列，不用传")
    private String matchType;

    /**收藏的状态 0-为推荐 1-已推荐*/
    @TableField("recommend_type")
    @ApiModelProperty(value = "收藏的状态 0-为推荐 1-已推荐，不传后台默认为0")
    private String recommendType;

    /**收藏的状态 0-为推荐 1-已推荐*/
    @TableField("favor_type")
    @ApiModelProperty(value = "是否喜欢 0-不喜欢 1-喜欢，不传后台默认为0")
    private String favorType;

    /**更新时间*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间,不需要传")
    private Date updateTime;

    /**创建时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间,不需要传")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(Long matchUserId) {
        this.matchUserId = matchUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
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

    public String getFavorType() {
        return favorType;
    }

    public void setFavorType(String favorType) {
        this.favorType = favorType;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(String skuIds) {
        this.skuIds = skuIds;
    }

    public String getProductIds() {
        return productIds;
    }

    public PmsProductUserMatchLibrary setProductIds(String productIds) {
        this.productIds = productIds;
        return this;
    }
}