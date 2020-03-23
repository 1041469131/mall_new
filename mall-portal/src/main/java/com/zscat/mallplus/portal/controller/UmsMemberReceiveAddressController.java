package com.zscat.mallplus.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.ums.IUmsMemberReceiveAddressService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberReceiveAddress;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 会员收货地址管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/28.
 */
@Controller
@Api(tags = "UmsMemberReceiveAddressController", description = "会员收货地址管理")
@RequestMapping("/api/address")
public class UmsMemberReceiveAddressController extends ApiBaseAction {
    @Autowired
    private IUmsMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UmsMemberReceiveAddress address) {
        address.setMemberId(UserUtils.getCurrentUmsMember().getId());
        address.setCreateTime(new Date());
        address.setUpdateTime(new Date());
        boolean count = memberReceiveAddressService.save(address);
        if (count) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        boolean count = memberReceiveAddressService.removeById(id);
        if (count) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/save")
    @ResponseBody
    public Object update(UmsMemberReceiveAddress address) {
        boolean count = false ;
        address.setMemberId(UserUtils.getCurrentUmsMember().getId());
        address.setCreateTime(new Date());
        address.setUpdateTime(new Date());
        if(address.getDefaultStatus() == MagicConstant.DEFAULT_STATUS_YES){
            int defaultCount = memberReceiveAddressService.count(new QueryWrapper<UmsMemberReceiveAddress>().
                    eq("member_id", UserUtils.getCurrentUmsMember().getId()).eq("default_status", MagicConstant.DEFAULT_STATUS_YES));
            if(defaultCount > 0){
                UmsMemberReceiveAddress def = new UmsMemberReceiveAddress();
                def.setDefaultStatus(MagicConstant.DEFAULT_STATUS_NO);
                def.setUpdateTime(new Date());
                memberReceiveAddressService.update(def, new QueryWrapper<UmsMemberReceiveAddress>().
                        eq("member_id", UserUtils.getCurrentUmsMember().getId()).eq("default_status", MagicConstant.DEFAULT_STATUS_YES));
            }
        }
        if (address!=null && address.getId()!=null){
             count = memberReceiveAddressService.updateById( address);
        }else {
            count = memberReceiveAddressService.save(address);
        }
        if (count ) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @IgnoreAuth
    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list() {
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressService.list(new QueryWrapper<UmsMemberReceiveAddress>().eq("member_id",
                UserUtils.getCurrentUmsMember().getId()));
        return new CommonResult().success(addressList);
    }

    @IgnoreAuth
    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object getItem(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getById(id);
        return new CommonResult().success(address);
    }
    @IgnoreAuth
    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/getItemDefaut", method = RequestMethod.GET)
    @ResponseBody
    public Object getItemDefaut() {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getDefaultItem();
        return new CommonResult().success(address);
    }
    /**
     *
     * @param id
     * @return
     */
    @ApiOperation("设为默认地址")
    @RequestMapping(value = "/address-set-default")
    @ResponseBody
    public Object setDefault(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = memberReceiveAddressService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }
}
