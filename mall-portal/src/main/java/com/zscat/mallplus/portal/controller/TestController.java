package com.zscat.mallplus.portal.controller;

import com.zscat.mallplus.manage.service.ums.IUmsMemberBlanceLogService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberBlanceLog;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.li create date 2020/5/19 description
 */

@RequestMapping("/api/test")
@RestController
public class TestController extends ApiBaseAction {
@Autowired
  private IUmsMemberBlanceLogService umsMemberBlanceLogService;
  @ApiOperation("根据订单id取消订单")
  @RequestMapping(value = "/test", method = RequestMethod.POST)
  @ResponseBody
  @IgnoreAuth
  public Object cancelOrderByOrderId(Long orderId) {
   Date date=new Date();
    UmsMemberBlanceLog umsMemberBlanceLog=new UmsMemberBlanceLog();
    umsMemberBlanceLog.setCreateTime(new Date());
    umsMemberBlanceLogService.save(umsMemberBlanceLog);
    return new CommonResult().success(date);
  }


}
