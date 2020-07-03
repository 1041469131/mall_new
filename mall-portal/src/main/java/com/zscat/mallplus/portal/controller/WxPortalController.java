package com.zscat.mallplus.portal.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.portal.config.WxMaConfiguration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/api/portal/{appid}")
public class WxPortalController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @GetMapping(produces = "text/plain;charset=utf-8")
  @IgnoreAuth
  public String authGet(@PathVariable String appid,
    @RequestParam(name = "signature", required = false) String signature,
    @RequestParam(name = "timestamp", required = false) String timestamp,
    @RequestParam(name = "nonce", required = false) String nonce,
    @RequestParam(name = "echostr", required = false) String echostr) {
    this.logger.info("\n接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
      signature, timestamp, nonce, echostr);

    if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
      throw new IllegalArgumentException("请求参数非法，请核实!");
    }

    final WxMaService wxService = WxMaConfiguration.getMaService(appid);

    if (wxService.checkSignature(timestamp, nonce, signature)) {
      return echostr;
    }

    return "非法请求";
  }

  @PostMapping(produces = "application/xml; charset=UTF-8")
  @IgnoreAuth
  public String post(@PathVariable String appid,
    @RequestBody String requestBody,
    @RequestParam(name = "msg_signature", required = false) String msgSignature,
    @RequestParam(name = "encrypt_type", required = false) String encryptType,
    @RequestParam(name = "signature", required = false) String signature,
    @RequestParam("timestamp") String timestamp,
    @RequestParam("nonce") String nonce) {
    this.logger.info("\n接收微信请求：[msg_signature=[{}], encrypt_type=[{}], signature=[{}]," +
        " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
      msgSignature, encryptType, signature, timestamp, nonce, requestBody);

    final WxMaService wxService = WxMaConfiguration.getMaService(appid);

    final boolean isJson = Objects.equals(wxService.getWxMaConfig().getMsgDataFormat(),
      WxMaConstants.MsgDataFormat.JSON);
    if (StringUtils.isBlank(encryptType)) {
      // 明文传输的消息
      WxMaMessage inMessage;
      if (isJson) {
        inMessage = WxMaMessage.fromJson(requestBody);
      } else {//xml
        inMessage = WxMaMessage.fromXml(requestBody);
      }

      return resultMsgString(appid, isJson, inMessage);
    }

    if ("aes".equals(encryptType)) {
      // 是aes加密的消息
      WxMaMessage inMessage;
      if (isJson) {
        inMessage = WxMaMessage.fromEncryptedJson(requestBody, wxService.getWxMaConfig());
      } else {//xml
        inMessage = WxMaMessage.fromEncryptedXml(requestBody, wxService.getWxMaConfig(),
          timestamp, nonce, msgSignature);
      }
      return resultMsgString(appid, isJson, inMessage);
    }

    throw new RuntimeException("不可识别的加密类型：" + encryptType);
  }

  private String resultMsgString(@PathVariable String appid, boolean isJson,
    WxMaMessage inMessage) {
    WxMaXmlOutMessage route = this.route(inMessage, appid);
    if (route != null) {
      if (isJson) {
        Map map = new LinkedHashMap();
        map.put("ToUserName", route.getToUserName());
        map.put("FromUserName", route.getFromUserName());
        map.put("CreateTime", route.getCreateTime());
        map.put("MsgType", route.getMsgType());
        return JSONObject.fromObject(map).toString();
      } else {
        return route.toXml();
      }
    }
    return "success";
  }

  private WxMaXmlOutMessage route(WxMaMessage message, String appid) {
    try {
      return WxMaConfiguration.getRouter(appid).route(message);
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }
    return null;
  }

}
