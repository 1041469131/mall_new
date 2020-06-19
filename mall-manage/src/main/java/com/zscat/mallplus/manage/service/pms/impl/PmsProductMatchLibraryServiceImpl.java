package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.pms.IPmsProductMatchLibraryService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.wechat.WechatApiService;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.manage.utils.applet.TemplateData;
import com.zscat.mallplus.manage.utils.applet.WX_TemplateMsgUtil;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductMatchLibraryMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 搭配的服务类
 * @Date: 2019/10/14
 * @Description
 */
@Service
public class PmsProductMatchLibraryServiceImpl extends ServiceImpl<PmsProductMatchLibraryMapper,PmsProductMatchLibrary> implements IPmsProductMatchLibraryService {

  @Autowired
  private PmsProductMatchLibraryMapper pmsProductMatchLibraryMapper;
  @Override
  public Page<PmsProductMatchLibrary> listByPage(PmsProductQueryParam queryParam) {
    return pmsProductMatchLibraryMapper.listByPage(new Page<>(queryParam.getPageNum(),queryParam.getPageSize()),queryParam);
  }


}
