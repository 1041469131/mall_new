package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.ums.IUmsMatchTimeService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.ums.entity.UmsMatchTime;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Calendar;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.li create date 2020/5/28 description
 */
@Slf4j
@RestController
@Api(tags = "UmsMatchTimeController", description = "搭配时间线")
@RequestMapping("/ums/MatchTime")
public class UmsMatchTimeController {
  @Autowired
  private IUmsMatchTimeService umsMatchTimeService;

  @SysLog(MODULE = "ums", REMARK = "设置搭配时间线")
  @ApiOperation("设置搭配时间线")
  @PostMapping(value = "/update")
  public CommonResult update(@ApiParam("搭配时间线") @RequestBody UmsMatchTime umsMatchTime) {
    try {
      if(umsMatchTime.getStatus()==0){
        //修改时间
        umsMatchTimeService.updateById(umsMatchTime);
      }else{
        //修改当前的时间节点状态
        umsMatchTimeService.saveOrUpdate(umsMatchTime);
        //插入未来的时间节点
        UmsMatchTime umsMatchTimeFuture=new UmsMatchTime();
        BeanUtils.copyProperties(umsMatchTime,umsMatchTimeFuture);
        umsMatchTimeFuture.setId(null);
        umsMatchTimeFuture.setStatus(0);
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(umsMatchTime.getMatchTime());
        calendar.add(Calendar.MONTH, umsMatchTime.getDressFreqMonth());
        umsMatchTimeFuture.setMatchTime(calendar.getTime());
        umsMatchTimeService.save(umsMatchTimeFuture);
      }
       return new CommonResult().success();
    } catch (Exception e) {
      log.error("设置搭配时间线：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
  }


  @SysLog(MODULE = "ums", REMARK = "历史搭配时间线")
  @ApiOperation("历史搭配时间线")
  @PostMapping(value = "/list")
  public CommonResult<List<UmsMatchTime>> list(@ApiParam("搭配参数") @RequestBody UmsMatchTime umsMatchTime) {
    List<UmsMatchTime> list = umsMatchTimeService
      .list(new QueryWrapper<UmsMatchTime>().lambda().eq(UmsMatchTime::getMemberId, umsMatchTime.getMemberId()));
    return new CommonResult().success(list);
  }
}
