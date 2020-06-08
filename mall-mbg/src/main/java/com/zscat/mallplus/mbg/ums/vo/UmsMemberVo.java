package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsMatchTime;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 会员扩展类
 * @Date: 2019/10/19
 * @Description
 */
public class UmsMemberVo extends UmsMember {

    @ApiModelProperty("穿衣风格名称")
    private String dressStyleName;

    @ApiModelProperty("穿衣的色系名称")
    private String dressColorName;

    @ApiModelProperty("重来不穿的风格名称")
    private String neverDressStyleName;

    @ApiModelProperty("永远不穿的图案名称")
    private String neverDressIconName;

    @ApiModelProperty("合适的面料名称")
    private String suiteLiningName;

    @ApiModelProperty("喜欢的版型名称")
    private String enjoyModelName;

    @ApiModelProperty("单品的预算名称")
    private String itemBudgetName;

    @ApiModelProperty("平衡身体名称")
    private String balanceBodyName;

    @ApiModelProperty("体貌特征名称")
    private String aspectName;

    @ApiModelProperty("更在意衣服的")
    private String careClothesName;

    @ApiModelProperty("衣服的频率描述")
    private String dressFreqName;

    @ApiModelProperty("推荐的搭配数量描述")
    private String matchCountName;

    @ApiModelProperty("推荐人id")
    private String recommendId;

    @ApiModelProperty("搭配师的手机号")
    private String matcherUserPhone;
    @ApiModelProperty("搭配推送时间间隔")
    private Integer dressFreqMonth;

    @ApiModelProperty("搭配通知状态")
    private UmsMatchTime umsMatchTime;

    @ApiModelProperty("用户标签")
    private List<UmsMemberTag> umsMemberTags;

    public String getDressStyleName() {
        return dressStyleName;
    }

    public void setDressStyleName(String dressStyleName) {
        this.dressStyleName = dressStyleName;
    }

    public String getDressColorName() {
        return dressColorName;
    }

    public void setDressColorName(String dressColorName) {
        this.dressColorName = dressColorName;
    }

    public String getNeverDressStyleName() {
        return neverDressStyleName;
    }

    public void setNeverDressStyleName(String neverDressStyleName) {
        this.neverDressStyleName = neverDressStyleName;
    }

    public String getNeverDressIconName() {
        return neverDressIconName;
    }

    public void setNeverDressIconName(String neverDressIconName) {
        this.neverDressIconName = neverDressIconName;
    }

    public String getSuiteLiningName() {
        return suiteLiningName;
    }

    public void setSuiteLiningName(String suiteLiningName) {
        this.suiteLiningName = suiteLiningName;
    }

    public String getEnjoyModelName() {
        return enjoyModelName;
    }

    public void setEnjoyModelName(String enjoyModelName) {
        this.enjoyModelName = enjoyModelName;
    }

    public String getItemBudgetName() {
        return itemBudgetName;
    }

    public void setItemBudgetName(String itemBudgetName) {
        this.itemBudgetName = itemBudgetName;
    }

    public String getBalanceBodyName() {
        return balanceBodyName;
    }

    public void setBalanceBodyName(String balanceBodyName) {
        this.balanceBodyName = balanceBodyName;
    }

    public String getAspectName() {
        return aspectName;
    }

    public void setAspectName(String aspectName) {
        this.aspectName = aspectName;
    }

    public String getCareClothesName() {
        return careClothesName;
    }

    public void setCareClothesName(String careClothesName) {
        this.careClothesName = careClothesName;
    }

    public String getDressFreqName() {
        return dressFreqName;
    }

    public void setDressFreqName(String dressFreqName) {
        this.dressFreqName = dressFreqName;
    }

    public String getMatchCountName() {
        return matchCountName;
    }

    public void setMatchCountName(String matchCountName) {
        this.matchCountName = matchCountName;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getMatcherUserPhone() {
        return matcherUserPhone;
    }

    public void setMatcherUserPhone(String matcherUserPhone) {
        this.matcherUserPhone = matcherUserPhone;
    }

    public Integer getDressFreqMonth() {
        return dressFreqMonth;
    }

    public UmsMemberVo setDressFreqMonth(Integer dressFreqMonth) {
        this.dressFreqMonth = dressFreqMonth;
        return this;
    }

    public UmsMatchTime getUmsMatchTime() {
        return umsMatchTime;
    }

    public UmsMemberVo setUmsMatchTime(UmsMatchTime umsMatchTime) {
        this.umsMatchTime = umsMatchTime;
        return this;
    }

    public List<UmsMemberTag> getUmsMemberTags() {
        return umsMemberTags;
    }

    public UmsMemberVo setUmsMemberTags(List<UmsMemberTag> umsMemberTags) {
        this.umsMemberTags = umsMemberTags;
        return this;
    }
}
