package com.zscat.mallplus.manage.assemble;

import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.vo.UmsApplyMatcherVo;

import java.util.Date;

/**
 * 组装系统用户
 */
public class SysUserAssemble {
    /**
     * 根据申请人的信息生成系统用户（基本上是搭配师）
     * @param umsApplyMatcherVo
     * @return
     */
    public static SysUser assembleSysUser(UmsApplyMatcherVo umsApplyMatcherVo) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(umsApplyMatcherVo.getUserName());
        sysUser.setName(umsApplyMatcherVo.getUserName());
        sysUser.setCreateTime(new Date());
        sysUser.setNickName(umsApplyMatcherVo.getNickname());
        sysUser.setWechatName(umsApplyMatcherVo.getWechatNo());
        sysUser.setWechatQrcodeUrl(umsApplyMatcherVo.getWechatTwoCode());
        sysUser.setPhone(umsApplyMatcherVo.getPhone());
        sysUser.setIcon(umsApplyMatcherVo.getIcon());
        sysUser.setType("0");
        sysUser.setLevel("common");
        sysUser.setRoleIds("10");
        return sysUser;
    }
}
