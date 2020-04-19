package com.zscat.mallplus.mbg.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@TableName("pms_product_commission")
public class PmsProductCommission implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**商品id*/
    @TableField("product_id")
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**推广类型 0-参与 1-不参与*/
    @TableField("promote_type")
    @ApiModelProperty(value = "推广类型 0-参与 1-不参与")
    private String promoteType;

    /**佣金类型 0-默认佣金比例 1-自定义佣金比例*/
    @TableField("commission_type")
    @ApiModelProperty(value = "佣金类型 0-默认佣金比例 1-自定义佣金比例")
    private String commissionType;

    /**佣金比例组成的方式为{‘用户的等级’:'比例数字'}*/
    @TableField("commission_proportion")
    @ApiModelProperty(value = "佣金比例组成的方式为{‘用户的等级’:'比例数字'}")
    private String commissionProportion;

    /**邀请比例组成的方式为{‘用户的等级’:'比例数字'}*/
    @TableField("invite_proportion")
    @ApiModelProperty(value = "邀请比例组成的方式为{‘用户的等级’:'比例数字'}")
    private String inviteProportion;

    /**更新日期*/
    @TableField("update_date")
    @ApiModelProperty(value = "更新日期")
    private Date updateDate;

    /**更新时间时间戳*/
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间时间戳")
    private Long updateTime;

    /**创建日期*/
    @TableField("create_date")
    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    /**创建时间戳*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间戳")
    private Long createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPromoteType() {
        return promoteType;
    }

    public void setPromoteType(String promoteType) {
        this.promoteType = promoteType;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getCommissionProportion() {
        return commissionProportion;
    }

    public void setCommissionProportion(String commissionProportion) {
        this.commissionProportion = commissionProportion;
    }

    public String getInviteProportion() {
        return inviteProportion;
    }

    public void setInviteProportion(String inviteProportion) {
        this.inviteProportion = inviteProportion;
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
        sb.append(", productId=").append(productId);
        sb.append(", promoteType=").append(promoteType);
        sb.append(", commissionType=").append(commissionType);
        sb.append(", commissionProportion=").append(commissionProportion);
        sb.append(", inviteProportion=").append(inviteProportion);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createDate=").append(createDate);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}