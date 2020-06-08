package com.zscat.mallplus.mbg.pms.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 产品查询参数
 */
@Data
public class PmsProductQueryParam {

    @ApiModelProperty("上架状态")
    private Integer publishStatus;

    @ApiModelProperty("审核状态")
    private Integer verifyStatus;

    @TableField("delete_status")
    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    private Integer deleteStatus;

    @ApiModelProperty("商品名称模糊关键字")
    private String keyword;

    @ApiModelProperty("商品货号")
    private String productSn;

    @ApiModelProperty("商品分类编号")
    private Long productCategoryId;

    @ApiModelProperty("商品品牌编号")
    private Long brandId;

    @ApiModelProperty("起始价格区间")
    private BigDecimal startPrice;

    @ApiModelProperty("结束价格区间")
    private BigDecimal endPrice;

    @ApiModelProperty("起始销量区间")
    private Integer startSale;

    @ApiModelProperty("结束销量区间")
    private Integer endSale;

    @ApiModelProperty("搭配师id")
    private Long matchUserId;

    @ApiModelProperty("会员id")
    private Long memberId;

    @ApiModelProperty(value = "收藏的状态 0-为收藏 1-已收藏")
    private String collectStatus;

    @ApiModelProperty(value = "搭配库的归属类型 0-个人 1-公司")
    private String matchOwer;

    @ApiModelProperty("商品分类编号")
    private Long productAttributeCategoryId;

    private Integer pageSize = 10;
    private Integer pageNum = 1;
    private String  sort = "price asc"; // 1 asc

    @ApiModelProperty(value = "收藏的状态 0-为推荐 1-已推荐，不传后台默认为0")
    private String recommendType;



}
