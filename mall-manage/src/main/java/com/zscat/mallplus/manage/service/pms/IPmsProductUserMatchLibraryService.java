package com.zscat.mallplus.manage.service.pms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import io.swagger.annotations.ApiParam;

/**
 * 用户搭配是服务
 * @Date: 2019/10/19
 * @Description
 */
public interface IPmsProductUserMatchLibraryService extends IService<PmsProductUserMatchLibrary> {

    boolean saveProductUserMatch( String matchIdParam,String recommType);
    Page<PmsProductUserMatchLibrary> listByPage(PmsProductQueryParam queryParam);
}
