package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName("ums_apply_matcher")
@ApiModel("会员申请搭配师")
@Data
public class UmsApplyMatcher implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**会员id*/
    @TableField("member_id")
    @ApiModelProperty(value = "会员id,不用传")
    private Long memberId;

    /**用户姓名*/
    @TableField("user_name")
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    /**微信号*/
    @TableField("wechat_no")
    @ApiModelProperty(value = "微信号")
    private String wechatNo;

    /**手机号*/
    @TableField("phone")
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**微信二维码*/
    @TableField("wechat_two_code")
    @ApiModelProperty(value = "微信二维码")
    private String wechatTwoCode;

    /**邀请人手机号*/
    @TableField("invite_phone")
    @ApiModelProperty(value = "邀请人手机号")
    private String invitePhone;

    /**介绍*/
    @TableField("introduce")
    @ApiModelProperty(value = "介绍")
    private String introduce;

    /**审核状态 0-未审核 1-审核通过 2-审核拒绝*/
    @TableField("audit_status")
    @ApiModelProperty(value = "审核状态 0-未审核 1-审核通过 2-审核拒绝")
    private String auditStatus;

    /**审核原因*/
    @TableField("audit_reson")
    @ApiModelProperty(value = "审核备注，后管，小程序可以不用传")
    private String auditReson;

    @TableField("update_date")
    @ApiModelProperty(value = "更新时间，不传")
    private Date updateDate;

    @TableField("update_time")
    @ApiModelProperty(value = "更新时间时间戳，不传")
    private Long updateTime;

    /**创建时间*/
    @TableField("create_date")
    @ApiModelProperty(value = "创建时间，不传")
    private Date createDate;

    /**创建时间戳*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间戳，不传")
    private Long createTime;

    @TableField("audit_id")
    @ApiModelProperty(value = "审核人id")
    private Long auditId;

    @TableField("relate_status")
    @ApiModelProperty(value = "关联的状态 0-未绑定 1-已绑定 2-绑定解除")
    private String relateStatus;

    @TableField("nickname")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @TableField("icon")
    @ApiModelProperty(value = "头像")
    private String icon;

    private static final long serialVersionUID = 1L;
}