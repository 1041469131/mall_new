package com.zscat.mallplus.manage.service.pms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsBrand;

import java.util.List;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IPmsBrandService extends IService<PmsBrand> {

    int updateShowStatus(List<Long> ids, Integer showStatus);

    int updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
