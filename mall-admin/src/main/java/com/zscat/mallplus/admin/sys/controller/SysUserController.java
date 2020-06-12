package com.zscat.mallplus.admin.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.dto.SysUserPassWordDto;
import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.service.sys.ISysPermissionService;
import com.zscat.mallplus.manage.service.sys.ISysRoleService;
import com.zscat.mallplus.manage.service.sys.ISysUserAccountService;
import com.zscat.mallplus.manage.service.sys.ISysUserRoleService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.manage.vo.SmsResultVO;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.sys.entity.SysPermission;
import com.zscat.mallplus.mbg.sys.entity.SysRole;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.sys.entity.SysUserAccount;
import com.zscat.mallplus.mbg.sys.entity.SysUserRole;
import com.zscat.mallplus.mbg.sys.vo.SysUserVO;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Slf4j
@Api(description = "用户管理", tags = "SysUserController")
@RestController
@RequestMapping("/sys/sysUser")
public class SysUserController extends ApiController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysPermissionService permissionService;
    @Resource
    private RedisService redisService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISmsService smsService;
    @Autowired
    private ISysUserAccountService sysUserAccountService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有用户列表")
    @ApiOperation("根据条件查询所有用户列表")
    @GetMapping(value = "/list")
    public Object getUserByPage(SysUser entity,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(sysUserService.page(new Page<SysUser>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有用户列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存用户")
    @ApiOperation("保存用户")
    @PostMapping(value = "/register")
    public Object saveUser(@RequestBody SysUser entity) {
        try {
            if (sysUserService.saves(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存用户：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "修改密码")
    @ApiOperation("修改密码")
    @PostMapping(value = "/setPassword")
    public CommonResult<String> updatePassword(@RequestBody @ApiParam(value = "修改密码的入参", required = true) SysUserPassWordDto entity) {
        try {
            SysUser currentMember = sysUserService.getById(entity.getId());
            if(!passwordEncoder.matches(entity.getOldPwd(),currentMember.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
            currentMember.setPassword(entity.getNewPwd());
            if (sysUserService.updates(currentMember)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("修改密码失败：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }




    @SysLog(MODULE = "sys", REMARK = "更新用户")
    @ApiOperation("更新用户")
    @PostMapping(value = "/update")
    public CommonResult updateUser(@ApiParam("用户") @RequestBody SysUser entity) {
        try {
            if (sysUserService.updates(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新用户：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除用户")
    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public Object deleteUser(@ApiParam("用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("用户id");
            }
            if (sysUserService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除用户：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给用户分配角色")
    @ApiOperation("查询用户明细")
    @GetMapping(value = "/{id}")
    public Object getUserById(@ApiParam("用户id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("用户id");
            }
            SysUser coupon = sysUserService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询用户明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "sys", REMARK = "刷新token")
    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    @ResponseBody
    public Object refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = sysUserService.refreshToken(token);
        if (refreshToken == null) {
            return new CommonResult().failed();
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @SysLog(MODULE = "sys", REMARK = "登录以后返回token")
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody SysUser umsAdminLoginParam, BindingResult result) {
        String token = sysUserService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return new CommonResult().paramFailed("用户名或密码错误");
        }
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
       return new CommonResult().success(tokenMap);
    }

    @SysLog(MODULE = "sys", REMARK = "获取当前登录用户信息")
    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SysUser> getAdminInfo(Principal principal) {
        String username = principal.getName();
        SysUser queryU = new SysUser();
        queryU.setUsername(username);
        SysUser umsAdmin = sysUserService.getOne(new QueryWrapper<>(queryU));
        List<String> roles = null;
        if(umsAdmin != null){
            List<SysUserRole> sysUserRoles = iSysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("admin_id",umsAdmin.getId()));
            if(!CollectionUtils.isEmpty(sysUserRoles)){
                roles = new ArrayList<>();
               for(SysUserRole sysUserRole:sysUserRoles){
                   roles.add(sysUserRole.getRoleId().toString());
               }
               umsAdmin.setRoleIds(String.join(",",roles));
            }
        }
        return new CommonResult<SysUser>().success(umsAdmin);
    }

    @SysLog(MODULE = "sys", REMARK = "登出功能")
    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        return new CommonResult().success(null);
    }


    @SysLog(MODULE = "sys", REMARK = "给用户分配角色")
    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds) {
        int count = sysUserService.updateUserRole(adminId, roleIds);
        if (count >= 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "获取指定用户的角色")
    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SysRole>> getRoleList(@PathVariable Long adminId) {
        List<SysRole> roleList = sysUserService.getRoleListByUserId(adminId);
        return new CommonResult().success(roleList);
    }

    @SysLog(MODULE = "sys", REMARK = "获取指定用户的角色")
    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/userRoleCheck", method = RequestMethod.GET)
    @ResponseBody
    public Object userRoleCheck(@RequestParam("adminId") Long adminId) {
        List<SysRole> roleList = sysUserService.getRoleListByUserId(adminId);
        List<SysRole> allroleList = roleService.list(new QueryWrapper<>());
        for (SysRole a : allroleList) {
            for (SysRole u : roleList) {
                if (a.getId().equals(u.getId())) {
                    a.setChecked(true);
                }
            }
        }
        return new CommonResult().success(allroleList);
    }

    @SysLog(MODULE = "sys", REMARK = "给用户分配+-权限")
    @ApiOperation("给用户分配+-权限")
    @RequestMapping(value = "/permission/update", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePermission(@RequestParam Long adminId,
                                   @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = sysUserService.updatePermissionByUserId(adminId, permissionIds);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "获取用户所有权限（包括+-权限）")
    @ApiOperation("获取用户所有权限（包括+-权限）")
    @RequestMapping(value = "/permission/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getPermissionList(@PathVariable Long adminId) {
        List<SysPermission> permissionList = sysUserService.getPermissionListByUserId(adminId);
        return new CommonResult().success(permissionList);
    }

    /*****************************新增接口************************************************/

    @SysLog(MODULE = "sys", REMARK = "获取搭配师列表")
    @ApiOperation("分页获取获取搭配师列表")
    @RequestMapping(value = "/pageMatcherUsers", method = RequestMethod.POST)
    @ResponseBody
    public Object pageMatcherUsers(@RequestBody SysUserVO sysUser) {
        Page<SysUserVO> sysUserVOPage = sysUserService.pageMatcherUsers(sysUser);
        return new CommonResult<>().success(sysUserVOPage);
    }

    @SysLog(MODULE = "sys", REMARK = "获取我的邀请列表")
    @ApiOperation("分页获取获取搭配师列表")
    @RequestMapping(value = "/pageMyInviteMatcherUsers", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Page<SysUserVO>> pageMyInviteMatcherUsers(@ApiParam("参数") @RequestBody SysUserVO sysUser) {
        sysUser.setId(UserUtils.getCurrentMember().getId());
        Page<SysUserVO> sysUserVOPage = sysUserService.pageMyInviteMatcherUsers(sysUser);
        return new CommonResult<Page<SysUserVO>>().success(sysUserVOPage);
    }

    @SysLog(MODULE = "sys", REMARK = "根据手机号获取系统用户")
    @ApiOperation("根据手机号获取系统用户")
    @RequestMapping(value = "/getSysUserByPhone", method = RequestMethod.GET)
    @ResponseBody
    public Object getSysUserByPhone(String phone) {
        if(StringUtils.isEmpty(phone)){
            new CommonResult<>().failed("手机号不能为空");
        }
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", phone));
        return new CommonResult<>().success(sysUser);
    }

    @IgnoreAuth
    @ApiOperation(value="获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Void> getAuthCode(@ApiParam(value = "电话号码") @RequestParam  String telephone) {
       try {
           smsService.generateAuthCode(telephone);
       }catch (Exception e){
           return new CommonResult().failed("发送验证码失败");
       }

        return new CommonResult().success();
    }

    @ApiOperation("根据手机号和验证码进行校验")
    @RequestMapping(value = "/verifyAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Boolean> verifyAuthCode(@ApiParam(value = "电话号码") @RequestParam String telephone,@ApiParam(value = "验证码") @RequestParam String authCode) {
        boolean verifyAuthCode = smsService.verifyAuthCode( authCode,telephone);
        if(!verifyAuthCode){
            return new CommonResult<Boolean>().failed("验证码填写不正确");
        }
        return new CommonResult<Boolean>().success();
    }


    @ApiOperation("获取收款账户")
    @RequestMapping(value = "/account/get", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SysUserAccount> getAccount(@ApiParam(value = "用户id") @RequestParam Long id) {
        SysUserAccount sysUserAccount = sysUserAccountService.getOne(new QueryWrapper<SysUserAccount>().eq("sys_user_id", id));
        return new CommonResult<SysUserAccount>().success(sysUserAccount);
    }

    @ApiOperation("保存收款账户")
    @RequestMapping(value = "/account/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SysUserAccount> saveAccount(@ApiParam(value = "用户id") @RequestBody  SysUserAccount sysUserAccount) {
         sysUserAccountService.saveOrUpdate(sysUserAccount);
        return new CommonResult<SysUserAccount>().success(sysUserAccount);
    }
}

