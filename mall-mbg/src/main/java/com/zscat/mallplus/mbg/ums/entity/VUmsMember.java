package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("v_ums_member")
@ApiModel("粉丝和平台管理")
public class VUmsMember implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**搭配师id*/
    @TableField("match_user_id")
    @ApiModelProperty(value = "搭配师id")
    private Long matchUserId;

    private String fanName;

    /**注册时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "注册时间")
    private Date createTime;

    private String matchUserName;

    private String inviteName;

    private BigDecimal totalAmount;

    private BigDecimal avaAmount;

    /**参数名称*/
    @TableField("dress_freq_name")
    @ApiModelProperty(value = "需求频次")
    private String dressFreqName;

    /**属性名对应的code*/
    @TableField("dress_freq_code")
    @ApiModelProperty(value = "属性名对应的code")
    private String dressFreqCode;

    /**手机号码*/
    @TableField("phone")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "订单数")
    private BigDecimal orderCount;

    private String tagname;

    @ApiModelProperty(value = "推荐的日期(时间戳)")
    private Long recomendTime;

    @ApiModelProperty(value = "推荐的日期")
    private Date recomendDate;

    @ApiModelProperty(value = "头像地址")
    private String icon;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(Long matchUserId) {
        this.matchUserId = matchUserId;
    }

    public String getFanName() {
        return fanName;
    }

    public void setFanName(String fanName) {
        this.fanName = fanName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMatchUserName() {
        return matchUserName;
    }

    public void setMatchUserName(String matchUserName) {
        this.matchUserName = matchUserName;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAvaAmount() {
        return avaAmount;
    }

    public void setAvaAmount(BigDecimal avaAmount) {
        this.avaAmount = avaAmount;
    }

    public String getDressFreqName() {
        return dressFreqName;
    }

    public void setDressFreqName(String dressFreqName) {
        this.dressFreqName = dressFreqName;
    }

    public String getDressFreqCode() {
        return dressFreqCode;
    }

    public void setDressFreqCode(String dressFreqCode) {
        this.dressFreqCode = dressFreqCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(BigDecimal orderCount) {
        this.orderCount = orderCount;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getRecomendTime() {
        return recomendTime;
    }

    public void setRecomendTime(Long recomendTime) {
        this.recomendTime = recomendTime;
    }

    public Date getRecomendDate() {
        return recomendDate;
    }

    public void setRecomendDate(Date recomendDate) {
        this.recomendDate = recomendDate;
    }
}