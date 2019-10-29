package com.zscat.mallplus.manage.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.cms.entity.CmsSubject;
import com.zscat.mallplus.mbg.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.mbg.oms.vo.HomeContentResult;
import com.zscat.mallplus.mbg.pms.entity.PmsBrand;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;

import java.util.List;

/**
 * <p>
 * 首页轮播广告表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface ISmsHomeAdvertiseService extends IService<SmsHomeAdvertise> {

    /**
     * 更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 修改推荐排序
     */
    int updateSort(Long id, Integer sort);

    HomeContentResult singelContent();

    List<PmsBrand> getRecommendBrandList(int pageNum, int pageSize) ;
    List<PmsProduct> getNewProductList(int pageNum, int pageSize) ;
    List<PmsProduct> getHotProductList(int pageNum, int pageSize) ;
    List<CmsSubject> getRecommendSubjectList(int pageNum, int pageSize) ;

    List<SmsHomeAdvertise> getHomeAdvertiseList() ;
}
