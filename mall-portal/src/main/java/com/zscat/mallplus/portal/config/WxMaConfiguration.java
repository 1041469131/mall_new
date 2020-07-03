package com.zscat.mallplus.portal.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import me.chanjar.weixin.common.api.WxConsts.KefuMsgType;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfiguration {

  private WxMaProperties properties;

  private static Map<String, WxMaMessageRouter> routers = Maps.newHashMap();
  private static Map<String, WxMaService> maServices = Maps.newHashMap();
  @Autowired
  private IUmsMemberService umsMemberService;
  @Autowired
  private RedisService redisService;
  @Autowired
  private ISysUserService sysUserService;
  @Autowired
  private JedisPool jedisPool;


  @Autowired
  public WxMaConfiguration(WxMaProperties properties) {
    this.properties = properties;
  }

  public static WxMaService getMaService(String appid) {
    WxMaService wxService = maServices.get(appid);
    if (wxService == null) {
      throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
    }

    return wxService;
  }

  public static WxMaMessageRouter getRouter(String appid) {
    return routers.get(appid);
  }

  @PostConstruct
  public void init() {
    List<WxMaProperties.Config> configs = this.properties.getConfigs();
    if (configs == null) {
      throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
    }

    maServices = configs.stream()
      .map(a -> {
        WxMaDefaultConfigImpl config = new WxMaRedisConfigImpl(jedisPool);
        config.setAppid(a.getAppid());
        config.setSecret(a.getSecret());
        config.setToken(a.getToken());
        config.setAesKey(a.getAesKey());
        config.setMsgDataFormat(a.getMsgDataFormat());

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        routers.put(a.getAppid(), this.newRouter(service));
        return service;
      }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));
  }

  private WxMaMessageRouter newRouter(WxMaService service) {
    final WxMaMessageRouter router = new WxMaMessageRouter(service);
    router.rule().async(false).msgType("text").handler(textHandler).end()
      .rule().async(false).msgType("event").event("user_enter_tempsession").handler(picHandler).end();
    return router;
  }

  private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
    System.out.println("textHandler：" + wxMessage.toString());
    if (wxMessage.getMsgType().equals("text") || wxMessage.getMsgType().equals(KefuMsgType.IMAGE)) {
      WxMaXmlOutMessage wxMaXmlOutMessage = new WxMaXmlOutMessage();
      wxMaXmlOutMessage.setFromUserName(wxMessage.getToUser());
      wxMaXmlOutMessage.setToUserName(wxMessage.getFromUser());
      wxMaXmlOutMessage.setMsgType(KefuMsgType.TRANSFER_CUSTOMER_SERVICE);
      wxMaXmlOutMessage.setCreateTime(System.currentTimeMillis());
      return wxMaXmlOutMessage;
    }
    return null;
  };

  private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
    if (StringUtils.equals(wxMessage.getSessionFrom(), "addMatcher")) {
      try {
        String openId = wxMessage.getFromUser();
        String mediaId = redisService.get("addMatcher:" + openId);
        if (mediaId == null) {
          UmsMember umsMember = umsMemberService.queryByOpenId(openId);
          if (umsMember == null) {
            return null;
          }
          Long matchUserId = umsMember.getMatchUserId();
          SysUser sysUser = sysUserService.getById(matchUserId);
          String wechatQrcodeUrl = sysUser.getWechatQrcodeUrl();
          if (wechatQrcodeUrl != null) {
            URL url = new URL(wechatQrcodeUrl);
            InputStream inputStream = url.openConnection().getInputStream();
            WxMediaUploadResult uploadResult = service.getMediaService()
              .uploadMedia("image", "png", inputStream);
            mediaId = uploadResult.getMediaId();
            redisService.set("addMatcher:" + openId, mediaId);
            redisService.expire("addMatcher:" + openId, 60 * 60 * 24 * 2);
          }
        }
        service.getMsgService()
          .sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("长按下图添加搭配师微信↓↓↓").toUser(wxMessage.getFromUser()).build());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newImageBuilder().mediaId(mediaId).toUser(openId).build());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  };
}
