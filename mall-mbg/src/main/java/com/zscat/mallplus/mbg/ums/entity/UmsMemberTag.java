package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;

/**
 * <p>
 * 用户标签表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_tag")
public class UmsMemberTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("名称 必传")
    private String name;

    /**
     * 1会员标签 2 商品标签 3 文章标签
     */
    @ApiModelProperty("1会员标签 2 商品标签 3 文章标签 必传")
    private Integer type;

    /**
     * 1自动标签 2 手动标签
     */
    @ApiModelProperty("1自动标签 2 手动标签 必传")
    @TableField("gen_type")
    private Integer genType;

    @TableField("create_time")
    private Date createTime;

    /**搭配师id*/
    @TableField("match_user_id")
    private Long matchUserId;

    /**平台类型 0-平台 1-搭配平台*/
    @TableField("platform_type")
    @ApiModelProperty("平台类型 0-平台 1-搭配平台 必传")
    private String platformType;

    /**备注*/
    @TableField("remark")
    @ApiModelProperty("备注有就传 没有就不传")
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGenType() {
        return genType;
    }

    public void setGenType(Integer genType) {
        this.genType = genType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(Long matchUserId) {
        this.matchUserId = matchUserId;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
