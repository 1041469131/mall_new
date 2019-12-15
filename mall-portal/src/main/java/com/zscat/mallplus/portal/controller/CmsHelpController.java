package com.zscat.mallplus.portal.controller;


import com.zscat.mallplus.manage.service.cms.ICmsHelpCategoryService;
import com.zscat.mallplus.manage.service.cms.ICmsHelpService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.vo.HomeContentResult;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.portal.constant.RedisKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "OmsCartItemController", description = "帮助管理控制")
@RequestMapping("/api/help")
public class CmsHelpController {

    @Autowired
    private ICmsHelpCategoryService iCmsHelpCategoryService;


    @IgnoreAuth
    @ApiOperation("获取帮助信息")
    @RequestMapping(value = "/getHelpsInfos", method = RequestMethod.GET)
    @ResponseBody
    public Object getHelpsInfos() {
        return new CommonResult().success(iCmsHelpCategoryService.getCartProduct());
    }
}
