package com.zscat.mallplus.manage.service.ums;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.vo.UmsApplyMatcherVo;
import com.zscat.mallplus.mbg.ums.vo.VUmsMemberVo;

/**
 * 会员申请搭配师服务接口
 */
public interface IUmsApplyMatcherService extends IService<UmsApplyMatcher> {

    Page<UmsApplyMatcherVo> pageMatcher(UmsApplyMatcherVo umsApplyMatcherVo);
}
