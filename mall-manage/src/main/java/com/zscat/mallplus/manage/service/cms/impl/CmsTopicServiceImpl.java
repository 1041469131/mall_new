package com.zscat.mallplus.manage.service.cms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.cms.ICmsTopicService;
import com.zscat.mallplus.mbg.cms.entity.CmsTopic;
import com.zscat.mallplus.mbg.cms.entity.CmsTopicCategory;
import com.zscat.mallplus.mbg.cms.entity.CmsTopicMember;
import com.zscat.mallplus.mbg.cms.mapper.CmsTopicCategoryMapper;
import com.zscat.mallplus.mbg.cms.mapper.CmsTopicMapper;
import com.zscat.mallplus.mbg.cms.mapper.CmsTopicMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 话题表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class CmsTopicServiceImpl extends ServiceImpl<CmsTopicMapper, CmsTopic> implements ICmsTopicService {

    @Autowired
    private CmsTopicMapper topicMapper;
    @Autowired
    private CmsTopicMemberMapper topicMemberMapper;

    @Autowired
    private CmsTopicCategoryMapper topicCategoryMapper;

    @Override
    @Transactional
    public boolean saves(CmsTopic entity) {
        entity.setCreateTime(new Date());
        topicMapper.insert(entity);
        CmsTopicCategory category = topicCategoryMapper.selectById(entity.getCategoryId());
        category.setSubjectCount(category.getSubjectCount() + 1);
        topicCategoryMapper.updateById(category);
        return true;
    }
    @Transactional
    @Override
    public int updateVerifyStatus(Long ids,Long topicId, Integer verifyStatus) {
        CmsTopicMember product = new CmsTopicMember();
        product.setStatus(verifyStatus+"");
        product.setId(ids);
        int count = topicMemberMapper.updateById(product);

        CmsTopic topic = topicMapper.selectById(topicId);
        if (verifyStatus==1){
            topic.setAttendCount(topic.getAttendCount()+1);
        }else{
            topic.setAttendCount(topic.getAttendCount()-1);
        }
        topicMapper.updateById(topic);
        return count;
    }
}
