package com.zscat.mallplus.manage.service.pms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;

/**
 * 搭配服务接口
 * @Date: 2019/10/14
 * @Description
 */
public interface IPmsProductMatchLibraryService extends IService<PmsProductMatchLibrary>{
  Page<PmsProductMatchLibrary> listByPage(PmsProductQueryParam queryParam);
}
