package com.zscat.mallplus.mbg.ums.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiang.li create date 2020/5/27 description
 */
@Data
@ApiModel("查询名称")
public class UmsMemberQueryParam {
  @ApiModelProperty("昵称")
  private String nickName;
  @ApiModelProperty("备注名")
  private String personalizedSignature;
  @ApiModelProperty("电话号码")
  private String phone;
  @ApiModelProperty("关键字")
  private String keyWord;

  private Integer pageNum;

  private  Integer pageSize;

  private Long id;

  Long matcherUserId;

}
