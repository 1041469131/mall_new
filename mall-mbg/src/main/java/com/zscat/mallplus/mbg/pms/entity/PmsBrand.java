package com.zscat.mallplus.mbg.pms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 品牌表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("pms_brand")
@ApiModel("品牌")
public class PmsBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    /**
     * 首字母
     */
    @ApiModelProperty("首字母")
    @TableField("first_letter")
    private String firstLetter;

    private Integer sort;

    /**
     * 是否为品牌制造商：0->不是；1->是
     */
    @ApiModelProperty("是否为品牌制造商：0->不是；1->是")
    @TableField("factory_status")
    private Integer factoryStatus;

    @TableField("show_status")
    @ApiModelProperty("是否展示：0->不是；1->是")
    private Integer showStatus;

    /**
     * 产品数量
     */
    @TableField("product_count")
    @ApiModelProperty("产品数量")
    private Integer productCount;

    /**
     * 产品评论数量
     */
    @TableField("product_comment_count")
    @ApiModelProperty("产品评论数量")
    private Integer productCommentCount;

    /**
     * 品牌logo
     */
    @ApiModelProperty("品牌logo")
    private String logo;

    /**
     * 专区大图
     */
    @ApiModelProperty("专区大图")
    @TableField("big_pic")
    private String bigPic;

    /**
     * 品牌故事
     */
    @ApiModelProperty("品牌故事")
    @TableField("brand_story")
    private String brandStory;

    @TableField("delivery_closing_time")
    @ApiModelProperty("发货截单时间")
    private String deliveryClosingTime;

    @TableField("account_type")
    @ApiModelProperty("付款账号类型 1支付宝")
    private String accountType;

    @ApiModelProperty("付款账户")
    @TableField("account")
    private String account;

    @ApiModelProperty("联系手机号")
    @TableField("phone")
    private String phone;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getFactoryStatus() {
        return factoryStatus;
    }

    public void setFactoryStatus(Integer factoryStatus) {
        this.factoryStatus = factoryStatus;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getProductCommentCount() {
        return productCommentCount;
    }

    public void setProductCommentCount(Integer productCommentCount) {
        this.productCommentCount = productCommentCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getBrandStory() {
        return brandStory;
    }

    public void setBrandStory(String brandStory) {
        this.brandStory = brandStory;
    }

    public String getDeliveryClosingTime() {
        return deliveryClosingTime;
    }

    public PmsBrand setDeliveryClosingTime(String deliveryClosingTime) {
        this.deliveryClosingTime = deliveryClosingTime;
        return this;
    }

    public String getAccountType() {
        return accountType;
    }

    public PmsBrand setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public PmsBrand setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public PmsBrand setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public String toString() {
        return "PmsBrand{" +
        ", id=" + id +
        ", name=" + name +
        ", firstLetter=" + firstLetter +
        ", sort=" + sort +
        ", factoryStatus=" + factoryStatus +
        ", showStatus=" + showStatus +
        ", productCount=" + productCount +
        ", productCommentCount=" + productCommentCount +
        ", logo=" + logo +
        ", bigPic=" + bigPic +
        ", brandStory=" + brandStory +
        "}";
    }
}
