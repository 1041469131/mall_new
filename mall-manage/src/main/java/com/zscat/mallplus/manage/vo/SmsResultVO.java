package com.zscat.mallplus.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiang.li create date 2020/5/21 description
 */
@Data
@ApiModel(value="SmsResultVO",description="短信返回值对象")
public class SmsResultVO {
  @ApiModelProperty(value="验证码")
  private String authCode;
  @ApiModelProperty(value="验证码校验状态")
  private Boolean checkFlag;


}
