package com.zscat.mallplus.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.marking.entity.UserFormId;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    public CommonResult<UmsMember> register4MiniProgram(UmsMember umsMember) {
        umsMember.setId(UserUtils.getCurrentUmsMember().getId());
        umsMember.setUpdateTime(new Date());
        if(MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsComplete())){
            umsMember.setMatchUserId(iSysUserService.getRandomSysUser().getId());
        }
        if(memberService.updateById(umsMember)){
            return new CommonResult<>().success("注册成功");
        }else{
            return new CommonResult<>().failed("更新用户数据失败，注册失败");
        }
    }


    @ApiOperation("查询注册参数接口")
    @RequestMapping(value = "/queryRegisterParam")
    @ResponseBody
    public CommonResult<List<UmsMemberRegisterParam>> queryRegisterParam(@ApiParam("类别,aspect-体貌特征;dressStyle-穿衣风格;dressColour-穿搭色系;neverDressStyle-永远都不会穿的款式;neverDressIcon-永远都不会穿的图案;rightLining-合适面料;enjoy_model-喜欢的版型;skuBudget-单品预算;neverDressStyle-永远都不会穿的款式;careClothes-您更在意衣服的;balanceBody-平衡身材问题") String category) {
        List<UmsMemberRegisterParam> umsMemberRegisterParams = iUmsMemberRegisterParamService.list(new QueryWrapper<UmsMemberRegisterParam>().eq("category",category ));
        return new CommonResult<>().success(umsMemberRegisterParams);
    }

    @ApiOperation("查询用户详情")
    @RequestMapping(value = "/queryUmsMemberDetail")
    @ResponseBody
    public CommonResult<UmsMember> queryUmsMemberDetail(){
        Long userId = UserUtils.getCurrentUmsMember().getId();//会员用户id
        UmsMember umsMember = memberService.getById(userId);
        return new CommonResult<>().success(umsMember);
    }

    @ApiOperation("查询搭配师详情")
    @RequestMapping(value = "/queryUserMatchDetail")
    @ResponseBody
    public CommonResult<SysUser> queryUserMatchDetail(){
        Long userId = UserUtils.getCurrentUmsMember().getId();//会员用户id
        UmsMember umsMember = memberService.getById(userId);
        Long userMatchId = umsMember.getMatchUserId();
        SysUser sysUser = iSysUserService.getById(userMatchId);
        return new CommonResult<>().success(sysUser);
    }


}
