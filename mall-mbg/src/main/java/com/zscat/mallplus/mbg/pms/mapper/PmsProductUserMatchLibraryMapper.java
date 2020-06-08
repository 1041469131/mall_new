package com.zscat.mallplus.mbg.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import org.apache.ibatis.annotations.Param;

public interface PmsProductUserMatchLibraryMapper extends BaseMapper<PmsProductUserMatchLibrary> {
  Page<PmsProductUserMatchLibrary> oldListByPage(Page<PmsProductUserMatchLibrary> pmsProductUserMatchLibraryPage,PmsProductQueryParam queryParam);
  Page<PmsProductUserMatchLibrary> listByPage(Page<PmsProductUserMatchLibrary> pmsProductUserMatchLibraryPage,@Param("queryParam") PmsProductQueryParam queryParam);
}