package com.zscat.mallplus.admin.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.pms.IPmsBrandService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.utils.ImportUtil;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.pms.entity.PmsBrand;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductVertifyRecord;
import com.zscat.mallplus.mbg.pms.vo.PmsProductParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品信息
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "PmsProductController", description = "商品信息管理")
@RequestMapping("/pms/PmsProduct")
public class PmsProductController {

    @Autowired
    private IPmsProductService iPmsProductService;

    @Autowired
    private ImportUtil importUtil;
    @Autowired
    private IPmsBrandService pmsBrandService;

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有商品信息列表")
    @ApiOperation("根据条件查询所有商品信息列表")
    @PostMapping(value = "/list")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public CommonResult<Page<PmsProduct>> getPmsProductByPage(@RequestBody @ApiParam("商品查询条件") PmsProductQueryParam queryParam) {
        try {
            queryParam.setDeleteStatus(0);
            Page<PmsProduct> pmsProductPage = iPmsProductService.listProductsByPage(queryParam);
            return new CommonResult().success(pmsProductPage);
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "保存商品信息")
    @ApiOperation("保存商品信息")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:PmsProduct:create')")
    public CommonResult<Long> savePmsProduct(@ApiParam("商品创建修改使用的参数") @RequestBody PmsProductParam productParam) {
        try {
            Long productId = iPmsProductService.create(productParam);
            if (productId > 0) {
                return new CommonResult().success(productId);
            } else {
                return new CommonResult().failed();
            }
        } catch (Exception e) {
            log.error("保存商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "pms", REMARK = "更新商品信息")
    @ApiOperation("更新商品信息")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public CommonResult updatePmsProduct(@ApiParam("商品id")@PathVariable Long id, @ApiParam("商品创建修改使用的参数") @RequestBody PmsProductParam productParam) {
        try {
            PmsProduct pmsProduct = iPmsProductService.getById(id);
            if(pmsProduct != null){
                if(!(pmsProduct.getPublishStatus().equals(MagicConstant.PUBLISH_STATUS_UP) || pmsProduct.getDeleteStatus()
                  .equals(MagicConstant.DELETE_YET) ||
                  pmsProduct.getVerifyStatus().equals(MagicConstant.VERIFY_STATUS_VERIFYED))){
                    int count = iPmsProductService.update(id, productParam);
                    if (count > 0) {
                        return new CommonResult().success(count);
                    } else {
                        return new CommonResult().failed();
                    }
                }else{
                    return new CommonResult().failed("该条记录可能已删除，已上架，已审核通过");
                }

            }
        } catch (Exception e) {
            log.error("更新商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();

    }

    @SysLog(MODULE = "pms", REMARK = "删除商品信息")
    @ApiOperation("删除商品信息")
    @RequestMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public CommonResult deletePmsProduct(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            if (StringUtils.isEmpty(iPmsProductService.deleteProduct(id))) {
                return new CommonResult().success();
            }else{
                return new CommonResult().failed(iPmsProductService.deleteProduct(id));
            }
        } catch (Exception e) {
            log.error("删除商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品信息明细")
    @ApiOperation("查询商品信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public CommonResult<PmsProduct> getPmsProductById(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("商品信息id");
            }
            PmsProduct coupon = iPmsProductService.getById(id);
            return new CommonResult<PmsProduct>().success(coupon);
        } catch (Exception e) {
            log.error("查询商品信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商品信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除商品信息")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public CommonResult<Boolean> deleteBatch(@ApiParam("商品ids")@RequestParam("ids") List<Long> ids) {
        boolean count = iPmsProductService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "根据商品id获取商品编辑信息")
  //  @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public CommonResult<PmsProductResult> getUpdateInfo(@ApiParam("商品id") @PathVariable Long id) {
        PmsProductResult productResult = iPmsProductService.getUpdateInfo(id);
        PmsBrand pmsBrand = pmsBrandService.getById(productResult.getBrandId());
        productResult.setPmsBrand(pmsBrand);
        return new CommonResult<PmsProductResult>().success(productResult);
    }

    @ApiOperation("根据商品id获取审核信息")
    @RequestMapping(value = "/fetchVList/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "据商品id获取审核信息")
    public CommonResult<List<PmsProductVertifyRecord>> fetchVList(@ApiParam("商品id") @PathVariable Long id) {
        List<PmsProductVertifyRecord> list = iPmsProductService.getProductVertifyRecord(id);
        return new CommonResult().success(list);
    }

    @ApiOperation("批量修改审核状态")
    @RequestMapping(value = "/update/verifyStatus")
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量修改审核状态")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public CommonResult<Integer> updateVerifyStatus(@ApiParam("商品ids")@RequestParam("ids") Long ids,
      @ApiParam("审核状态")@RequestParam("verifyStatus") Integer verifyStatus,
      @ApiParam("审核详情") @RequestParam("detail") String detail) {
        int count = iPmsProductService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量上下架")
    @RequestMapping(value = "/update/publishStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量上下架")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public CommonResult updatePublishStatus(@ApiParam("商品ids") @RequestParam("ids") List<Long> ids,
      @ApiParam("上架状态")@RequestParam("publishStatus") Integer publishStatus) {
        int count = iPmsProductService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量推荐商品")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量推荐商品")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public CommonResult updateRecommendStatus(@ApiParam("商品ids")@RequestParam("ids") List<Long> ids,
      @ApiParam("推荐状态")@RequestParam("recommendStatus") Integer recommendStatus) {
        int count = iPmsProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量设为新品")
    @RequestMapping(value = "/update/newStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量设为新品")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public CommonResult updateNewStatus(@ApiParam("商品ids")@RequestParam("ids") List<Long> ids,
      @ApiParam("新品状态") @RequestParam("newStatus") Integer newStatus) {
        int count = iPmsProductService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量修改删除状态")
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量修改删除状态")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public CommonResult updateDeleteStatus(@ApiParam("商品ids")@RequestParam("ids") List<Long> ids,
      @ApiParam("删除状态")@RequestParam("deleteStatus") Integer deleteStatus) {
        int count = iPmsProductService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("导入商品")
    @RequestMapping(value = "/importProduct", method = RequestMethod.POST)
    @ResponseBody
    @IgnoreAuth
    public CommonResult importProduct(String productStr) {
        importUtil.importProduct(productStr);
        return new CommonResult<>().success();
    }

    /**********************************************新增接口*********************************************************************/
    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有商品信息列表")
    @ApiOperation("根据条件查询所有商品信息列表")
    @PostMapping(value = "/listPmsProductByPage")
    @IgnoreAuth
    public CommonResult<Page<PmsProductVo>> listPmsProductByPage(@ApiParam("产品信息的扩展类") @RequestBody PmsProductQueryParam queryParam) {
        Page<PmsProductVo> pmsProductList = iPmsProductService.listPmsProductByPage(queryParam);

        return new CommonResult().success(pmsProductList);
    }


}
