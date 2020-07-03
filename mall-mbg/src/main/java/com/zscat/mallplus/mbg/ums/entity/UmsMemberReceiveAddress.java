package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 会员收货地址表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_receive_address")
@Data
public class UmsMemberReceiveAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    /**
     * 收货人名称
     */
    @ApiModelProperty("收货人名称")
    private String name;

    @TableField("phone_number")
    @ApiModelProperty("手机号")
    private String phoneNumber;

    /**
     * 是否为默认
     */
    @TableField("default_status")
    @ApiModelProperty("是否是默认 1-是 0-否")
    private Integer defaultStatus;

    /**
     * 邮政编码
     */
    @TableField("post_code")
    @ApiModelProperty("邮政编码")
    private String postCode;

    /**
     * 省份/直辖市
     */
    @ApiModelProperty("省份/直辖市")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty("城市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String region;

    /**
     * 详细地址(街道)
     */
    @ApiModelProperty("详细地址")
    @TableField("detail_address")
    private String detailAddress;

    /**创建时间*******/
    private Date createTime;

    /**更新时间******/
    private Date updateTime;
}
