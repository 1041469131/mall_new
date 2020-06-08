package com.zscat.mallplus.mbg.pms.vo;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author xiang.li create date 2020/5/29 description
 */
@Data
@ApiModel("历史单品")
public class PmsProductHistoryVO {
  @ApiModelProperty("喜欢")
  List<PmsProduct> pmsProducts4like;
  @ApiModelProperty("无感")
  List<PmsProduct> pmsProducts4dislike;
  @ApiModelProperty("已购买")
  List<PmsProduct> pmsProducts4Pay;
  @ApiModelProperty("退货")
  List<PmsProduct> pmsProducts4returnSale;
}
