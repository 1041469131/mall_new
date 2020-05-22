package com.zscat.mallplus.manage.service.sms.impl;

import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.utils.SendSmsUtil;
import com.zscat.mallplus.manage.utils.SpringContextHolder;
import freemarker.template.utility.StringUtil;
import java.util.Random;
import javax.swing.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author xiang.li create date 2020/5/20 description
 */
@Service
public class SmsServiceImpl implements ISmsService {

  @Value("${redis.key.prefix.authCode}")
  private String REDIS_KEY_PREFIX_AUTH_CODE;

  @Value("${authCode.expire.seconds}")
  private Long AUTH_CODE_EXPIRE_SECONDS;

  @Value("${send.sms.accessKeyId}")
  private String accessKeyId;

  @Value("${send.sms.accessSecret}")
  private String accessSecret;

  @Value("${send.sms.authCode.templateCode}")
  private String templateCode;

  @Autowired
  private RedisService redisService;

  private Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

  @Override
  public String generateAuthCode(String telephone) {
    StringBuilder sb = new StringBuilder();
    String profile = SpringContextHolder.getActiveProfile();
    if (profile != null && profile.equals("dev")) {
      sb.append("123456");
    } else {
      Random random = new Random();
      for (int i = 0; i < 6; i++) {
        sb.append(random.nextInt(10));
      }
      String tempParam = " { \"code\":" + sb.toString() + " }";
      logger.info("发送验证码开始");
      System.out.println("发送验证码开始");
      SendSmsUtil.sendMessage(telephone, tempParam, accessKeyId, accessSecret, templateCode);
      logger.info("发送验证码结束");
      System.out.println("发送验证码结束");
    }
    //验证码绑定手机号并存储到redis
    redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
    redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
    return sb.toString();
  }

  @Override
  public boolean verifyAuthCode(String authCode, String telephone) {
    if (StringUtils.isEmpty(authCode)) {
      return false;
    }
    String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
    return authCode.equals(realAuthCode);
  }
}
