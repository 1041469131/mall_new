package com.zscat.mallplus.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.cms.ICmsSubjectService;
import com.zscat.mallplus.manage.service.marking.ISmsHomeAdvertiseService;
import com.zscat.mallplus.manage.service.pms.*;
import com.zscat.mallplus.manage.service.ums.IUmsCollectService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.pms.entity.*;
import com.zscat.mallplus.mbg.pms.vo.*;
import com.zscat.mallplus.mbg.ums.entity.UmsCollect;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.constant.RedisKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 首页内容管理Controller
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
@RestController
@Api(tags = "GoodsController", description = "商品相关管理")
@RequestMapping("/api/pms")
public class PmsGoodsController {

    @Autowired
    private IPmsProductAttributeCategoryService productAttributeCategoryService;

    @Autowired
    private ISmsHomeAdvertiseService advertiseService;

    @Autowired
    private IPmsProductService pmsProductService;

    @Autowired
    private IPmsProductAttributeService productAttributeService;

    @Autowired
    private IPmsProductCategoryService productCategoryService;

    @Autowired
    private ICmsSubjectService subjectService;

    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private IPmsProductConsultService pmsProductConsultService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IPmsProductMatchLibraryService iPmsProductMatchLibraryService;

    @Autowired
    private IPmsProductUserMatchLibraryService iPmsProductUserMatchLibraryService;

    @Autowired
    private IUmsCollectService iUmsCollectService;

    @Autowired
    private IPmsSkuStockService iPmsSkuStockService;

    @Autowired
    private IPmsProductService iPmsProductService;

    @IgnoreAuth
    @ApiOperation("查询商品列表1")
    @GetMapping(value = "/product/queryProductList1")
    public Object queryProductList1(PmsProduct productQueryParam) {
        productQueryParam.setPublishStatus(MagicConstant.PUBLISH_STATUS_UP);
        productQueryParam.setVerifyStatus(MagicConstant.VERIFY_STATUS_VERIFYED);
        return new CommonResult().success(pmsProductService.list(new QueryWrapper<>(productQueryParam)));
    }

