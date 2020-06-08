package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberLevelId() {
        return memberLevelId;
    }

    public void setMemberLevelId(Long memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getLuckeyCount() {
        return luckeyCount;
    }

    public void setLuckeyCount(Integer luckeyCount) {
        this.luckeyCount = luckeyCount;
    }

    public Integer getHistoryIntegration() {
        return historyIntegration;
    }

    public void setHistoryIntegration(Integer historyIntegration) {
        this.historyIntegration = historyIntegration;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWeixinOpenid() {
        return weixinOpenid;
    }

    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid;
    }

    public Long getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(Long invitecode) {
        this.invitecode = invitecode;
    }

    public String getBlance() {
        return blance;
    }

    public void setBlance(String blance) {
        this.blance = blance;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public String getPantsSize() {
        return pantsSize;
    }

    public void setPantsSize(String pantsSize) {
        this.pantsSize = pantsSize;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getDressStyle() {
        return dressStyle;
    }

    public void setDressStyle(String dressStyle) {
        this.dressStyle = dressStyle;
    }

    public String getDressColor() {
        return dressColor;
    }

    public void setDressColor(String dressColor) {
        this.dressColor = dressColor;
    }

    public String getNeverDressStyle() {
        return neverDressStyle;
    }

    public void setNeverDressStyle(String neverDressStyle) {
        this.neverDressStyle = neverDressStyle;
    }

    public String getNeverDressIcon() {
        return neverDressIcon;
    }

    public void setNeverDressIcon(String neverDressIcon) {
        this.neverDressIcon = neverDressIcon;
    }

    public String getSuiteLining() {
        return suiteLining;
    }

    public void setSuiteLining(String suiteLining) {
        this.suiteLining = suiteLining;
    }

    public String getEnjoyModel() {
        return enjoyModel;
    }

    public void setEnjoyModel(String enjoyModel) {
        this.enjoyModel = enjoyModel;
    }

    public String getItemBudget() {
        return itemBudget;
    }

    public void setItemBudget(String itemBudget) {
        this.itemBudget = itemBudget;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getConfimpassword() {
        return confimpassword;
    }

    public void setConfimpassword(String confimpassword) {
        this.confimpassword = confimpassword;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getBalanceBody() {
        return balanceBody;
    }

    public void setBalanceBody(String balanceBody) {
        this.balanceBody = balanceBody;
    }

    public String getBalanceBodyDescr() {
        return balanceBodyDescr;
    }

    public void setBalanceBodyDescr(String balanceBodyDescr) {
        this.balanceBodyDescr = balanceBodyDescr;
    }

    public String getNeverDressStyleDescr() {
        return neverDressStyleDescr;
    }

    public void setNeverDressStyleDescr(String neverDressStyleDescr) {
        this.neverDressStyleDescr = neverDressStyleDescr;
    }

    public String getNeverDressIconDescr() {
        return neverDressIconDescr;
    }

    public void setNeverDressIconDescr(String neverDressIconDescr) {
        this.neverDressIconDescr = neverDressIconDescr;
    }

    public String getSuiteLiningDescr() {
        return suiteLiningDescr;
    }

    public void setSuiteLiningDescr(String suiteLiningDescr) {
        this.suiteLiningDescr = suiteLiningDescr;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getBodyUrl() {
        return bodyUrl;
    }

    public void setBodyUrl(String bodyUrl) {
        this.bodyUrl = bodyUrl;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public Long getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(Long matchUserId) {
        this.matchUserId = matchUserId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getDressFreq() {
        return dressFreq;
    }

    public void setDressFreq(String dressFreq) {
        this.dressFreq = dressFreq;
    }

    public String getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(String matchCount) {
        this.matchCount = matchCount;
    }

    public String getCareClothes() {
        return careClothes;
    }

    public void setCareClothes(String careClothes) {
        this.careClothes = careClothes;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getImpression() {
        return impression;
    }

    public UmsMember setImpression(String impression) {
        this.impression = impression;
        return this;
    }

    public Integer getBudgetStatus() {
        return budgetStatus;
    }

    public UmsMember setBudgetStatus(Integer budgetStatus) {
        this.budgetStatus = budgetStatus;
        return this;
    }
}