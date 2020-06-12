package com.zscat.mallplus.mbg.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberVo;
import com.zscat.mallplus.mbg.ums.vo.VUmsMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<UmsMember> getRecommedInfos(@Param("recommendedId") Long recommendedId);

    Page<UmsMemberVo> pageUmsMembers(Page<UmsMemberVo> umsMemberPage, Map<String, Object> paramMap);

    Page<VUmsMemberVo> pageVUmsMembers(Page<VUmsMemberVo> pmsProductPage, @Param("paramMap") VUmsMemberVo vUmsMemberVo);
}