    /**
     * 分类和分类下的商品(项目上线这个方法可以删除掉)
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation("分类和分类下的商品")
    @GetMapping("/getProductCategoryDto")
    public Object getProductCategoryDtoByPid() {
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.list(null);
        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());

            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
            productQueryParam.setDeleteStatus(0);
            gt.setGoodsList(pmsProductService.list(new QueryWrapper<>(productQueryParam)));
        }
        return new CommonResult().success(productAttributeCategoryList);
    }

    /**
     * 查询所有一级分类及子分类（项目上线方法可以删除掉）
     *
     * @return
     */
    @IgnoreAuth
    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping("/listWithChildren")
    public Object listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return new CommonResult().success(list);
    }


    @IgnoreAuth
    @GetMapping(value = "/product/queryProductDetail")
    @ApiOperation("查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        PmsProductResult productResult = pmsProductService.getUpdateInfo(id);
        UmsMember umsMember = memberService.getCurrentMember();
        if (productResult != null) {
            UmsCollect umsCollect = iUmsCollectService.getOne(new QueryWrapper<UmsCollect>().eq("member_id", umsMember.getId()).eq("assembly_id",productResult.getId() ));
            if(umsCollect != null){
                productResult.setIs_favorite(Integer.valueOf(umsCollect.getFavorType()));
            }
        }
        return new CommonResult().success(productResult);
    }


    @IgnoreAuth
    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping(value = "/attr/list")
    //项目上线改方法可以删除掉
    public Object getList(@RequestParam(value = "cid", required = false, defaultValue = "0") Long cid,
                          @RequestParam(value = "type") Integer type,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        PmsProductAttribute q = new PmsProductAttribute();
        q.setType(type);q.setProductAttributeCategoryId(cid);
        IPage<PmsProductAttribute> productAttributeList = productAttributeService.page(new Page<>(pageSize,pageNum),new QueryWrapper<>(q));
        return new CommonResult().success(productAttributeList);
    }

    @IgnoreAuth
    @ApiOperation("获取某个商品的评价")
    @RequestMapping(value = "/consult/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "goodsId", required = false, defaultValue = "0") Long goodsId,
                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        PmsProductConsult productConsult = new PmsProductConsult();
        productConsult.setGoodsId(goodsId);
        List<PmsProductConsult> list = null;
        String consultJson = redisService.get(RedisKey.PmsProductConsult + goodsId);
        if (consultJson != null) {
            list = JsonUtil.jsonToList(consultJson, PmsProductConsult.class);
        } else {
            list = pmsProductConsultService.list(new QueryWrapper<>(productConsult));
            redisService.set(RedisKey.PmsProductConsult + goodsId, JsonUtil.objectToJson(list));
            redisService.expire(RedisKey.PmsProductConsult + goodsId, 24 * 60 * 60);
        }

        int goods = 0;
        int general = 0;
        int bad = 0;
        ConsultTypeCount count = new ConsultTypeCount();
        for (PmsProductConsult consult : list) {
            if (consult.getStoreId() != null) {
                if (consult.getStoreId() == 1) {
                    goods++;
                }
                if (consult.getStoreId() == 2) {
                    general++;
                }
                if (consult.getStoreId() == 3) {
                    bad++;
                }
            }
        }
        count.setAll(goods + general + bad);
        count.setBad(bad);
        count.setGeneral(general);
        count.setGoods(goods);
        List<PmsProductConsult> productConsults = pmsProductConsultService.list(new QueryWrapper<>(productConsult));
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("list", productConsults);
        objectMap.put("count", count);
        return new CommonResult().success(objectMap);
    }

    /*************新增接口和搭配相关的接口****************/

    @IgnoreAuth
    @ApiOperation("获取该用户的推荐商品")
    @RequestMapping(value = "/listRecommedProducts", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductMatchLibraryVo>> listRecommedProducts(@ApiParam("时长格式 就是数字类型 比如15")String periodDay) {
        Long userId = UserUtils.getCurrentUmsMember().getId();
        List<PmsProductMatchLibraryVo> pmsProductMatchLibraryVos = null;

        List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries = null;
        if(StringUtils.isEmpty(periodDay)){
            pmsProductUserMatchLibraries = iPmsProductUserMatchLibraryService.list(new QueryWrapper<PmsProductUserMatchLibrary>().
                    eq("user_id",userId).eq("recommend_type",MagicConstant.RECOMMEND_TYPE_YES).orderByDesc("create_time"));
        }else{
            Date periodDate = DateUtils.subtractDate(new Date(), Long.valueOf(periodDay));
            pmsProductUserMatchLibraries = iPmsProductUserMatchLibraryService.list(new QueryWrapper<PmsProductUserMatchLibrary>().
                    eq("user_id",userId).eq("recommend_type",MagicConstant.RECOMMEND_TYPE_YES).between("create_time", new Date(), periodDate).
                    orderByDesc("create_time"));
        }

        if(!CollectionUtils.isEmpty(pmsProductUserMatchLibraries)){
            pmsProductMatchLibraryVos = new ArrayList<>();
            for(PmsProductUserMatchLibrary pmsProductUserMatchLibrary : pmsProductUserMatchLibraries){
                PmsProductMatchLibraryVo pmsProductMatchLibraryVo = new PmsProductMatchLibraryVo();
                String skuIds = pmsProductUserMatchLibrary.getSkuIds();
                if(!StringUtils.isEmpty(skuIds)){
                    String[] skuIdArrays = skuIds.split(",");
                    List<Long> skuIdList = new ArrayList<>();;
                    for (String skuId:skuIdArrays){
                        skuIdList.add(Long.valueOf(skuId));
                    }
                    List<PmsSkuStock> pmsSkuStocks = (List<PmsSkuStock>)iPmsSkuStockService.listByIds(skuIdList);
                    List<PmsSkuStockVo> pmsSkuStockVos = null;
                    if(!CollectionUtils.isEmpty(pmsSkuStocks)){
                        pmsSkuStockVos = new ArrayList<>();
                        for(PmsSkuStock pmsSkuStock:pmsSkuStocks){
                            PmsSkuStockVo pmsSkuStockVo = new PmsSkuStockVo();
                            BeanUtils.copyProperties(pmsSkuStock, pmsSkuStockVo);
                            Long productId = pmsSkuStock.getProductId();
                            PmsProduct pmsProduct = iPmsProductService.getById(productId);
                            pmsSkuStockVo.setPmsProduct(pmsProduct);
                            pmsSkuStockVos.add(pmsSkuStockVo);
                        }
                    }
                    pmsProductMatchLibraryVo.setPmsSkuStockVos(pmsSkuStockVos);
                    pmsProductMatchLibraryVo.setPmsProductUserMatchLibrary(pmsProductUserMatchLibrary);
                    pmsProductMatchLibraryVos.add(pmsProductMatchLibraryVo);
                }
            }
        }
        return new CommonResult().success(pmsProductMatchLibraryVos);
    }


    @IgnoreAuth
    @ApiOperation("根据id获取用户搭配详情")
    @RequestMapping(value = "/queryUserMatchLibrary", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProductMatchLibraryVo> queryUserMatchLibrary(@ApiParam("搭配id") String userMatchId) {
        Long userId = UserUtils.getCurrentUmsMember().getId();
        PmsProductMatchLibraryVo pmsProductMatchLibraryVo = new PmsProductMatchLibraryVo();

        PmsProductUserMatchLibrary  pmsProductUserMatchLibrary = iPmsProductUserMatchLibraryService.getById(Long.valueOf(userMatchId));
        String skuIds = pmsProductUserMatchLibrary.getSkuIds();
            if(!StringUtils.isEmpty(skuIds)){
                    String[] skuIdArrays = skuIds.split(",");
                    List<Long> skuIdList = new ArrayList<>();;
                    for (String skuId:skuIdArrays){
                        skuIdList.add(Long.valueOf(skuId));
                    }
                    List<PmsSkuStock> pmsSkuStocks = (List<PmsSkuStock>)iPmsSkuStockService.listByIds(skuIdList);
                    List<PmsSkuStockVo> pmsSkuStockVos = null;
                    if(!CollectionUtils.isEmpty(pmsSkuStocks)){
                        pmsSkuStockVos = new ArrayList<>();
                        for(PmsSkuStock pmsSkuStock:pmsSkuStocks){
                            PmsSkuStockVo pmsSkuStockVo = new PmsSkuStockVo();
                            BeanUtils.copyProperties(pmsSkuStock, pmsSkuStockVo);
                            Long productId = pmsSkuStock.getProductId();
                            PmsProduct pmsProduct = iPmsProductService.getById(productId);
                            pmsSkuStockVo.setPmsProduct(pmsProduct);
                            pmsSkuStockVos.add(pmsSkuStockVo);
                        }
                    }
                pmsProductMatchLibraryVo.setPmsSkuStockVos(pmsSkuStockVos);
                pmsProductMatchLibraryVo.setPmsProductUserMatchLibrary(pmsProductUserMatchLibrary);
            }
        return new CommonResult().success(pmsProductMatchLibraryVo);
    }
}
