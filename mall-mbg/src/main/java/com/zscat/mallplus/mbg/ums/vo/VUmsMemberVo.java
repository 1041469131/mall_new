package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

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
    private String startTotalAmount;

    @ApiModelProperty("累计消费总最后金额")
    private String endTotalAmount;

    @ApiModelProperty("平均消费总起始金额")
    private String startAvaAmount;

    @ApiModelProperty("平均消费总最后金额")
    private String endAvaAmount;


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

    public String getStartTotalAmount() {
        return startTotalAmount;
    }

    public void setStartTotalAmount(String startTotalAmount) {
        this.startTotalAmount = startTotalAmount;
    }

    public String getEndTotalAmount() {
        return endTotalAmount;
    }

    public void setEndTotalAmount(String endTotalAmount) {
        this.endTotalAmount = endTotalAmount;
    }

    public String getStartAvaAmount() {
        return startAvaAmount;
    }

    public void setStartAvaAmount(String startAvaAmount) {
        this.startAvaAmount = startAvaAmount;
    }

    public String getEndAvaAmount() {
        return endAvaAmount;
    }

    public void setEndAvaAmount(String endAvaAmount) {
        this.endAvaAmount = endAvaAmount;
    }
}
