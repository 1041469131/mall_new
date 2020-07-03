package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.marking.ISmsCouponService;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsMatchTimeService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.utils.CharUtil;
import com.zscat.mallplus.manage.utils.CommonUtil;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.manage.utils.JwtTokenUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.manage.vo.MemberDetails;
import com.zscat.mallplus.mbg.exception.ApiMallPlusException;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMatchTime;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberStatisticsInfo;
import com.zscat.mallplus.mbg.ums.entity.UmsRecommendRelation;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMemberTagRelationMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberStatisticsInfoMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsRecommendRelationMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberQueryParam;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberVo;
import com.zscat.mallplus.mbg.ums.vo.VUmsMemberVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements IUmsMemberService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
  @Autowired
  private UmsMemberMapper umsMemberMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RedisService redisService;

  @Autowired
  private SmsCouponHistoryMapper smsCouponHistoryMapper;

  @Autowired
  private UmsMemberStatisticsInfoMapper umsMemberStatisticsInfoMapper;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private ISmsCouponService iSmsCouponService;

  @Autowired
  private UmsRecommendRelationMapper umsRecommendRelationMapper;

  @Autowired
  private IUmsMemberRegisterParamService iUmsMemberRegisterParamService;

  @Autowired
  private ISysUserService iSysUserService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private ISmsService smsService;

  @Autowired
  private IUmsMatchTimeService umsMatchTimeService;

  @Value("${redis.key.prefix.authCode}")
  private String REDIS_KEY_PREFIX_AUTH_CODE;

  @Value("${authCode.expire.seconds}")
  private Long AUTH_CODE_EXPIRE_SECONDS;

  @Value("${jwt.tokenHead}")
  private String tokenHead;

  @Autowired
  private WxAppletProperties wxAppletProperties;

  @Override
  public UmsMember getByUsername(String username) {
    UmsMember umsMember = new UmsMember();
    umsMember.setUsername(username);
    return umsMemberMapper.selectOne(new QueryWrapper<>(umsMember));
  }

  @Override
  public UmsMember getById(Long id) {
    return umsMemberMapper.selectById(id);
  }

  @Override
  public CommonResult register(String username, String password, String telephone, String authCode) {
    //没有该用户进行添加操作
    UmsMember umsMember = new UmsMember();
    umsMember.setUsername(username);
    umsMember.setPhone(telephone);
    umsMember.setPassword(password);
    this.register(umsMember);
    return new CommonResult().success("注册成功", null);
  }

  @Override
  public CommonResult register(UmsMember user) {
    //验证验证码
        /*if (!verifyAuthCode(authCode, telephone)) {
            return new CommonResult().failed("验证码错误");
        }*/
    if (!user.getPassword().equals(user.getConfimpassword())) {
      return new CommonResult().failed("密码不一致");
    }
    //查询是否已有该用户
    UmsMember queryM = new UmsMember();
    queryM.setUsername(user.getUsername());
    queryM.setPassword(passwordEncoder.encode(user.getPassword()));
    UmsMember umsMembers = umsMemberMapper.selectOne(new QueryWrapper<>(queryM));
    if (umsMembers != null) {
      return new CommonResult().failed("该用户已经存在");
    }
    //没有该用户进行添加操作
    UmsMember umsMember = new UmsMember();
    umsMember.setUsername(user.getUsername());
    umsMember.setPhone(user.getPhone());
    umsMember.setPassword(passwordEncoder.encode(user.getPassword()));
    umsMember.setCreateTime(new Date());
    umsMember.setStatus(MagicConstant.ACCOUNT_STATUS_ON);
    umsMemberMapper.insert(umsMember);
    umsMember.setPassword(null);
    return new CommonResult().success("注册成功", null);
  }

  @Override
  public CommonResult generateAuthCode(String telephone) {
    int count = this.count(new QueryWrapper<UmsMember>().eq("phone", telephone));
    if (count > 0) {
      new CommonResult().failed("手机号存在");
    }
    return new CommonResult().success("获取验证码成功", smsService.generateAuthCode(telephone));
  }

  @Override
  public CommonResult updatePassword(String telephone, String password, String authCode) {
    UmsMember example = new UmsMember();
    example.setPhone(telephone);
    UmsMember member = umsMemberMapper.selectOne(new QueryWrapper<>(example));
    if (member == null) {
      return new CommonResult().failed("该账号不存在");
    }
    //验证验证码
    if (!smsService.verifyAuthCode(authCode, telephone)) {
      return new CommonResult().failed("验证码错误");
    }
    member.setPassword(passwordEncoder.encode(password));
    umsMemberMapper.updateById(member);
    return new CommonResult().success("密码修改成功", null);
  }

  @Override
  public UmsMember getCurrentMember() {
    try {
      SecurityContext ctx = SecurityContextHolder.getContext();
      Authentication auth = ctx.getAuthentication();
      MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
      return memberDetails.getUmsMember();
    } catch (Exception e) {
      return new UmsMember();
    }
  }

  @Override
  public void updateIntegration(Long id, Integer integration) {
    UmsMember record = new UmsMember();
    record.setId(id);
    record.setIntegration(integration);
    umsMemberMapper.updateById(record);
  }

  @Override
  public UmsMember queryByOpenId(String openId) {
    UmsMember queryO = new UmsMember();
    queryO.setWeixinOpenid(openId);
    List<UmsMember> umsMembers = umsMemberMapper.selectList(new QueryWrapper<>(queryO));
    if(!CollectionUtils.isEmpty(umsMembers)){
      return umsMembers.get(0);
    }
    return null;
  }

  @Override
  @Transactional
  public Object loginByWeixin(HttpServletRequest req) {
    try {
      String code = req.getParameter("code");
      if (StringUtils.isEmpty(code)) {
        System.out.println("code is empty");
      }
      String userInfos = req.getParameter("userInfo");
      String signature = req.getParameter("signature");
      Map<String, Object> me = JsonUtil.readJsonToMap(userInfos);
      if (null == me) {
        return new CommonResult<>().failed("登录失败");
      }
      Map<String, Object> resultObj = new HashMap<String, Object>();
      //获取openid
      //通过自定义工具类组合出小程序需要的登录凭证 code
      String requestUrl = this.getWebAccess(code);
      JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);
      if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
        return new CommonResult<>().failed("登录失败");
      }
      //验证用户信息完整性
      String sha1 = CommonUtil.getSha1(userInfos + sessionData.getString("session_key"));
      if (!signature.equals(sha1)) {
        return new CommonResult<>().failed("登录失败");
      }
      UmsMember userVo = this.queryByOpenId(sessionData.getString("openid"));
      String token;
      if (null == userVo) {
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
        umsMember.setSourceType(1);
        umsMember.setPassword(passwordEncoder.encode("123456"));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        umsMember.setBlance("0");
        umsMember.setIntegration(0);
        umsMember.setHistoryIntegration(0);
        umsMember.setWeixinOpenid(sessionData.getString("openid"));
        if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
          //会员头像(默认头像)
          umsMember.setIcon("/upload/img/avatar/01.jpg");
        } else {
          umsMember.setIcon(me.get("avatarUrl").toString());
        }
        // umsMember.setGender(Integer.parseInt(me.get("gender")));
        umsMember.setNickname(me.get("nickName").toString());
        umsMember.setSessionKey(sessionData.getString("session_key"));
        umsMember.setGender(Integer.valueOf(me.get("gender").toString()));
        umsMember.setCreateDate(System.currentTimeMillis());
        umsMemberMapper.insert(umsMember);
        saveUmsStatisticsInfo(umsMember);
        token = jwtTokenUtil.generateToken(umsMember.getUsername());
        resultObj.put("userId", umsMember.getId());
        resultObj.put("is_complete", "0");
      } else {
        token = jwtTokenUtil.generateToken(userVo.getUsername());
        userVo.setSessionKey(sessionData.getString("session_key"));
        umsMemberMapper.updateById(userVo);
        resultObj.put("userId", String.valueOf(userVo.getId()));
        resultObj.put("is_complete", userVo.getIsComplete());
      }

      if (StringUtils.isEmpty(token)) {
        return new CommonResult<>().failed("登录失败");
      }
      resultObj.put("tokenHead", tokenHead);
      resultObj.put("token", token);
      resultObj.put("userInfo", me);
      return new CommonResult<>().success(resultObj);
    } catch (ApiMallPlusException e) {
      e.printStackTrace();
      return new CommonResult<>().failed(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return new CommonResult<>().failed(e.getMessage());
    }

  }

  /**
   * 新增会员统计信息
   */
  private void saveUmsStatisticsInfo(UmsMember umsMember) {
    UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
    umsMemberStatisticsInfo.setMemberId(umsMember.getId());
    umsMemberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
  }

  @Override
  public Map<String, Object> login(String username, String password) {
    Map<String, Object> tokenMap = new HashMap<>();
    String token = null;
    //密码需要客户端加密后传递
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password));
    try {
           /* Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UmsMember member = this.getByUsername(username);
            token = jwtTokenUtil.generateToken(userDetails);*/
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        throw new BadCredentialsException("密码不正确");
      }
      MemberDetails memberDetails = (MemberDetails) userDetails;
      UmsMember member = memberDetails.getUmsMember();
      //   Authentication authentication = authenticationManager.authenticate(authenticationToken);
      Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
      token = jwtTokenUtil.generateToken(userDetails);
      tokenMap.put("userInfo", member);
    } catch (AuthenticationException e) {
      LOGGER.warn("登录异常:{}", e.getMessage());

    }
    tokenMap.put("token", token);
    tokenMap.put("tokenHead", tokenHead);
    return tokenMap;
  }

  @Override
  public String refreshToken(String oldToken) {
    String token = oldToken.substring(tokenHead.length());
    if (jwtTokenUtil.canRefresh(token)) {
      return jwtTokenUtil.refreshToken(token);
    }
    return null;
  }


  //替换字符串
  public String getCode(String APPID, String REDIRECT_URI, String SCOPE) {
    return String.format(wxAppletProperties.getGetCode(), APPID, REDIRECT_URI, SCOPE);
  }

  //替换字符串
  public String getWebAccess(String CODE) {
    return String.format(wxAppletProperties.getWebAccessTokenhttps(),
      wxAppletProperties.getAppId(),
      wxAppletProperties.getSecret(),
      CODE);
  }

  //替换字符串
  public String getUserMessage(String access_token, String openid) {
    return String.format(wxAppletProperties.getUserMessage(), access_token, openid);
  }


  @Override
  public UmsMember getRandomUmsMember() {
    return umsMemberMapper.getRandomUmsMember();
  }

  @Override
  public Object loginByAuthCode(String telephone, String authCode) {
    Map<String, Object> resultObj = new HashMap<String, Object>();
    //验证码绑定手机号并存储到redis
    String oldAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
    if (!authCode.equals(oldAuthCode)) {
      return new CommonResult().success("验证码填写不正确");
    }
    UmsMember userVo = this.getOne(new QueryWrapper<UmsMember>().eq("phone", telephone));
    if (userVo == null) {
      return new CommonResult().success("该手机号还没有在系统注册");
    }
    String token = jwtTokenUtil.generateToken(userVo.getUsername());
    resultObj.put("userId", userVo.getId());
    resultObj.put("is_complete", userVo.getIsComplete());
    if (StringUtils.isEmpty(token)) {
      return new CommonResult<>().failed("登录失败");
    }
    resultObj.put("tokenHead", tokenHead);
    resultObj.put("token", token);
    Map<String, Object> me = new HashMap<>();
    me.put("avatarUrl", userVo.getIcon());
    me.put("gender", userVo.getGender());
    me.put("nickName", userVo.getNickname());
    resultObj.put("userInfo", me);
    return new CommonResult<>().success(resultObj);
  }

  @Override
  public Object modifyPhoneByAuthCode(UmsMember umsMember, String phone, String authCode) {
    String oldAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + phone);
    if (!authCode.equals(oldAuthCode)) {
      return new CommonResult().failed("验证码填写不正确");
    }
    umsMember.setPhone(phone);
    this.updateById(umsMember);
    return new CommonResult().success();
  }

  @Override
  public Map<String, Object> getRecommedInfos(Long recommendedId) {
    Map<String, Object> paramMap = new HashMap<>(4);
    List<UmsMember> umsMembers = umsMemberMapper.getRecommedInfos(recommendedId);
    paramMap.put("umsMembers", umsMembers);
    BigDecimal totalAmount = smsCouponHistoryMapper.getTotalAmout(recommendedId);
    paramMap.put("totalAmount", totalAmount);
    if (!CollectionUtils.isEmpty(umsMembers)) {
      paramMap.put("totalSize", umsMembers.size());
    }
    return paramMap;
  }

  @Override
  @Transactional
  public String register4MiniProgram(UmsMemberVo umsMember) {
    Long memberId = UserUtils.getCurrentUmsMember().getId();
    umsMember.setId(memberId);
    umsMember.setUpdateTime(new Date());
    if (MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsRegister())) {
      Long matchUserId = umsMember.getMatchUserId();
      if (umsMember.getMatchUserId() == null) {
        matchUserId = iSysUserService.getRandomSysUser().getId();
      }
      umsMember.setMatchUserId(matchUserId);
      setMatchTime(umsMember, matchUserId);
    }
    updateById(umsMember);
    if (MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsRegister())) {
      if (!org.apache.commons.lang.StringUtils.isEmpty(umsMember.getRecommendId())) {
        Integer count = umsRecommendRelationMapper
          .selectCount(new QueryWrapper<UmsRecommendRelation>().eq("recommended_id", memberId).eq("status", "1"));
        if (count == 0) {
          UmsRecommendRelation umsRecommendRelation = new UmsRecommendRelation();
          umsRecommendRelation.setStatus("1");
          umsRecommendRelation.setCreateTime(new Date());
          umsRecommendRelation.setUpdateTime(new Date());
          umsRecommendRelation.setRecommendedId(memberId);
          umsRecommendRelation.setRecommendId(Long.valueOf(umsMember.getRecommendId()));
          umsRecommendRelationMapper.insert(umsRecommendRelation);
          //分享之后将优惠券发放给被推荐的人
          //  iSmsCouponService.allocateCoupon("4", umsMember.getId());
          //将优惠券分享给推荐的人
          //   iSmsCouponService.allocateCoupon("4", umsRecommendRelation.getRecommendId());
          return "注册成功";
        }
      }
    }
