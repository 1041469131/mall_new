package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.ums.IUmsApplyMatcherService;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.mapper.UmsApplyMatcherMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsApplyMatcherVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员申请搭配师的服务实现类
 */
@Service
public class UmsApplyMatcherServiceImpl extends ServiceImpl<UmsApplyMatcherMapper,UmsApplyMatcher> implements IUmsApplyMatcherService{

    @Autowired
    private UmsApplyMatcherMapper umsApplyMatcherMapper;

    @Override
    public Page<UmsApplyMatcherVo> pageMatcher(UmsApplyMatcherVo umsApplyMatcherVo) {
        Page<UmsApplyMatcherVo> page = new Page<>(umsApplyMatcherVo.getPageNum(),umsApplyMatcherVo.getPageSize());
        return umsApplyMatcherMapper.pageMatcher(page,umsApplyMatcherVo);
    }
}
