package com.zscat.mallplus.manage.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.cms.ICmsSubjectCategoryService;
import com.zscat.mallplus.manage.service.cms.ICmsSubjectCommentService;
import com.zscat.mallplus.manage.service.cms.ICmsSubjectService;
import com.zscat.mallplus.manage.service.marking.*;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.pms.IPmsBrandService;
import com.zscat.mallplus.manage.service.pms.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.manage.service.pms.IPmsProductCategoryService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberLevelService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.mbg.cms.entity.CmsSubject;
import com.zscat.mallplus.mbg.marking.entity.*;
import com.zscat.mallplus.mbg.marking.mapper.SmsHomeAdvertiseMapper;
import com.zscat.mallplus.mbg.marking.mapper.SmsHomeNewProductMapper;
import com.zscat.mallplus.mbg.oms.vo.HomeContentResult;
import com.zscat.mallplus.mbg.pms.entity.PmsBrand;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttributeCategory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 首页轮播广告表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class SmsHomeAdvertiseServiceImpl extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> implements ISmsHomeAdvertiseService {

    @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Autowired
    private IOmsOrderService orderService;
    @Resource
    private ISmsGroupService groupService;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private IPmsProductService pmsProductService;
    @Resource
    private IPmsProductAttributeCategoryService productAttributeCategoryService;
    @Resource
    private IPmsProductCategoryService productCategoryService;

    @Resource
    private ISmsHomeBrandService homeBrandService;
    @Resource
    private ISmsHomeNewProductService homeNewProductService;
    @Resource
    private ISmsHomeRecommendProductService homeRecommendProductService;
    @Resource
    private ISmsHomeRecommendSubjectService homeRecommendSubjectService;

    @Resource
    private ICmsSubjectCategoryService subjectCategoryService;
    @Resource
    private ICmsSubjectService subjectService;
    @Resource
    private ICmsSubjectCommentService commentService;
    @Resource
    private IPmsBrandService brandService;
    @Resource
    private SmsHomeNewProductMapper homeNewProductMapper;
    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        return homeNewProductMapper.updateById(homeNewProduct);
    }
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeNewProduct record = new SmsHomeNewProduct();
        record.setRecommendStatus(recommendStatus);
        return homeNewProductMapper.update(record, new QueryWrapper<SmsHomeNewProduct>().in("id",ids));
    }

    @Override
    public HomeContentResult singelContent() {
        HomeContentResult result = new HomeContentResult();
        //获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取推荐品牌
        result.setBrandList(this.getRecommendBrandList(0, 4));

        //获取新品推荐
        result.setNewProductList(this.getNewProductList(0, 4));
        //获取人气推荐
        result.setHotProductList(this.getHotProductList(0, 4));
        //获取推荐专题
        result.setSubjectList(this.getRecommendSubjectList(0, 4));
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.list(new QueryWrapper<>());

        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());
            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
            List<PmsProduct> goodsList = pmsProductService.list(new QueryWrapper<>(productQueryParam));
            if (goodsList!=null && goodsList.size()>0){
                PmsProduct pmsProduct = goodsList.get(0);
                PmsProduct product =  new PmsProduct();
                BeanUtils.copyProperties(pmsProduct, product);
                //  product.setType(1);
                goodsList.add(product);
            }
            gt.setGoodsList(goodsList);
        }
        result.setCat_list(productAttributeCategoryList);
        return result;
    }
    @Override
    public List<PmsBrand> getRecommendBrandList(int pageNum, int pageSize) {
        List<SmsHomeBrand> brands = homeBrandService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<PmsBrand>) brandService.listByIds(brands);

    }
    @Override
    public List<PmsProduct> getNewProductList(int pageNum, int pageSize) {
        List<SmsHomeNewProduct> brands = homeNewProductService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<PmsProduct>) pmsProductService.listByIds(brands);
    }
    @Override
    public List<PmsProduct> getHotProductList(int pageNum, int pageSize) {
        List<SmsHomeRecommendProduct> brands = homeRecommendProductService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<PmsProduct>) pmsProductService.listByIds(brands);
    }
    @Override
    public List<CmsSubject> getRecommendSubjectList(int pageNum, int pageSize) {
        List<SmsHomeRecommendSubject> brands = homeRecommendSubjectService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<CmsSubject>) subjectService.listByIds(brands);
    }
    @Override
    public List<SmsHomeAdvertise> getHomeAdvertiseList() {
        SmsHomeAdvertise advertise = new SmsHomeAdvertise();
        advertise.setStatus(1);
        return  advertiseService.list(new QueryWrapper<>(advertise));
    }
}
