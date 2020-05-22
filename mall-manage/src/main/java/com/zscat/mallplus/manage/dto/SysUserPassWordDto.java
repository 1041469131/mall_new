package com.zscat.mallplus.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author xiang.li create date 2020/5/20 description
 */
@ApiModel(value="SysUserPassWordDto",description="用户修改密码对象")
public class SysUserPassWordDto implements Serializable {
  @ApiModelProperty(value="用户ID",name="id",example="20")
  private Long id;
  @ApiModelProperty(value="旧密码",name="oldPwd",example="123456")
  private String oldPwd;
  @ApiModelProperty(value="新密码",name="newPwd",example="111111")
  private String newPwd;

  public Long getId() {
    return id;
  }

  public SysUserPassWordDto setId(Long id) {
    this.id = id;
    return this;
  }

  public String getOldPwd() {
    return oldPwd;
  }

  public SysUserPassWordDto setOldPwd(String oldPwd) {
    this.oldPwd = oldPwd;
    return this;
  }

  public String getNewPwd() {
    return newPwd;
  }

  public SysUserPassWordDto setNewPwd(String newPwd) {
    this.newPwd = newPwd;
    return this;
  }
}
