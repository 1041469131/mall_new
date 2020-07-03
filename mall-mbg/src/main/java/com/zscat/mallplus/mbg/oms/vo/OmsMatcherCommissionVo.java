package com.zscat.mallplus.mbg.oms.vo;

import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单佣金的扩展类
 */
@ApiModel("订单佣金的实体对象")
@Data
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

    @ApiModelProperty("确认收货时间")
    private Date receiveTime;
}
