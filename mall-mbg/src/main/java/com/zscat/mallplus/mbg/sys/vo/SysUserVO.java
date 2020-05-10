package com.zscat.mallplus.mbg.sys.vo;

import com.zscat.mallplus.mbg.sys.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("系统用户参数")
public class SysUserVO extends SysUser{

    @ApiModelProperty("加入的开始时间")
    private String startCreateDate;

    @ApiModelProperty("加入的结束时间")
    private String endCreateDate;

    @ApiModelProperty("邀请人的手机号")
    private String invitePhone;

    @ApiModelProperty("邀请人的昵称和备注名")
    private String inviteName;

    @ApiModelProperty("累计邀请的搭配师树")
    private Integer matcherCount;

    @ApiModelProperty("粉丝数量")
    private Integer fanCount;

    @ApiModelProperty("总的收益")
    private BigDecimal totalProfit;

    @ApiModelProperty("关系绑定时间(时间戳)")
    private Long relateUpdateTime;

    @ApiModelProperty("关系绑定时间")
    private Date relateUpdateDate;

    @ApiModelProperty("关联的状态 0-未绑定 1-已绑定 2-绑定解除")
    private String relateStatus;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

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

    public String getInvitePhone() {
        return invitePhone;
    }

    public void setInvitePhone(String invitePhone) {
        this.invitePhone = invitePhone;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public Integer getMatcherCount() {
        return matcherCount;
    }

    public void setMatcherCount(Integer matcherCount) {
        this.matcherCount = matcherCount;
    }

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getRelateUpdateTime() {
        return relateUpdateTime;
    }

    public void setRelateUpdateTime(Long relateUpdateTime) {
        this.relateUpdateTime = relateUpdateTime;
    }

    public Date getRelateUpdateDate() {
        return relateUpdateDate;
    }

    public void setRelateUpdateDate(Date relateUpdateDate) {
        this.relateUpdateDate = relateUpdateDate;
    }

    public String getRelateStatus() {
        return relateStatus;
    }

    public void setRelateStatus(String relateStatus) {
        this.relateStatus = relateStatus;
    }
}
