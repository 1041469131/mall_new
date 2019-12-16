package com.zscat.mallplus.portal.controller;


import com.zscat.mallplus.manage.service.sys.impl.OssServiceImpl;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.sys.vo.OssCallbackResult;
import com.zscat.mallplus.mbg.sys.vo.OssPolicyResult;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.portal.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Oss相关操作接口
 * https://github.com/shenzhuan/mallplus on 2018/4/26.
 */
@Controller
@Api(tags = "OssController", description = "Oss管理")
@RequestMapping("/api/aliyun/oss")
public class OssController {

    @Autowired
    private OssServiceImpl ossService;

    @Value("${logistics.host}")
    private String host;

    @Value("${logistics.path}")
    private String path;

    @Value("${logistics.method}")
    private String method;

    @Value("${logistics.appcode}")
    private String appcode;

    @ApiOperation(value = "oss上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public Object policy() {
        OssPolicyResult result = ossService.policy();
        return new CommonResult().success(result);
    }

    @ApiOperation(value = "oss上传成功回调")
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    @ResponseBody
    public Object callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return new CommonResult().success(ossCallbackResult);
    }

    @IgnoreAuth
    @ApiOperation(value = "获取物流相关信息")
    @RequestMapping(value = "getLogisticsInfos", method = RequestMethod.GET)
    @ResponseBody
    public Object getLogisticsInfos(String no,String type) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("no", no);
        if(!StringUtils.isEmpty(type)){
            querys.put("no", type);
        }
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String respon = EntityUtils.toString(response.getEntity());
            return new CommonResult().success(respon);
        }catch (Exception e){
            return new CommonResult().failed(e.getMessage());
        }
    }


}
