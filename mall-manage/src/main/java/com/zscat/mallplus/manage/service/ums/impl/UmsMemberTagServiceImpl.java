package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.ums.IUmsMemberTagService;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberTagMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberTagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户标签表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class UmsMemberTagServiceImpl extends ServiceImpl<UmsMemberTagMapper, UmsMemberTag> implements IUmsMemberTagService {

    @Autowired
    private UmsMemberTagMapper umsMemberTagMapper;

    @Override
    public List<UmsMemberTagVo> listUmsMemberTags(Long matchUserId) {
        return umsMemberTagMapper.listUmsMemberTags(matchUserId);
    }

    @Override
    public List<UmsMemberTagVo> listTagsCountByMatchUserId(Long matchUserId) {
        return umsMemberTagMapper.listTagsCountByMatchUserId(matchUserId);
    }

    @Override
    public List<UmsMemberTag> listTagsByMemberId(Long memberId) {
        return umsMemberTagMapper.listTagsByMemberId(memberId);
    }
}
