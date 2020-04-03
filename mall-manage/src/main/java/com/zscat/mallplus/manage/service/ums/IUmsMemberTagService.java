package com.zscat.mallplus.manage.service.ums;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberTagVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户标签表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IUmsMemberTagService extends IService<UmsMemberTag> {

    List<UmsMemberTagVo> listUmsMemberTags(@Param("matchUserId") Long matchUserId);

}
