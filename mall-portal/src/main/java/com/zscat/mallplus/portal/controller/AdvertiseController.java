package com.zscat.mallplus.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.marking.ISmsHomeAdvertiseService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@Api(tags = "AdvertiseController", description = "轮播图片")
@RequestMapping("/api/advertise")
public class AdvertiseController {


    @Autowired
    private ISmsHomeAdvertiseService iSmsHomeAdvertiseService;

    @IgnoreAuth
    @ApiOperation("过去轮播图片信息")
    @RequestMapping(value = "/advertises", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult advertises() {
        List<SmsHomeAdvertise> advertises = iSmsHomeAdvertiseService.list(new QueryWrapper<SmsHomeAdvertise>().
                eq("type", MagicConstant.AD_TYPE_APP).eq("status",MagicConstant.AD_STATUS_ONLINE).le("start_time",new Date()).gt("end_time",new Date()));
        return new CommonResult().success(advertises);
    }
}
