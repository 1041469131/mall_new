package com.zscat.mallplus.mbg.oms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 确认收货提交参数
 * https://github.com/shenzhuan/mallplus on 2018/10/18.
 */
@Getter
@Setter
public class OmsUpdateStatusParam {

    @ApiModelProperty("服务单号")
    private Long id;

    @ApiModelProperty("收货地址关联id")
    private Long companyAddressId;

    @ApiModelProperty("确认退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty("处理备注")
    private String handleNote;

    @ApiModelProperty("处理人")
    private String handleMan;

    @ApiModelProperty("收货备注")
    private String receiveNote;

    @ApiModelProperty("收货人")
    private String receiveMan;

    @ApiModelProperty("申请状态：0->待处理；1->退货中；2->已完成；3->已拒绝 4-已撤销 5-寄回退货,6-已收货 7-等待退款")
    private Integer status;
}
