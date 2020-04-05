package com.zscat.mallplus.manage.service.ums;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberMemberTagRelation;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberMemberTagRelationVo;

/**
 * <p>
 * 用户和标签关系表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IUmsMemberMemberTagRelationService extends IService<UmsMemberMemberTagRelation> {

    /**
     * 保存用户关联关系表
     * @param entity
     * @return
     */
    boolean saveTagRelation(UmsMemberMemberTagRelationVo entity);
}
