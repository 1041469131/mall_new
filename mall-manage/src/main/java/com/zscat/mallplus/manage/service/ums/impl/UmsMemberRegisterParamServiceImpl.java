package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberRegisterParamMapper;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户注册信息实现类
 */
@Service
public class UmsMemberRegisterParamServiceImpl extends ServiceImpl<UmsMemberRegisterParamMapper,UmsMemberRegisterParam> implements IUmsMemberRegisterParamService ,
  InitializingBean {

  private  Map<Long, UmsMemberRegisterParam> umsMemberRegisterParamMap;




  @Override
  public void afterPropertiesSet() throws Exception {
    List<UmsMemberRegisterParam> data = list(new QueryWrapper<>());
    umsMemberRegisterParamMap = data.stream().collect(Collectors.toMap(UmsMemberRegisterParam::getId, Function.identity()));
  }

  @Override
  public UmsMemberRegisterParam findById(Long id) {
    return umsMemberRegisterParamMap.get(id);
  }
}
