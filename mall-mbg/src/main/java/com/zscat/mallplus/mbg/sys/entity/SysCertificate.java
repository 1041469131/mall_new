package com.zscat.mallplus.mbg.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@ApiModel("用户证书")
@Data
public class SysCertificate implements Serializable {
    private Long id;

    /**电话号码*/
    @TableField("phone")
    @ApiModelProperty(value = "电话号码")
    private String phone;

    /**身份证号*/
    @TableField("identity_card")
    @ApiModelProperty(value = "身份证号")
    private String identityCard;

    /**类型*/
    @TableField("type")
    @ApiModelProperty(value = "类型 1是搭配师证书")
    private Integer type;

    /**编号*/
    @TableField("card_number")
    @ApiModelProperty(value = "编号")
    private String cardNumber;

    /**用户名*/
    @TableField("name")
    @ApiModelProperty(value = "用户名")
    private String name;

    @TableField("status")
    @ApiModelProperty(value = "状态 0 失效 1有效")
    private  Integer status;

    private Long sysUserId;

    @TableField("create_date")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @TableField("expiry_date")
    @ApiModelProperty(value = "过期时间")
    private Date expiryDate;

    private static final long serialVersionUID = 1L;
}