package com.zscat.mallplus.manage.utils;

import com.zscat.mallplus.manage.bo.AdminUserDetails;
import com.zscat.mallplus.manage.vo.MemberDetails;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Auther: shenzhuan
 * @Date: 2019/3/30 16:26
 * @Description:
 */
public class UserUtils {

    public static SysUser getCurrentMember() {
        try {
            SecurityContext ctx = SecurityContextHolder.getContext();
            Authentication auth = ctx.getAuthentication();
            AdminUserDetails memberDetails = (AdminUserDetails) auth.getPrincipal();
            return memberDetails.getUmsAdmin();
        } catch (Exception e) {
            return new SysUser();
        }
    }

    public static UmsMember getCurrentUmsMember() {
        try {
            SecurityContext ctx = SecurityContextHolder.getContext();
            Authentication auth = ctx.getAuthentication();
            MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
            return memberDetails.getUmsMember();
        }catch (Exception e){
            return new UmsMember();
        }
    }
}
