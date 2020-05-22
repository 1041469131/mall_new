package com.zscat.mallplus.mbg.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel("收款账户")
@Data
public class SysUserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**用户id*/
    @TableField("sys_user_id")
    @ApiModelProperty(value = "用户id")
    private Long sysUserId;

    /**微信号*/
    @TableField("wechat_name")
    @ApiModelProperty(value = "微信号")
    private String wechatName;

    /**电话号码*/
    @TableField("phone")
    @ApiModelProperty(value = "电话号码")
    private String phone;

    /**支付宝实名*/
    @TableField("alipay_real_name")
    @ApiModelProperty(value = "支付宝实名")
    private String alipayRealName;

    /**支付宝绑定电话号码*/
    @TableField("alipay_bind_phone")
    @ApiModelProperty(value = "支付宝绑定电话号码")
    private String alipayBindPhone;

    /**开户名*/
    @TableField("account_name")
    @ApiModelProperty(value = "开户名")
    private String accountName;

    /**银行卡号*/
    @TableField("bank_card")
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;

    /**开户银行*/
    @TableField("bank_name")
    @ApiModelProperty(value = "开户银行")
    private String bankName;

    /**微信收款二维码*/
    @TableField("wechat_account_url")
    @ApiModelProperty(value = "微信收款二维码")
    private String wechatAccountUrl;

    /**支付宝收款二维码*/
    @TableField("alipay_account_url")
    @ApiModelProperty(value = "支付宝收款二维码")
    private String alipayAccountUrl;


}