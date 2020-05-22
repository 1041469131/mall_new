package com.zscat.mallplus.mbg.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@TableName("sys_matcher_account")
@ApiModel("")
public class SysMatcherAccount implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "如果是新增的话不需要传，如果不是新增的话，必填")
    private Long id;

    /**搭配师id*/
    @TableField("matcher_id")
    @ApiModelProperty(value = "搭配师id（不需要传）")
    private Long matcherId;

    /**会员id*/
    @TableField("member_id")
    @ApiModelProperty(value = "会员id（不需要传）")
    private Long memberId;

    /**账户实名*/
    @TableField("account_real_name")
    @ApiModelProperty(value = "账户实名(必传)")
    private String accountRealName;

    /**银行卡号*/
    @TableField("bank_code")
    @ApiModelProperty(value = "银行卡号（如果类型是银行卡，必填）")
    private String bankCode;

    /**绑定手机号*/
    @TableField("bind_phone")
    @ApiModelProperty(value = "绑定手机号（如果类型不是银行卡，必填）")
    private String bindPhone;

    /**开户行*/
    @TableField("bank")
    @ApiModelProperty(value = "开户行（类型是银行卡，必填）")
    private String bank;

    /**二维码*/
    @TableField("qr_code")
    @ApiModelProperty(value = "二维码（类型不是银行卡，必填）")
    private String qrCode;

    /**账户类型 0-银行卡 1-微信 2-支付宝*/
    @TableField("account_type")
    @ApiModelProperty(value = "账户类型 0-银行卡 1-微信 2-支付宝（必填）")
    private String accountType;

    /**更新时间*/
    @TableField("update_date")
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    /**更新时间戳*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间戳")
    private Long updateTime;

    /**创建时间*/
    @TableField("create_date")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    /**创建时间戳*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间戳")
    private Long createTime;

    /**账户的状态 0-未启动 1-已启用*/
    @TableField("status")
    @ApiModelProperty(value = "账户的状态 0-未启动 1-已启用")
    private String status;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatcherId() {
        return matcherId;
    }

    public void setMatcherId(Long matcherId) {
        this.matcherId = matcherId;
    }

    public String getAccountRealName() {
        return accountRealName;
    }

    public void setAccountRealName(String accountRealName) {
        this.accountRealName = accountRealName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}