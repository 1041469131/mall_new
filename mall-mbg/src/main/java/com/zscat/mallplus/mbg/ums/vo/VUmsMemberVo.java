package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsMatchTime;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;
import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@ApiModel("粉丝和平台管理扩展对象")
@Data
public class VUmsMemberVo extends VUmsMember {

    @ApiModelProperty("推荐数量")
    private Integer recomendCount;

    @ApiModelProperty("处理的状态 0-待处理 1-急需处理 2-已处理")
    private String recommendStatus;

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

    @ApiModelProperty("创建时间的时间戳")
    private Long createDate;

    @ApiModelProperty("系统的类型 0-搭配师平台 1-平台后管")
    private String systemType;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("页的总数据")
    private Integer pageSize;

    @ApiModelProperty("搭配通知状态")
    private UmsMatchTime umsMatchTime;

    @ApiModelProperty("用户标签")
    private List<UmsMemberTag> umsMemberTags;
    @ApiModelProperty("穿衣频率")
    private String dressFreq;
}
