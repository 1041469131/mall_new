package com.zscat.mallplus.manage.service.cms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.cms.ICmsHelpService;
import com.zscat.mallplus.mbg.cms.entity.CmsHelp;
import com.zscat.mallplus.mbg.cms.entity.CmsHelpCategory;
import com.zscat.mallplus.mbg.cms.mapper.CmsHelpCategoryMapper;
import com.zscat.mallplus.mbg.cms.mapper.CmsHelpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 帮助表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class CmsHelpServiceImpl extends ServiceImpl<CmsHelpMapper, CmsHelp> implements ICmsHelpService {

    @Autowired
    private CmsHelpMapper helpMapper;
    @Autowired
    private CmsHelpCategoryMapper helpCategoryMapper;

    @Override
    @Transactional
    public boolean saves(CmsHelp entity) {
        entity.setCreateTime(new Date());
        helpMapper.insert(entity);
        CmsHelpCategory category = helpCategoryMapper.selectById(entity.getCategoryId());
        category.setHelpCount(category.getHelpCount() + 1);
        helpCategoryMapper.updateById(category);
        return true;
    }
}
