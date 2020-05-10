package com.zscat.mallplus.manage.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherStatistics;
import com.zscat.mallplus.mbg.sys.vo.SysMatcherStatisticsVo;

import java.util.List;

public interface ISysMatcherStatisticsService extends IService<SysMatcherStatistics>{

    Page<SysMatcherStatisticsVo> pageMatherStatistics(SysMatcherStatisticsVo sysMatcherStatisticsVo);

    void refreshMatcherStatisticsByOrder(OmsOrder omsOrder);

    void timingMatcherStatics();

    void updateMatcherStaticsByMatcherId(Long matcherId);

    SysMatcherStatisticsVo querySysMatcherStatistics(Long matcherUserId);
}
