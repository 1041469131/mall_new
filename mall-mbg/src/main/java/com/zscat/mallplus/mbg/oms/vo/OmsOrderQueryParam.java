package com.zscat.mallplus.mbg.oms.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单查询参数
 * https://github.com/shenzhuan/mallplus on 2018/10/11.
 */
@Getter
@Setter
public class OmsOrderQueryParam {
    @ApiModelProperty(value = "订单编号")
    private String orderSn;
    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;
    @ApiModelProperty(value = "收货人号码")
    private String receiverPhone;
    @ApiModelProperty(value = "买家手机号码")
    private String memberPhone;
    @ApiModelProperty(value = "物流单号")
    private String deliverySn;
    @ApiModelProperty(value = "商品名称")
    private String productName;
    @ApiModelProperty(value = "所属搭配师昵称")
    private String sysUserNickName;
    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
    @ApiModelProperty(value = "订单类型：0->正常订单；1->秒杀订单")
    private Integer orderType;
    @ApiModelProperty(value = "付款方式 0->未支付；1->支付宝；2->微信")
    private Integer payType;
    @ApiModelProperty(value = "售后类型：类型 0-退款 1-售后")
    private Integer orderSaleType;
    @ApiModelProperty(value = "售后状态：状态 0->待处理；1->退货中；2->已完成；3->已拒绝;4-已撤销，5-寄回退款退货，6-已收货")
    private Integer orderSaleStatus;
    @ApiModelProperty(value = "订单提交时间")
    private String createTime;
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("页的总数据")
    private Integer pageSize;
    @ApiModelProperty("开始时间")
    private Date startDate;
    @ApiModelProperty("结束时间")
    private Date endDate;
    @ApiModelProperty("订单id")
    private Long orderId;
}
