package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberVo;
import com.zscat.mallplus.mbg.ums.vo.VUmsMemberVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "UmsMemberController", description = "会员表管理")
@RequestMapping("/ums/UmsMember")
public class UmsMemberController {

    @Resource
    private IUmsMemberService IUmsMemberService;

    @Autowired
    private IUmsMemberRegisterParamService iUmsMemberRegisterParamService;

    @SysLog(MODULE = "ums", REMARK = "根据条件查询所有会员表列表")
    @ApiOperation("根据条件查询所有会员表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public Object getUmsMemberByPage(String keyword,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            if(!StringUtils.isEmpty(keyword)){
                return new CommonResult().success(IUmsMemberService.page(new Page<UmsMember>(pageNum, pageSize), new QueryWrapper<UmsMember>().like("nickname",keyword)));
            }else{
                return new CommonResult().success(IUmsMemberService.page(new Page<UmsMember>(pageNum, pageSize), new QueryWrapper<UmsMember>()));
            }

        } catch (Exception e) {
            log.error("根据条件查询所有会员表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "ums", REMARK = "分页查询会员表列表")
    @ApiOperation("分页查询会员表列表")
    @GetMapping(value = "/pageUmsMembers")
    @IgnoreAuth
    public Object pageUmsMembers(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        Page<UmsMemberVo> umsMemberPage = new Page<>(pageNum,pageSize);
        List<Long> productAttributeIds = new ArrayList<>();
        Map<String,Object> paramMap = new HashMap<>();
        Page<UmsMemberVo> pmsProductList = IUmsMemberService.pageUmsMembers(umsMemberPage,paramMap);
        return pmsProductList;
    }

    @SysLog(MODULE = "ums", REMARK = "保存会员表")
    @ApiOperation("保存会员表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('ums:UmsMember:create')")
    public Object saveUmsMember(@RequestBody UmsMember entity) {
        try {
            if (IUmsMemberService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "更新会员表")
    @ApiOperation("更新会员表")
    @PostMapping(value = "/update")
//    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @IgnoreAuth
    public CommonResult updateUmsMember(@RequestBody UmsMember entity) {
        try {
            if (IUmsMemberService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

//    @IgnoreAuth
    @SysLog(MODULE = "ums", REMARK = "删除会员表")
    @ApiOperation("删除会员表")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ums:UmsMember:delete')")
    public Object deleteUmsMember(@ApiParam("会员表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员表id");
            }
            if (IUmsMemberService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "给会员表分配会员表")
    @ApiOperation("查询会员表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public Object getUmsMemberById(@ApiParam("会员表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员表id");
            }
            UmsMember coupon = IUmsMemberService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询会员表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除会员表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除会员表")
    @PreAuthorize("hasAuthority('ums:UmsMember:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IUmsMemberService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    /******************************************新增接口*****************************************/
    @ApiOperation(value = "根据用户昵称查询用户详情")
    @RequestMapping(value = "/query/detail", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "ums", REMARK = "根据用户昵称查询用户详情")
//    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public CommonResult<List<UmsMemberVo>> queryUserByNickName(@ApiParam("用户昵称") String nickName) {
        List<UmsMember> umsMembers = IUmsMemberService.list(new QueryWrapper<UmsMember>().like("nickname", nickName));
        List<UmsMemberVo> umsMemberVos = null;
        if(!CollectionUtils.isEmpty(umsMembers)){
            umsMemberVos = new ArrayList<>();
            dealUmsMembers(umsMembers,umsMemberVos);
        }
        return new CommonResult<>().success(umsMemberVos);
    }

    /**
     * 处理用户信息
     * @param umsMembers
     */
    private void dealUmsMembers(List<UmsMember> umsMembers,List<UmsMemberVo> umsMemberVos) {
        for(UmsMember umsMember:umsMembers){
            UmsMemberVo umsMemberVo = new UmsMemberVo();
            BeanUtils.copyProperties(umsMember, umsMemberVo);
            String dressTypeId = umsMember.getDressStyle();//穿衣风格
            umsMemberVo.setDressStyleName(dealUserTag(dressTypeId));
            String dressColorId = umsMember.getDressColor();//穿衣的色系
            umsMemberVo.setDressColorName(dealUserTag(dressColorId));
            String neverDressStyle = umsMember.getNeverDressStyle();//重来不穿的风格
            umsMemberVo.setNeverDressStyleName(dealUserTag(neverDressStyle));
            String neverDressIcon = umsMember.getNeverDressIcon();//永远不穿的图案
            umsMemberVo.setNeverDressIconName(dealUserTag(neverDressIcon));
            String suiteLining= umsMember.getSuiteLining();//合适的面料
            umsMemberVo.setSuiteLiningName(dealUserTag(suiteLining));
            String enjoyModel = umsMember.getEnjoyModel();//喜欢的版型
            umsMemberVo.setEnjoyModelName(dealUserTag(enjoyModel));
//            String itemBudget = umsMember.getItemBudget();//单品的预算
//            umsMemberVo.setItemBudgetName(dealUserTag(itemBudget));
            String balanceBody = umsMember.getBalanceBody();//平衡身体
            umsMemberVo.setBalanceBodyName(dealUserTag(balanceBody));
            String careClothes = umsMember.getCareClothes();//更在意衣服
            umsMemberVo.setCareClothesName(dealUserTag(careClothes));
            String dressFreq = umsMember.getDressFreq();//衣服频率
            umsMemberVo.setDressFreqName(dealUserTag(dressFreq));
            String matchCount = umsMember.getMatchCount();//搭配的次数
            umsMemberVo.setMatchCountName(dealUserTag(matchCount));
            String aspect = umsMember.getAspect();//体貌特征
            umsMemberVo.setAspectName(dealUserTag(aspect));
            umsMemberVos.add(umsMemberVo);
        }
    }

    /**
     * 处理用户的标签
     * @param tagId
     */
    private String dealUserTag(String tagId) {
        if(StringUtils.isEmpty(tagId)){
            return null;
        }
        String[] tagIds = tagId.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for(String userTagId:tagIds){
            UmsMemberRegisterParam umsMemberRegisterParam = iUmsMemberRegisterParamService.getById(Long.valueOf(userTagId));
            if(umsMemberRegisterParam != null){
                stringBuffer.append(umsMemberRegisterParam.getName()+",");
            }
        }
        if(!StringUtils.isEmpty(stringBuffer.toString())){
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    @ApiOperation(value = "根据搭配师id查询搭配师下面的粉丝(不需要传搭配师id后台通过登录自己获取)")
    @RequestMapping(value = "/listUmsMembers", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "ums", REMARK = "根据搭配师id查询搭配师下面的粉丝")
//    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public CommonResult<List<VUmsMemberVo>> listUmsMember4Matcher(@RequestBody VUmsMemberVo vUmsMemberVo){
        //当为搭配师平台的时候，将登陆用户的id赋值给搭配师
        if(MagicConstant.SYSTEM_TYPE_MATCH.equals(vUmsMemberVo.getSystemType())){
            vUmsMemberVo.setMatchUserId(UserUtils.getCurrentMember().getId());
        }
        Page<VUmsMemberVo> umsMembers = IUmsMemberService.listVUmsMembers(vUmsMemberVo);
        return new CommonResult<>().success(umsMembers);
    }

}
