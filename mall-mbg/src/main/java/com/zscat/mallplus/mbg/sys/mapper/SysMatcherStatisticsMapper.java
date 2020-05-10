package com.zscat.mallplus.mbg.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherStatistics;
import com.zscat.mallplus.mbg.sys.vo.SysMatcherStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SysMatcherStatisticsMapper extends BaseMapper<SysMatcherStatistics>{
    Page<SysMatcherStatisticsVo> pageMatherStatistics(Page<Object> objectPage, @Param("matcherStatics") SysMatcherStatisticsVo sysMatcherStatisticsVo);

    HashMap<String,Object> getAmount(Map<String,Object> paramMap);

    int getFanCount(@Param("matcherUserId")Long id);

    int getInviteCount(@Param("invitePhone")String phone);

    SysMatcherStatisticsVo querySysMatcherStatistics(@Param("matcherUserId")Long matcherUserId);
}