package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会员收货地址表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_receive_address")
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UmsMemberReceiveAddress{" +
        ", id=" + id +
        ", memberId=" + memberId +
        ", name=" + name +
        ", phoneNumber=" + phoneNumber +
        ", defaultStatus=" + defaultStatus +
        ", postCode=" + postCode +
        ", province=" + province +
        ", city=" + city +
        ", region=" + region +
        ", detailAddress=" + detailAddress +
        "}";
    }
}
