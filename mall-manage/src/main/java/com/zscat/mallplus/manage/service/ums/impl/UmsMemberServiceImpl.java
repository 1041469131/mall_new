package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.marking.ISmsCouponService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.utils.*;
import com.zscat.mallplus.manage.vo.MemberDetails;
import com.zscat.mallplus.mbg.exception.ApiMallPlusException;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductUserMatchLibraryMapper;
import com.zscat.mallplus.mbg.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsRecommendRelation;
import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMemberTagRelationMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsRecommendRelationMapper;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberVo;
import com.zscat.mallplus.mbg.ums.vo.VUmsMemberVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

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

    private Logger logger = LoggerFactory.getLogger(UmsMemberServiceImpl.class);

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Autowired
    private PmsProductUserMatchLibraryMapper pmsProductUserMatchLibraryMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
   /* @Resource
    private AuthenticationManager authenticationManager;*/

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ISmsCouponService iSmsCouponService;

    @Autowired
    private UmsRecommendRelationMapper umsRecommendRelationMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;

    @Value("${authCode.expire.seconds}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UmsMemberMemberTagRelationMapper umsMemberMemberTagRelationMapper;


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
        if (!user.getPassword().equals(user.getConfimpassword())){
            return new CommonResult().failed("密码不一致");
        }
        //查询是否已有该用户

        UmsMember queryM = new UmsMember();
        queryM.setUsername(user.getUsername());
        queryM.setPassword(passwordEncoder.encode(user.getPassword()));
        UmsMember umsMembers = umsMemberMapper.selectOne(new QueryWrapper<>(queryM));
        if (umsMembers!=null) {
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
    public CommonResult generateAuthCode(String telephone, String accessKeyId, String accessSecret, String templateCode) {
        int count = this.count(new QueryWrapper<UmsMember>().eq("phone", telephone));
        if(count > 0){
            new CommonResult().failed("手机号存在");
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String envir = SpringContextHolder.getActiveProfile();
        if("dev".equals(envir)){
            sb.append("123456");
        }else{
            for (int i = 0; i < 6; i++) {
                sb.append(random.nextInt(10));
            }
            String tempParam = " { \"code\":"+sb.toString()+" }";
            logger.info("发送验证码开始");
            System.out.println("发送验证码开始");
            SendSmsUtil.sendMessage(telephone, tempParam,accessKeyId, accessSecret, templateCode);
            logger.info("发送验证码结束");
            System.out.println("发送验证码结束");
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return new CommonResult().success("获取验证码成功", sb.toString());
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMember example = new UmsMember();
        example.setPhone(telephone);
        UmsMember member = umsMemberMapper.selectOne(new QueryWrapper<>(example));
        if (member==null) {
            return new CommonResult().failed("该账号不存在");
        }
        //验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
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
        }catch (Exception e){
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


    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return authCode.equals(realAuthCode);
    }

    @Override
    public UmsMember queryByOpenId(String openId) {
        UmsMember queryO = new UmsMember();
        queryO.setWeixinOpenid(openId);
        return umsMemberMapper.selectOne(new QueryWrapper<>(queryO));
    }

    @Override
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
            String requestUrl = this.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code

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
            String token = null;
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

                umsMemberMapper.insert(umsMember);
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
                resultObj.put("is_complete", "0");
            }else {
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
        }catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>().failed(e.getMessage());
        }

    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        //密码需要客户端加密后传递
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password));
        try {
           /* Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UmsMember member = this.getByUsername(username);
            token = jwtTokenUtil.generateToken(userDetails);*/

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
            UmsMember member = this.getByUsername(username);
            //   Authentication authentication = authenticationManager.authenticate(authenticationToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo",member);
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
    @Autowired
    private WxAppletProperties wxAppletProperties;

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
        if(!authCode.equals(oldAuthCode)){
            return new CommonResult().success("验证码填写不正确");
        }

        UmsMember userVo = this.getOne(new QueryWrapper<UmsMember>().eq("phone",telephone));
        if(userVo == null){
            return new CommonResult().success("改手机号还没有在系统注册");
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
        me.put("avatarUrl",userVo.getIcon());
        me.put("gender",userVo.getGender());
        me.put("nickName",userVo.getNickname());
        resultObj.put("userInfo", me);
        return new CommonResult<>().success(resultObj);
    }

    @Override
    public Object modifyPhoneByAuthCode(UmsMember umsMember, String phone, String authCode) {
        String oldAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + phone);
        if(!authCode.equals(oldAuthCode)){
            return new CommonResult().failed("验证码填写不正确");
        }
        umsMember.setPhone(phone);
        this.updateById(umsMember);
        return new CommonResult().success();
    }

    @Override
    public Map<String,Object> getRecommedInfos(Long recommendedId) {
        Map<String,Object> paramMap = new HashMap<>();
        List<UmsMember> umsMembers = umsMemberMapper.getRecommedInfos(recommendedId);
        paramMap.put("umsMembers",umsMembers);
        BigDecimal totalAmount = smsCouponHistoryMapper.getTotalAmout(recommendedId);
        paramMap.put("totalAmount",totalAmount);
        if(!CollectionUtils.isEmpty(umsMembers)){
            paramMap.put("totalSize",umsMembers.size());
        }
        return paramMap;
    }

    @Override
    @Transactional
    public String register4MiniProgram(UmsMemberVo umsMember, Long matchUserId) {
        umsMember.setId(UserUtils.getCurrentUmsMember().getId());
        umsMember.setUpdateTime(new Date());
        if(MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsRegister())){
            umsMember.setMatchUserId(matchUserId);
        }
        if(updateById(umsMember)){
            if(MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsRegister())){
                if(!org.apache.commons.lang.StringUtils.isEmpty(umsMember.getRecommendId())){
                    Integer count = umsRecommendRelationMapper.selectCount(new QueryWrapper<UmsRecommendRelation>().eq("recommended_id",UserUtils.getCurrentUmsMember().getId()).
                            eq("status","1"));
                    if(count == 0){
                        UmsRecommendRelation umsRecommendRelation = new UmsRecommendRelation();
                        umsRecommendRelation.setStatus("1");
                        umsRecommendRelation.setCreateTime(new Date());
                        umsRecommendRelation.setUpdateTime(new Date());
                        umsRecommendRelation.setRecommendedId(UserUtils.getCurrentUmsMember().getId());
                        umsRecommendRelation.setRecommendId(Long.valueOf(umsMember.getRecommendId()));
                        umsRecommendRelationMapper.insert(umsRecommendRelation);
                        iSmsCouponService.allocateCoupon("4",umsMember.getId());//分享之后将优惠券发放给被推荐的人
                        iSmsCouponService.allocateCoupon("4",umsRecommendRelation.getRecommendId());//将优惠券分享给推荐的人
                        return "注册成功";
                    }
                }
            }
            if(MagicConstant.UMS_IS_COMPLETE_DONE.equals(umsMember.getIsComplete())){
                String msg = iSmsCouponService.allocateCoupon("3",umsMember.getId());//分配注册优惠券
                if(!org.apache.commons.lang.StringUtils.isEmpty(msg)){
                    msg = ",但是分配优惠券失败，请联系客服;";
                    return "完善资料成功"+msg;
                }else{
                    return "完善资料成功,优惠券已发放";
                }
            }
    }
        return null;
    }

    @Override
    public Page<UmsMemberVo> pageUmsMembers(Page<UmsMemberVo> umsMemberPage, Map<String, Object> paramMap) {
        return umsMemberMapper.pageUmsMembers(umsMemberPage,paramMap);
    }

    @Override
    @Transactional
    public List<VUmsMemberVo> listVUmsMembers(Long matchUserId) {
        List<VUmsMemberVo> vUmsMemberVos = null;
        List<VUmsMember> vUmsMembers = umsMemberMapper.listVUmsMembers(matchUserId);
        if(!CollectionUtils.isEmpty(vUmsMembers)){
            vUmsMemberVos = new ArrayList<>();
            for (VUmsMember vUmsMember : vUmsMembers){
                VUmsMemberVo vUmsMemberVo = new VUmsMemberVo();
                BeanUtils.copyProperties(vUmsMember,vUmsMemberVo);
                List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries = pmsProductUserMatchLibraryMapper.selectList(new QueryWrapper<PmsProductUserMatchLibrary>().
                        eq("user_id",vUmsMember.getId()).eq("recommend_type","1").orderByDesc("update_time"));
                if(!CollectionUtils.isEmpty(pmsProductUserMatchLibraries)){
                    Map<String,Object> skuMap = new HashMap<>();
                    for(PmsProductUserMatchLibrary pmsProductUserMatchLibrary : pmsProductUserMatchLibraries){
                        String[] skus = pmsProductUserMatchLibrary.getSkuIds().split(",");
                        for(String sku:skus){
                            skuMap.put(sku,sku);
                        }
                    }
                    vUmsMemberVo.setRecomendCount(skuMap.keySet().size());
                    String status = getStatus(vUmsMember.getRecomendDate(),vUmsMemberVo.getDressFreqCode());
                }
                vUmsMemberVos.add(vUmsMemberVo);
            }
        }
        return vUmsMemberVos;
    }

    /**
     * 获取粉丝的处理状态
     * @param recomendTime
     * @param dressFreqCode
     * @return
     */
    private String getStatus(Date recomendTime, String dressFreqCode) {
        Date nextRecomendTime = null;
        if(recomendTime != null){
            if("everymonth".equals(dressFreqCode)){
                nextRecomendTime = DateUtils.adjustMonth(recomendTime,1 );
            }else if("everytwomonth".equals(dressFreqCode)){
                nextRecomendTime = DateUtils.adjustMonth(recomendTime,2 );
            }else if("everyquarter".equals(dressFreqCode)){
                nextRecomendTime = DateUtils.adjustMonth(recomendTime,3 );
            }
        }else{
            return "0";
        }
        return "0";
    }
}
