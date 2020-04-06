package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("粉丝和平台管理扩展对象")
public class VUmsMemberVo extends VUmsMember {

    @ApiModelProperty("推荐数量")
    private Integer recomendCount;

    @ApiModelProperty("处理的状态 0-待处理 1-急需处理 2-已处理")
    private String status;

    @ApiModelProperty("粉丝的创建开始时间")
    private String startCreateDate;

    @ApiModelProperty("粉丝的创建结束时间")
    private String endCreateDate;

    @ApiModelProperty("上次推荐的开始时间")
    private String startRecommendDate;

    @ApiModelProperty("上次推荐的结束时间")
    private String endRecommendDate;

    @ApiModelProperty("累计消费总起始金额")
    private BigDecimal startTotalAmount;

    @ApiModelProperty("累计消费总最后金额")
    private BigDecimal endTotalAmount;

    @ApiModelProperty("平均消费总起始金额")
    private BigDecimal startAvaAmount;

    @ApiModelProperty("平均消费总最后金额")
    private BigDecimal endAvaAmount;

    @ApiModelProperty("粉丝备注姓名")
    private String personalizedSignature;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    public Integer getRecomendCount() {
        return recomendCount;
    }

    public void setRecomendCount(Integer recomendCount) {
        this.recomendCount = recomendCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartCreateDate() {
        return startCreateDate;
    }

    public void setStartCreateDate(String startCreateDate) {
        this.startCreateDate = startCreateDate;
    }

    public String getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(String endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    public String getStartRecommendDate() {
        return startRecommendDate;
    }

    public void setStartRecommendDate(String startRecommendDate) {
        this.startRecommendDate = startRecommendDate;
    }

    public String getEndRecommendDate() {
        return endRecommendDate;
    }

    public void setEndRecommendDate(String endRecommendDate) {
        this.endRecommendDate = endRecommendDate;
    }

    public BigDecimal getStartTotalAmount() {
        return startTotalAmount;
    }

    public void setStartTotalAmount(BigDecimal startTotalAmount) {
        this.startTotalAmount = startTotalAmount;
    }

    public BigDecimal getEndTotalAmount() {
        return endTotalAmount;
    }

    public void setEndTotalAmount(BigDecimal endTotalAmount) {
        this.endTotalAmount = endTotalAmount;
    }

    public BigDecimal getStartAvaAmount() {
        return startAvaAmount;
    }

    public void setStartAvaAmount(BigDecimal startAvaAmount) {
        this.startAvaAmount = startAvaAmount;
    }

    public BigDecimal getEndAvaAmount() {
        return endAvaAmount;
    }

    public void setEndAvaAmount(BigDecimal endAvaAmount) {
        this.endAvaAmount = endAvaAmount;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }
}
