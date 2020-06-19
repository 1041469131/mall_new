package com.zscat.mallplus.manage.service.sms;

/**
 * @author xiang.li create date 2020/5/20 description
 */
public interface ISmsService {
  /**
   * 生成验证码
   */
  String generateAuthCode(String telephone);

  /**
   * 校验验证码
   * @param authCode
   * @param telephone
   * @return
   */
  boolean verifyAuthCode(String authCode, String telephone);


  void sendUserName(String telephone,String msg);

  void deliveryNotify(String telephone,String deliverySn,String deliveryCompany);

}
