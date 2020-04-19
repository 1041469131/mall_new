package com.zscat.mallplus.mbg.sys.vo;

import com.zscat.mallplus.mbg.sys.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
}
