package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.ums.IUmsMemberMemberTagRelationService;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberMemberTagRelation;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMemberTagRelationMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberMemberTagRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户和标签关系表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class UmsMemberMemberTagRelationServiceImpl extends ServiceImpl<UmsMemberMemberTagRelationMapper, UmsMemberMemberTagRelation> implements IUmsMemberMemberTagRelationService {

    @Autowired
    private UmsMemberMemberTagRelationMapper umsMemberMemberTagRelationMapper;

    @Override
    @Transactional
    public boolean saveTagRelation(UmsMemberMemberTagRelationVo entity) {
        String tagIds = entity.getTagIds();
        umsMemberMemberTagRelationMapper.delete(new QueryWrapper<UmsMemberMemberTagRelation>().eq("member_id", entity.getMemberId()));
        if(!StringUtils.isEmpty(tagIds)){
            List<UmsMemberMemberTagRelation> umsMemberMemberTagRelations = new ArrayList<>();
            String[] tagIdArr = tagIds.split(",");
            for(String tagId:tagIdArr){
                UmsMemberMemberTagRelation umsMemberMemberTagRelation = new UmsMemberMemberTagRelation();
                umsMemberMemberTagRelation.setMemberId(entity.getMemberId());
                umsMemberMemberTagRelation.setTagId(Long.valueOf(tagId));
                umsMemberMemberTagRelation.setCreateTime(new Date());
                umsMemberMemberTagRelations.add(umsMemberMemberTagRelation);
            }
            if(!CollectionUtils.isEmpty(umsMemberMemberTagRelations)){
                this.saveBatch(umsMemberMemberTagRelations);
            }
        }
        return true;
    }
}
