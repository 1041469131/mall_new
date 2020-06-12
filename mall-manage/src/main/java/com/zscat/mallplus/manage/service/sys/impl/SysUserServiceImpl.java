package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.bo.Rediskey;
import com.zscat.mallplus.manage.service.sys.ISysRolePermissionService;
import com.zscat.mallplus.manage.service.sys.ISysUserPermissionService;
import com.zscat.mallplus.manage.service.sys.ISysUserRoleService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.manage.utils.JwtTokenUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.sys.entity.*;
import com.zscat.mallplus.mbg.sys.mapper.*;
import com.zscat.mallplus.mbg.sys.vo.SysUserVO;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.mapper.UmsApplyMatcherMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

     @Autowired(required = false)
     private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper adminRoleRelationMapper;
    @Autowired
    private ISysUserRoleService adminRoleRelationService;
    @Autowired
    private SysUserPermissionMapper adminPermissionRelationMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private ISysUserPermissionService userPermissionService;
    @Autowired
    private ISysRolePermissionService rolePermissionService;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UmsApplyMatcherMapper umsApplyMatcherMapper;
    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public String login(String username, String password) {
        String token = null;

        //密码需要客户端加密后传递
     //   UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password));
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
         //   Authentication authentication = authenticationManager.authenticate(authenticationToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            this.removePermissRedis(UserUtils.getCurrentMember().getId());
        } catch (AuthenticationException e) {
            log.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public int updateUserRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        SysUserRole userRole = new SysUserRole();
        userRole.setAdminId(adminId);
        adminRoleRelationMapper.delete(new QueryWrapper<>(userRole));
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysUserRole> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole roleRelation = new SysUserRole();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            userRoleService.saveBatch(list);
        }
        return count;
    }

    @Override
    public List<SysRole> getRoleListByUserId(Long adminId) {
        return roleMapper.getRoleListByUserId(adminId);
    }

    @Override
    public int updatePermissionByUserId(Long adminId, List<Long> permissionIds) {
        //删除原所有权限关系
        SysUserPermission userPermission = new SysUserPermission();
        userPermission.setAdminId(adminId);
        adminPermissionRelationMapper.delete(new QueryWrapper<>(userPermission));
        //获取用户所有角色权限
        List<SysPermission> permissionList = permissionMapper.listMenuByUserId(adminId);
        List<Long> rolePermissionList = permissionList.stream().map(SysPermission::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<SysUserPermission> relationList = new ArrayList<>();
            //筛选出+权限
            List<Long> addPermissionIdList = permissionIds.stream().filter(permissionId -> !rolePermissionList.contains(permissionId)).collect(Collectors.toList());
            //筛选出-权限
            List<Long> subPermissionIdList = rolePermissionList.stream().filter(permissionId -> !permissionIds.contains(permissionId)).collect(Collectors.toList());
            //插入+-权限关系
            relationList.addAll(convert(adminId, 1, addPermissionIdList));
            relationList.addAll(convert(adminId, -1, subPermissionIdList));
            userPermissionService.saveBatch(relationList);
        }
        return 0;
    }

    @Override
    public List<SysPermission> getPermissionListByUserId(Long adminId) {
        if (!redisService.exists(String.format(Rediskey.menuList,adminId))){
            List<SysPermission> list= permissionMapper.listMenuByUserId(adminId);
            redisService.set(String.format(Rediskey.menuTreesList,adminId), JsonUtil.objectToJson(list));
            return list;
        }else {
            return JsonUtil.jsonToList(redisService.get(String.format(Rediskey.menuTreesList,adminId)),SysPermission.class);
        }

    }

    @Override
    public boolean saves(SysUser umsAdmin) {
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户

        List<SysUser> umsAdminList = sysUserMapper.selectList(new QueryWrapper<SysUser>().eq("username",umsAdmin.getUsername()));
        if (umsAdminList.size() > 0) {
            return false;
        }
        //将密码进行加密操作
        if (StringUtils.isEmpty(umsAdmin.getPassword())){
            umsAdmin.setPassword("123456");
        }
        String md5Password = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(md5Password);
        sysUserMapper.insert(umsAdmin);
        updateRole(umsAdmin.getId(),umsAdmin.getRoleIds());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updates(SysUser admin) {
        if(!StringUtils.isEmpty(admin.getPassword())){
            String md5Password = passwordEncoder.encode(admin.getPassword());
            admin.setPassword(md5Password);
        }
        updateRole(admin.getId(),admin.getRoleIds());
        if(admin.getPhone()!=null){
            //如果手机号不为空说明是修改手机号 umsApplyMatcher也需要修改手机号
            SysUser sysUser = sysUserMapper.selectById(admin.getId());
            UmsApplyMatcher umsApplyMatcher = umsApplyMatcherMapper.selectOne(new QueryWrapper<UmsApplyMatcher>().eq("phone", sysUser.getPhone()));
            umsApplyMatcher.setPhone(admin.getPhone());
            umsApplyMatcherMapper.updateById(umsApplyMatcher);
            UmsApplyMatcher umsApplyMatcher1 = umsApplyMatcherMapper.selectOne(new QueryWrapper<UmsApplyMatcher>().eq("invite_phone", sysUser.getPhone()));
            umsApplyMatcher1.setInvitePhone(admin.getPhone());
            umsApplyMatcherMapper.updateById(umsApplyMatcher1);
        }
         sysUserMapper.updateById(admin);
        return true;
    }

    @Override
    public List<SysPermission> listUserPerms(Long id) {
        if (StringUtils.isEmpty(redisService.get(String.format(Rediskey.menuList,id)))){
            List<SysPermission> list= permissionMapper.listUserPerms(id);
            String key =String.format(Rediskey.menuList,id);
            redisService.set(key,JsonUtil.objectToJson(list));
            return list;
        }else {
            return JsonUtil.jsonToList(redisService.get(String.format(Rediskey.menuList,id)),SysPermission.class);
        }


    }

    @Override
    public List<SysPermission> listPermissions() {
        return permissionMapper.selectList(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getStatus,1));
    }

    @Override
    public void removePermissRedis(Long id) {
        redisService.remove(String.format(Rediskey.menuTreesList,id));
        redisService.remove(String.format(Rediskey.menuList,id));
    }

    /**
     * 将+-权限关系转化为对象
     */
    private List<SysUserPermission> convert(Long adminId, Integer type, List<Long> permissionIdList) {
        List<SysUserPermission> relationList = permissionIdList.stream().map(permissionId -> {
            SysUserPermission relation = new SysUserPermission();
            relation.setAdminId(adminId);
            relation.setType(type);
            relation.setPermissionId(permissionId);
            return relation;
        }).collect(Collectors.toList());
        return relationList;
    }
    public void updateRole(Long adminId, String roleIds) {
        this.removePermissRedis(adminId);
        adminRoleRelationMapper.delete(new QueryWrapper<SysUserRole>().eq("admin_id",adminId));
        //建立新关系
        if (!StringUtils.isEmpty(roleIds)) {
            String[] rids = roleIds.split(",");
            List<SysUserRole> list = new ArrayList<>();
            for (String roleId : rids) {
                SysUserRole roleRelation = new SysUserRole();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(Long.valueOf(roleId));
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }
    }

    @Override
    public SysUser getRandomSysUser() {
        return sysUserMapper.getRandomSysUser();
    }

    @Override
    public Page<SysUserVO> pageMatcherUsers(SysUserVO sysUser) {
        Page<SysUserVO> page = new Page<>(sysUser.getPageNum(),sysUser.getPageSize());
        return sysUserMapper.pageMatcherUsers(page,sysUser);
    }

    @Override
    public Page<SysUserVO> pageMyInviteMatcherUsers(SysUserVO sysUser) {
        Page<SysUserVO> page = new Page<>(sysUser.getPageNum(),sysUser.getPageSize());
        return sysUserMapper.pageMyInviteMatcherUsers(page,sysUser);
    }


}
