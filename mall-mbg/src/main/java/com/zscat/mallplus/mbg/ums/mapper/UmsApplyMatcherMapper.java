package com.zscat.mallplus.mbg.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.vo.UmsApplyMatcherVo;
import org.apache.ibatis.annotations.Param;

public interface UmsApplyMatcherMapper extends BaseMapper<UmsApplyMatcher>{
    Page<UmsApplyMatcherVo> pageMatcher(Page<UmsApplyMatcherVo> page, @Param("umsApplyMatcherVo") UmsApplyMatcherVo umsApplyMatcherVo);
}