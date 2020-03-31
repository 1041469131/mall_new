package com.macro.mall.portal.zscat.mallplus;

import com.zscat.mallplus.MallPortalApplication;
import com.zscat.mallplus.manage.service.oms.IOmsExpressCompanyService;
import com.zscat.mallplus.mbg.oms.entity.OmsExpressCompany;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.portal.controller.OmsExpressCompanyController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallPortalApplication.class)
public class TestOmsExpressCompanyController {
    @Autowired
    private OmsExpressCompanyController omsExpressCompanyController;

    @Autowired
    private IOmsExpressCompanyService iOmsExpressCompanyService;

    @Test
    public void testController(){
        CommonResult<List<OmsExpressCompany>> listCommonResult = omsExpressCompanyController.getOmsExpressCompanys(null);
        System.out.println(listCommonResult.getData().size());
    }
}
