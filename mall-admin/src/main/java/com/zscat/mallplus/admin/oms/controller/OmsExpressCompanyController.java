package com.zscat.mallplus.admin.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.oms.IOmsExpressCompanyService;
import com.zscat.mallplus.mbg.oms.entity.OmsExpressCompany;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(tags = "OmsExpressCompanyController", description = "物流公司管理")
@RequestMapping("/oms/express")
public class OmsExpressCompanyController {

    @Autowired
    IOmsExpressCompanyService iOmsExpressCompanyService;

    @ApiOperation("查询物流公司")
    @RequestMapping(value = "/getOmsExpressCompanys")
    @ResponseBody
    public CommonResult<List<OmsExpressCompany>> getOmsExpressCompanys(String companyName){
        List<OmsExpressCompany> omsExpressCompanies = null;
        if(StringUtils.isNotEmpty(companyName)){
            omsExpressCompanies = iOmsExpressCompanyService.list(new QueryWrapper<OmsExpressCompany>().
                    like("EXPRESS_CORP_NAME",companyName).eq("STATUS","0"));
        }else{
            omsExpressCompanies = iOmsExpressCompanyService.list(new QueryWrapper<OmsExpressCompany>().
                    eq("STATUS","0"));
        }
        return new CommonResult<>().success(omsExpressCompanies);
    }
}
