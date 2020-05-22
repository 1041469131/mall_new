package com.zscat.mallplus.mbg.sys.vo;

import com.zscat.mallplus.mbg.sys.entity.SysMatcherAccount;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherStatistics;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 搭配师业绩报酬报表
 */
@ApiModel("搭配师业绩报酬报表实体类")
public class SysMatcherStatisticsVo extends SysMatcherStatistics{

    @ApiModelProperty("搭配师头像")
    private String icon;

    @ApiModelProperty("搭配师昵称")
    private String nickName;

    @ApiModelProperty("搭配师名字")
    private String name;

    @ApiModelProperty("搭配师头像")
    private String phone;

    @ApiModelProperty("搭配师等级")
    private String level;

    @ApiModelProperty("邀请人姓名")
    private String inviteName;

    @ApiModelProperty("邀请人头像")
    private String inviteIcon;

    @ApiModelProperty("邀请人昵称")
    private String inviteNickName;
    @ApiModelProperty("微信昵称")
    private String  wechatNo;

    @ApiModelProperty("搭配师的账号信息")
    private List<SysMatcherAccount> sysMatcherAccounts;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
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

    public String getInviteIcon() {
        return inviteIcon;
    }

    public void setInviteIcon(String inviteIcon) {
        this.inviteIcon = inviteIcon;
    }

    public String getInviteNickName() {
        return inviteNickName;
    }

    public void setInviteNickName(String inviteNickName) {
        this.inviteNickName = inviteNickName;
    }

    public List<SysMatcherAccount> getSysMatcherAccounts() {
        return sysMatcherAccounts;
    }

    public void setSysMatcherAccounts(List<SysMatcherAccount> sysMatcherAccounts) {
        this.sysMatcherAccounts = sysMatcherAccounts;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }
}
