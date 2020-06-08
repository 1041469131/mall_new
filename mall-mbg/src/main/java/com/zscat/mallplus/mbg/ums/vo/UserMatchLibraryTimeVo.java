package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsMatchTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author xiang.li create date 2020/5/27 description
 */
@Data
@ApiModel("搭配推荐通知")
public class UserMatchLibraryTimeVo extends UmsMatchTime {
  @ApiModelProperty("搭配推送时间间隔")
  private Integer dressFreqMonth;
}
