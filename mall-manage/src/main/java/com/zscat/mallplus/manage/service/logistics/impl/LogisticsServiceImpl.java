package com.zscat.mallplus.manage.service.logistics.impl;

import com.zscat.mallplus.manage.service.logistics.IlogisticsService;
import com.zscat.mallplus.manage.utils.HttpUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author xiang.li create date 2020/5/26 description
 */
@Service
public class LogisticsServiceImpl implements IlogisticsService {
  @Value("${logistics.host}")
  private String host;

  @Value("${logistics.path}")
  private String path;

  @Value("${logistics.method}")
  private String method;

  @Value("${logistics.appcode}")
  private String appcode;

  @Override
  public String query(String no) throws Exception {
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Authorization", "APPCODE " + appcode);
    Map<String, String> querys = new HashMap<String, String>();
    querys.put("no", no);
    HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
    String respon = EntityUtils.toString(response.getEntity());
    return respon;
  }
}
