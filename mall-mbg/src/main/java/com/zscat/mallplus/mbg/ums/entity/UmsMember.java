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

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member")
@ApiModel("会员类")
@Data
public class UmsMember implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long memberLevelId;

    /**用户名*/
    @ApiModelProperty(value = "用户名")
    private String username;

    /**密码*/
    @ApiModelProperty(value = "密码")
    private String password;

    private String nickname;

    /**手机号码*/
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**帐号启用状态:0->禁用；1->启用*/
    @ApiModelProperty(value = "帐号启用状态:0->禁用；1->启用")
    private Integer status;

    /**注册时间*/
    @ApiModelProperty(value = "注册时间")
    private Date createTime;

    /**头像*/
    @ApiModelProperty(value = "头像")
    private String icon;

    /**性别：0->未知；1->男；2->女*/
    @ApiModelProperty(value = "性别：0->未知；1->男；2->女")
    private Integer gender;

    /**生日*/
    @ApiModelProperty(value = "生日")
    private String birthday;

    /**所做城市*/
    @ApiModelProperty(value = "所做城市")
    private String city;

    @ApiModelProperty(value = "所属城市code")
    private String cityCode;

    /**职业*/
    @ApiModelProperty(value = "职业")
    private String job;

    /**个性签名*/
    @ApiModelProperty(value = "个性签名")
    private String personalizedSignature;

    /**用户来源 1 小程序 2 公众号 3 页面*/
    @ApiModelProperty(value = "用户来源 1 小程序 2 公众号 3 页面")
    private Integer sourceType;

    /**积分*/
    @ApiModelProperty(value = "积分")
    private Integer integration;

    /**成长值*/
    @ApiModelProperty(value = "成长值")
    private Integer growth;

    /**剩余抽奖次数*/
    @ApiModelProperty(value = "剩余抽奖次数")
    private Integer luckeyCount;

    /**历史积分数量*/
    @ApiModelProperty(value = "历史积分数量")
    private Integer historyIntegration;

    private String avatar;

    private String weixinOpenid;

    private String sessionKey;

    private Long invitecode;

    /**余额*/
    @ApiModelProperty(value = "余额")
    private String blance;

    private Long schoolId;

    /**体重*/
    @ApiModelProperty(value = "体重")
    private String weight;

    /**身高*/
    @ApiModelProperty(value = "身高")
    private String height;

    /**衬衣尺码*/
    @ApiModelProperty(value = "衬衣尺码")
    private String shirtSize;

    /**裤子 尺码*/
    @ApiModelProperty(value = "裤子尺码")
    private String pantsSize;

    @ApiModelProperty(value = "鞋子尺码")
    private String shoeSize;

    /**体貌特征*/
    @ApiModelProperty(value = "体貌特征")
    private String aspect;

    /**穿衣风格*/
    @ApiModelProperty(value = "穿衣风格")
    private String dressStyle;

    /**穿衣色系*/
    @ApiModelProperty(value = "穿衣色系")
    private String dressColor;

    /**永远都不会穿的风格*/
    @ApiModelProperty(value = "永远都不会穿的风格")
    private String neverDressStyle;

    /**永远都不会穿的图案*/
    @ApiModelProperty(value = "永远都不会穿的图案")
    private String neverDressIcon;

    /**合适面料*/
    @ApiModelProperty(value = "合适面料")
    private String suiteLining;

    /**喜欢的版型*/
    @ApiModelProperty(value = "喜欢的版型")
    private String enjoyModel;

    /**单品的预算*/
    @ApiModelProperty(value = "单品的预算")
    private String itemBudget;

    /**更新时间*/
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**行业*/
    @ApiModelProperty(value = "行业")
    private String industry;

    @TableField(exist = false)
    private String confimpassword;

    /**平衡身材问题*/
    @ApiModelProperty(value = "平衡身材问题")
    private String balanceBody;

    /**平衡身材问题描述*/
    @ApiModelProperty(value = "平衡身材问题描述")
    private String balanceBodyDescr;

    /**永远不会穿风格的样式描述*/
    @ApiModelProperty(value = "永远不会穿风格的样式描述")
    private String neverDressStyleDescr;

    /**永远不会穿图案的样式描述*/
    @ApiModelProperty(value = "永远不会穿图案的样式描述")
    private String neverDressIconDescr;

    /**合适面料描述*/
    @ApiModelProperty(value = "合适面料描述")
    private String suiteLiningDescr;

    /**自我的描述*/
    @ApiModelProperty(value = "自我的描述")
    private String descr;

    /**全身照*/
    @ApiModelProperty(value = "全身照")
    private String bodyUrl;

    @ApiModelProperty(value = "是否已完成 0-未完成 1-已完成")
    private String isComplete;

    @ApiModelProperty(value = "搭配师id")
    private Long matchUserId;

    @ApiModelProperty(value = "更在意衣服的")
    private String careClothes;

    @ApiModelProperty(value = "0-没有注册成功 1-注册成功")
    private String isRegister;

    @ApiModelProperty(value = "衣服的频率")
    private String dressFreq;

    @ApiModelProperty(value = "推荐的搭配数量")
    private String matchCount;

    @ApiModelProperty(value = "创建时间，时间戳")
    private Long createDate;

    @ApiModelProperty("用户印像")
    private String impression;

    @TableField("budget_status")
    @ApiModelProperty("预算状态 0 不接受预算上调 1接受预算上调")
    private Integer budgetStatus;

    private static final long serialVersionUID = 1L;

}