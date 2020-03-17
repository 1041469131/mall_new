package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员扩展类
 * @Date: 2019/10/19
 * @Description
 */
public class UmsMemberVo extends UmsMember {

    @ApiModelProperty("穿衣风格名称")
    private String dressTypeName;

    @ApiModelProperty("穿衣的色系名称")
    private String dressColorIdName;

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
    private String careClothes;

    @ApiModelProperty("衣服的频率描述")
    private String dressFreqName;

    @ApiModelProperty("推荐的搭配数量描述")
    private String matchCountName;

    @ApiModelProperty("推荐人id")
    private String recommendId;

    public String getDressTypeName() {
        return dressTypeName;
    }

    public void setDressTypeName(String dressTypeName) {
        this.dressTypeName = dressTypeName;
    }

    public String getDressColorIdName() {
        return dressColorIdName;
    }

    public void setDressColorIdName(String dressColorIdName) {
        this.dressColorIdName = dressColorIdName;
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

    @Override
    public String getCareClothes() {
        return careClothes;
    }

    @Override
    public void setCareClothes(String careClothes) {
        this.careClothes = careClothes;
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
}
