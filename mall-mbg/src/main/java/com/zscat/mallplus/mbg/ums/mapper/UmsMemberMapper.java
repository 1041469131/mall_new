package com.zscat.mallplus.mbg.ums.mapper;

import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface UmsMemberMapper extends BaseMapper<UmsMember> {

    UmsMember getRandomUmsMember();

}
