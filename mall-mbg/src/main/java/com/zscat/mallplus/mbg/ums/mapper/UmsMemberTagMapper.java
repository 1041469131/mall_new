package com.zscat.mallplus.mbg.ums.mapper;

import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberTagVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户标签表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface UmsMemberTagMapper extends BaseMapper<UmsMemberTag> {

    List<UmsMemberTagVo> listUmsMemberTags(@Param("matchUserId") Long matchUserId);

}
