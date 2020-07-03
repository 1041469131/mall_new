package com.zscat.mallplus.mbg.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台用户表
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Data
@TableName("sys_user")
@ApiModel("用户")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String icon;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 昵称
     */
    @TableField("nick_name")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String note;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 最后登录时间
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    @ApiModelProperty(value = "账号启用状态")
    private Integer status;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    @TableField("supply_id")
    private Long supplyId;

    //角色
    @TableField(exist = false)
    @ApiModelProperty(value = "角色")
    private String roleIds;

    @ApiModelProperty(value = "微信名")
    private String wechatName;

    @ApiModelProperty(value = "微信二维码地址")
    private String wechatQrcodeUrl;

    @ApiModelProperty(value = "接入微信企业号Id")
    private String plugId;

    @ApiModelProperty(value = "简介")
    private String introduction;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "类型 0-搭配师")
    private String type;

    @ApiModelProperty(value = "等级 common:普通搭配师，advanced：高级搭配师")
    private String level;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "更新日期")
    private Date updateDate;

    @ApiModelProperty(value = "更新日期（时间戳）")
    private Long updateTime;

    @ApiModelProperty(value = "创建日期（时间戳）")
    private Long createDate;

}
