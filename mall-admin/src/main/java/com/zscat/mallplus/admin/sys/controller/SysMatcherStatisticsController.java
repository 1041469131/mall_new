package com.zscat.mallplus.admin.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.sys.mapper.SysMatcherStatisticsMapper;
import com.zscat.mallplus.mbg.sys.vo.SysMatcherStatisticsVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "SysMatcherStatisticsController", description = "搭配师业绩结算管理")
@RequestMapping("/sys/matcherStatistics")
public class SysMatcherStatisticsController {

    @Autowired
    private ISysMatcherStatisticsService iSysMatcherStatisticsService;

    @Autowired
    private SysMatcherStatisticsMapper sysMatcherStatisticsMapper;

    @SysLog(MODULE = "sys", REMARK = "分页查询搭配师业绩报酬")
    @ApiOperation("分页查询搭配师业绩报酬")
    @PostMapping(value = "/pageMatherStatistics")
//    @PreAuthorize("hasAuthority('sys:SysMemberArea:read')")
    public Object pageMatherStatistics(@RequestBody SysMatcherStatisticsVo sysMatcherStatisticsVo) {
        try {
            Page<SysMatcherStatisticsVo> sysMatcherStatisticsVos = iSysMatcherStatisticsService.pageMatherStatistics(sysMatcherStatisticsVo);
            return new CommonResult().success(sysMatcherStatisticsVos);
        } catch (Exception e) {
            log.error("根据条件查询所有列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "查询单个搭配师业绩报酬")
    @ApiOperation("查询单个搭配师业绩报酬")
    @PostMapping(value = "/querySysMatcherStatistics")
//    @PreAuthorize("hasAuthority('sys:SysMemberArea:read')")
    public Object querySysMatcherStatistics(Long matcherUserId) {
        try {
            SysMatcherStatisticsVo sysMatcherStatisticsVo = iSysMatcherStatisticsService.querySysMatcherStatistics(matcherUserId);
            return new CommonResult().success(sysMatcherStatisticsVo);
        } catch (Exception e) {
            log.error("根据条件查询所有列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "分页查询搭配师业绩报酬")
    @ApiOperation("分页查询搭配师业绩报酬")
    @PostMapping(value = "/aaa")

//    @PreAuthorize("hasAuthority('sys:SysMemberArea:read')")
    public Object aaa() {
        Map<String,Object> paramMap = new HashMap<>();
        List<Integer> orderStatus = new ArrayList<>();
        orderStatus.add(MagicConstant.ORDER_STATUS_WAIT_PAY);
        orderStatus.add(MagicConstant.ORDER_STATUS_WAIT_SEND);
        orderStatus.add(MagicConstant.ORDER_STATUS_YET_SEND);
        orderStatus.add(MagicConstant.ORDER_STATUS_YET_DONE);
        paramMap.put("statusList",orderStatus);
        HashMap<String, Object> allOrder = sysMatcherStatisticsMapper.getAmount(paramMap);
        return new CommonResult().success(allOrder);
    }


}
