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
    public static SysUser assembleSysUser(UmsApplyMatcherVo umsApplyMatcherVo,String userName) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(userName);
        sysUser.setName(umsApplyMatcherVo.getUserName());
        sysUser.setCreateTime(new Date());
        sysUser.setNickName(umsApplyMatcherVo.getNickname());
        sysUser.setWechatName(umsApplyMatcherVo.getWechatNo());
        sysUser.setWechatQrcodeUrl(umsApplyMatcherVo.getWechatTwoCode());
        sysUser.setPhone(umsApplyMatcherVo.getPhone());
        sysUser.setIcon(umsApplyMatcherVo.getIcon());
        sysUser.setIntroduction(umsApplyMatcherVo.getIntroduce());
        sysUser.setType("1");
        //TODO 设置等级
        sysUser.setLevel("1");
        sysUser.setRoleIds("10");
        return sysUser;
    }
}