//      if (MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsComplete())) {
//        //分配注册优惠券
//        String msg = iSmsCouponService.allocateCoupon("3", umsMember.getId());
//        if (!org.apache.commons.lang.StringUtils.isEmpty(msg)) {
//          msg = ",但是分配优惠券失败，请联系客服;";
//          return "完善资料成功" + msg;
//        } else {
//          return "完善资料成功,优惠券已发放";
//        }
//      }
    return null;
  }

  private void setMatchTime(UmsMemberVo umsMember, Long matchUserId) {
    //插入时间节点
    List<UmsMatchTime> timeList = umsMatchTimeService
      .list(new QueryWrapper<UmsMatchTime>().lambda().eq(UmsMatchTime::getMemberId, umsMember.getId()));
    if (CollectionUtils.isEmpty(timeList)) {
      UmsMember umsMemberQuery = umsMemberMapper.selectById(umsMember.getId());
      UmsMatchTime umsMatchTime = new UmsMatchTime();
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, 3);
      umsMatchTime.setMatchTime(calendar.getTime());
      umsMatchTime.setMatchUserId(matchUserId);
      umsMatchTime.setMemberId(umsMember.getId());
      umsMatchTime.setStatus(0);
      if (umsMemberQuery.getDressFreq() != null) {
        UmsMemberRegisterParam umsMemberRegisterParam = iUmsMemberRegisterParamService
          .findById(Long.valueOf(umsMemberQuery.getDressFreq()));
        umsMatchTime.setDressFreqMonth(dealDressFreqMonth(umsMemberRegisterParam.getName()));
      } else {
        umsMatchTime.setDressFreqMonth(2);
      }
      umsMatchTimeService.save(umsMatchTime);
    }
  }

  @Override
  public Page<UmsMember> pageUmsMembers(Page<UmsMember> umsMemberPage, UmsMemberQueryParam queryParam) {
    return umsMemberMapper.pageUmsMembers(umsMemberPage, queryParam);
  }

  @Override
  public Page<UmsMemberVo> pageUmsMemberVOs(Page<UmsMemberVo> umsMemberPage, Map<String, Object> paramMap) {
    return umsMemberMapper.pageUmsMemberVOs(umsMemberPage, paramMap);
  }

  @Override
  @Transactional
  public Page<VUmsMemberVo> listVUmsMembers(VUmsMemberVo vUmsMemberVo) {
    return umsMemberMapper.pageVUmsMembers(new Page<>(vUmsMemberVo.getPageNum(), vUmsMemberVo.getPageSize()), vUmsMemberVo);
  }

  private Integer dealDressFreqMonth(String dressFreqName) {
    switch (dressFreqName) {
      case "每个月":
        return 1;
      case "每2个月":
        return 2;
      case "每个季度":
        return 3;
      default:
        return 6;
    }
  }
}
