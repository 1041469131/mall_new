package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductMatchLibraryService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductMatchLibraryMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 搭配的服务类
 * @Date: 2019/10/14
 * @Description
 */
@Service
public class PmsProductMatchLibraryServiceImpl extends ServiceImpl<PmsProductMatchLibraryMapper,PmsProductMatchLibrary> implements IPmsProductMatchLibraryService {
  @Autowired
  private PmsProductMatchLibraryMapper pmsProductMatchLibraryMapper;
  @Override
  public Page<PmsProductMatchLibrary> listByPage(PmsProductQueryParam queryParam) {
    return pmsProductMatchLibraryMapper.listByPage(new Page<>(queryParam.getPageNum(),queryParam.getPageSize()),queryParam);
  }
}
