package com.zscat.mallplus.manage.service.cms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.cms.entity.CmsSubject;

/**
 * <p>
 * 专题表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface ICmsSubjectService extends IService<CmsSubject> {

    int updateRecommendStatus(Long ids, Integer recommendStatus);

    int updateShowStatus(Long ids, Integer showStatus);

    boolean saves(CmsSubject entity);
}
