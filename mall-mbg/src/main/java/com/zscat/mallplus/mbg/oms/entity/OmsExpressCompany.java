package com.zscat.mallplus.mbg.oms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@TableName("oms_express_company")
public class OmsExpressCompany implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String expressCorpId;

    /**快递公司名称*/
    @TableField("EXPRESS_CORP_NAME")
    @ApiModelProperty(value = "快递公司名称")
    private String expressCorpName;

    /**0 可用 1 不可用*/
    @TableField("STATUS")
    @ApiModelProperty(value = "0 可用 1 不可用")
    private Byte status;

    /**快递公司logo*/
    @TableField("LOGO_URL")
    @ApiModelProperty(value = "快递公司logo")
    private String logoUrl;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpressCorpId() {
        return expressCorpId;
    }

    public void setExpressCorpId(String expressCorpId) {
        this.expressCorpId = expressCorpId;
    }

    public String getExpressCorpName() {
        return expressCorpName;
    }

    public void setExpressCorpName(String expressCorpName) {
        this.expressCorpName = expressCorpName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}