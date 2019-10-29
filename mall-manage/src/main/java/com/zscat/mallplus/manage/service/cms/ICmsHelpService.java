package com.zscat.mallplus.manage.service.cms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.cms.entity.CmsHelp;

/**
 * <p>
 * 帮助表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface ICmsHelpService extends IService<CmsHelp> {

    boolean saves(CmsHelp entity);
}
