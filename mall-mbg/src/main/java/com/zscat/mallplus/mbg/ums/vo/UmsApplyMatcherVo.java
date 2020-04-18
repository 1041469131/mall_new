package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("搭配师审核对象")
public class UmsApplyMatcherVo extends UmsApplyMatcher{

    @ApiModelProperty("会员搭配师昵称")
    private String nickname;

    @ApiModelProperty("会员搭配师微信头像")
    private String icon;

    @ApiModelProperty("邀请人昵称")
    private String invitenickname;

    @ApiModelProperty("邀请人备注名")
    private String inviteremarkname;

    @ApiModelProperty("申请的开始时间")
    private String startApplyDate;

    @ApiModelProperty("申请的结束时间")
    private String endApplyDate;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInvitenickname() {
        return invitenickname;
    }

    public void setInvitenickname(String invitenickname) {
        this.invitenickname = invitenickname;
    }

    public String getInviteremarkname() {
        return inviteremarkname;
    }

    public void setInviteremarkname(String inviteremarkname) {
        this.inviteremarkname = inviteremarkname;
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

    public String getStartApplyDate() {
        return startApplyDate;
    }

    public void setStartApplyDate(String startApplyDate) {
        this.startApplyDate = startApplyDate;
    }

    public String getEndApplyDate() {
        return endApplyDate;
    }

    public void setEndApplyDate(String endApplyDate) {
        this.endApplyDate = endApplyDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
