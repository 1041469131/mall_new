package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单佣金的扩展类
 */
@ApiModel("订单佣金的实体对象")
public class OmsMatcherCommissionVo extends OmsMatcherCommission{

    @ApiModelProperty("订单状态")
    private String orderStatus;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("会员头像url")
    private String icon;

    @ApiModelProperty("会员昵称(前端查询条件会员名称或者姓名用这个字段来传值)")
    private String nickname;

    @ApiModelProperty("会员的别名")
    private String personalizedSignature;

    @ApiModelProperty("会员的手机号")
    private String phone;

    @ApiModelProperty("搭配师或者所属搭配师是头像url")
    private String matcherIcon;

    @ApiModelProperty("搭配师或者所属搭配师昵称")
    private String matcherNickName;

    @ApiModelProperty("搭配师或者所属搭配师姓名(查询条件用这个)")
    private String matcherName;

    @ApiModelProperty("订单金额（订单实际付款金额）")
    private BigDecimal payAmount;

    @ApiModelProperty("订单的创建时间")
    private Date orderCreateTime;

    @ApiModelProperty("邀请人的昵称")
    private String inviteNickName;

    @ApiModelProperty("邀请人的头像")
    private String inviteIcon;

    @ApiModelProperty("邀请人的名称")
    private String inviteName;

    @ApiModelProperty("订单的开始时间")
    private String startOrderDate;

    @ApiModelProperty("订单的结束时间")
    private String endOrderDate;

    @ApiModelProperty("订单结算状态(根据结算的状态进行查询，需要前端传入，多个用逗号分开)")
    private String settleStatuses;

    @ApiModelProperty("订单结算状态，后台sql查询使用，不需要前端传入")
    private String[] statusList;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonalizedSignature() {
        return personalizedSignature;
    }

    public void setPersonalizedSignature(String personalizedSignature) {
        this.personalizedSignature = personalizedSignature;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMatcherIcon() {
        return matcherIcon;
    }

    public void setMatcherIcon(String matcherIcon) {
        this.matcherIcon = matcherIcon;
    }

    public String getMatcherNickName() {
        return matcherNickName;
    }

    public void setMatcherNickName(String matcherNickName) {
        this.matcherNickName = matcherNickName;
    }

    public String getMatcherName() {
        return matcherName;
    }

    public void setMatcherName(String matcherName) {
        this.matcherName = matcherName;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getInviteNickName() {
        return inviteNickName;
    }

    public void setInviteNickName(String inviteNickName) {
        this.inviteNickName = inviteNickName;
    }

    public String getInviteIcon() {
        return inviteIcon;
    }

    public void setInviteIcon(String inviteIcon) {
        this.inviteIcon = inviteIcon;
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

    public String getStartOrderDate() {
        return startOrderDate;
    }

    public void setStartOrderDate(String startOrderDate) {
        this.startOrderDate = startOrderDate;
    }

    public String getEndOrderDate() {
        return endOrderDate;
    }

    public void setEndOrderDate(String endOrderDate) {
        this.endOrderDate = endOrderDate;
    }

    public String getSettleStatuses() {
        return settleStatuses;
    }

    public void setSettleStatuses(String settleStatuses) {
        this.settleStatuses = settleStatuses;
    }

    public String[] getStatusList() {
        return statusList;
    }

    public void setStatusList(String[] statusList) {
        this.statusList = statusList;
    }
}
