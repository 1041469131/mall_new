package com.zscat.mallplus.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.marking.ISmsCouponService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.CommonUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.marking.entity.UserFormId;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.ums.entity.UmsRecommendRelation;
import com.zscat.mallplus.mbg.ums.mapper.UmsRecommendRelationMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import com.zscat.mallplus.portal.util.WechatDecryptDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员登录注册管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/3.
 */
@Controller
@Api(tags = "UmsMemberController", description = "会员管理")
@RequestMapping("/api/member")
public class UmsMemberController extends ApiBaseAction {

    @Autowired
    private IUmsMemberService memberService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IUmsMemberRegisterParamService iUmsMemberRegisterParamService;

    @IgnoreAuth
    @ApiOperation("登录以后返回token")
    @GetMapping(value = "/login")
    @ResponseBody
    public Object login(UmsMember umsMember) {
        if (umsMember==null){
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        try {
            Map<String, Object> token = memberService.login(umsMember.getUsername(), umsMember.getPassword());
            if (token.get("token") == null) {
                return new CommonResult().validateFailed("用户名或密码错误");
            }
            return new CommonResult().success(token);
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }

    }

    @IgnoreAuth
    @ApiOperation("注册使用与app的")
    @RequestMapping(value = "/reg")
    @ResponseBody
    public Object register(UmsMember umsMember) {
        if (umsMember==null){
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        return memberService.register(umsMember);
    }

    @IgnoreAuth
    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public Object getAuthCode(@RequestParam String telephone) {
        return memberService.generateAuthCode(telephone);
    }

    @ApiOperation("根据手机号和验证码进行登录")
    @RequestMapping(value = "/loginByAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public Object loginByAuthCode(@RequestParam String telephone,@RequestParam String authCode) {
        return memberService.loginByAuthCode(telephone,authCode);
    }




    @ApiOperation("修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePassword(@RequestParam String telephone,
                                 @RequestParam String password,
                                 @RequestParam String authCode) {
        return memberService.updatePassword(telephone, password, authCode);
    }

    @IgnoreAuth
    @GetMapping("/user")
    @ApiOperation("用户")
    @ResponseBody
    public Object user() {
        UmsMember umsMember = memberService.getCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            return new CommonResult().success(umsMember);
        }
        return new CommonResult().failed();

    }

    @ApiOperation("刷新token")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    @ResponseBody
    public Object refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = memberService.refreshToken(token);
        if (refreshToken == null) {
            return new CommonResult().failed();
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }



    @ApiOperation("登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        return new CommonResult().success(null);
    }


    /**
     * 提交小程序推送formid
     * @param request
     * @param response
     * @param formId 小程序推送formId
     * @param source @see com.fittime.health.market.model.PushUserFormIdRecord.source
     * @return
     */
    @RequestMapping(value = "submitFormId")
    @ApiOperation("提交小程序推送formid")
    @ResponseBody
    public Object submitFormId(HttpServletRequest request, HttpServletResponse response,  String formId, Integer source) {

        UserFormId entity = new UserFormId();

        if (ValidatorUtils.empty(formId)) {
            return new CommonResult().validateFailed("前置参数错误，formId不能为空");
        }

        if (ValidatorUtils.empty(source)) {
            return new CommonResult().validateFailed("前置参数错误，source不能为空");
        }

        //校验formId是否已经存在
        /*if(memberService.exists(formId)) {
            return new CommonResult().validateFailed("前置参数错误，formId已经存在 formId：" + formId);
        }

        entity.setUserId(this.getCurrentMember().getId());
        entity.setFormId(formId);
        entity.setSource(source);
        entity.setStatus(1);

        memberService.insert(entity);*/

        return new CommonResult().success("添加成功");
    }



    @ApiOperation("用户注册接口(小程序)")
    @RequestMapping(value = "/register4MiniProgram")
    @ResponseBody
    public CommonResult<UmsMember> register4MiniProgram(UmsMemberVo umsMember)throws Exception {
        String msg = memberService.register4MiniProgram(umsMember);
        return new CommonResult().success(msg);
    }


    @ApiOperation("查询用户详情")
    @RequestMapping(value = "/queryUmsMemberDetail")
    @ResponseBody
    public CommonResult<UmsMember> queryUmsMemberDetail(){
        Long userId = UserUtils.getCurrentUmsMember().getId();//会员用户id
        UmsMember umsMember = memberService.getById(userId);
        return new CommonResult().success(umsMember);
    }

    @ApiOperation("查询搭配师详情")
    @RequestMapping(value = "/queryUserMatchDetail")
    @ResponseBody
    public CommonResult<SysUser> queryUserMatchDetail(){
        Long userId = UserUtils.getCurrentUmsMember().getId();//会员用户id
        UmsMember umsMember = memberService.getById(userId);
        Long userMatchId = umsMember.getMatchUserId();
        SysUser sysUser = iSysUserService.getById(userMatchId);
        return new CommonResult().success(sysUser);
    }

    @ApiOperation("查询注册参数接口")
    @RequestMapping(value = "/queryRegisterParam")
    @ResponseBody
    public CommonResult<List<UmsMemberRegisterParam>> queryRegisterParam(@ApiParam("类别,aspect-体貌特征;dressStyle-穿衣风格;dressColour-穿搭色系;neverDressStyle-永远都不会穿的款式;neverDressIcon-永远都不会穿的图案;rightLining-合适面料;enjoy_model-喜欢的版型;skuBudget-单品预算;neverDressStyle-永远都不会穿的款式;careClothes-您更在意衣服的;balanceBody-平衡身材问题") String category) {
        List<UmsMemberRegisterParam> umsMemberRegisterParams = iUmsMemberRegisterParamService.list(new QueryWrapper<UmsMemberRegisterParam>().eq("category",category ));
        return new CommonResult().success(umsMemberRegisterParams);
    }

    @IgnoreAuth
    @ApiOperation("查询行业列表")
    @RequestMapping(value = "/listIndustry")
    @ResponseBody
    public CommonResult<List<UmsMemberRegisterParam>> listIndustry() {
        List<UmsMemberRegisterParam> umsMemberRegisterParams = iUmsMemberRegisterParamService.list(new QueryWrapper<UmsMemberRegisterParam>().eq("category","industry" ));
        return new CommonResult().success(umsMemberRegisterParams);
    }

    @IgnoreAuth
    @ApiOperation("根据父id查询职业")
    @RequestMapping(value = "/listProfession")
    @ResponseBody
    public CommonResult<List<UmsMemberRegisterParam>> listProfession(Long parentId) {
        List<UmsMemberRegisterParam> umsMemberRegisterParams = iUmsMemberRegisterParamService.list(new QueryWrapper<UmsMemberRegisterParam>().eq("parent_id",parentId ));
        return new CommonResult().success(umsMemberRegisterParams);
    }

    @ApiOperation("判断手机号是否存在")
    @RequestMapping(value = "/isExist4Phone")
    @ResponseBody
    public CommonResult<Boolean> isExist4Phone(String phoneNo) {
        if(StringUtils.isEmpty(phoneNo)){
            return new CommonResult<Boolean>().failed("手机号不能为空");
        }
        int count = memberService.count(new QueryWrapper<UmsMember>().eq("phone", phoneNo));
        if(count > 0){
            return new CommonResult().success(true);
        }else{
            return new CommonResult().success(false);
        }
    }

    @ApiOperation("更新手机号")
    @RequestMapping(value = "/updatePhoneNo")
    @ResponseBody
    public CommonResult<Boolean> updatePhoneNo(String encryptedData,String iv,String phone) {
        UmsMember umsMember = UserUtils.getCurrentUmsMember();
        if(StringUtils.isEmpty(encryptedData) && !StringUtils.isEmpty(phone)){
            umsMember.setPhone(phone);
        }else{
            String phoneNo = WechatDecryptDataUtil.getPhoneNo(encryptedData, umsMember.getSessionKey(), iv);
            umsMember.setPhone(phoneNo);
        }
        memberService.updateById(umsMember);
        return new CommonResult<Boolean>().success();
    }

    @ApiOperation("修改手机号")
    @RequestMapping(value = "/modifyPhone")
    @ResponseBody
    public Object modifyPhoneByAuthCode(String phone,String authCode) {
        UmsMember umsMember = UserUtils.getCurrentUmsMember();
        return memberService.modifyPhoneByAuthCode(umsMember,phone,authCode);
    }

    @ApiOperation("根据被推荐的人id查询该推荐人推荐哪些人")
    @RequestMapping(value = "/getRecommedInfos")
    @ResponseBody
    @IgnoreAuth
    public Object getRecommedInfos() {
        Map<String,Object> paramMap = memberService.getRecommedInfos(UserUtils.getCurrentUmsMember().getId());
        return new CommonResult<>().success(paramMap);
    }

    @IgnoreAuth
    @ApiOperation("获取手机号")
    @RequestMapping(value = "/getPhoneNo")
    @ResponseBody
    public Object getPhoneNo(String encryptedData,String iv,String code) {
        String requestUrl = memberService.getWebAccess(code);
        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);
        String phoneNo = WechatDecryptDataUtil.getPhoneNo(encryptedData, sessionData.getString("session_key"), iv);
        return new CommonResult<>().success(phoneNo);
    }
}
