package com.zscat.mallplus.admin.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.cms.entity.CmsTopic;

/**
 * <p>
 * 话题表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface ICmsTopicService extends IService<CmsTopic> {

    int updateVerifyStatus(Long ids, Long topicId,Integer verifyStatus);

    boolean saves(CmsTopic entity);
}
