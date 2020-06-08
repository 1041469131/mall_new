package com.zscat.mallplus.mbg.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import org.apache.ibatis.annotations.Param;

public interface PmsProductMatchLibraryMapper extends BaseMapper<PmsProductMatchLibrary> {

  Page<PmsProductMatchLibrary> listByPage(Page<PmsProductMatchLibrary> pmsProductMatchLibraryPage ,@Param("queryParam") PmsProductQueryParam queryParam);
}