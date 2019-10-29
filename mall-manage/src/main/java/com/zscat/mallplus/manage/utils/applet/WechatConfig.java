package com.zscat.mallplus.manage.utils.applet;

import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.utils.SpringContextHolder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

@SuppressWarnings("deprecation")
public class WechatConfig {

    private static SSLConnectionSocketFactory sslcsf;

    public static SSLConnectionSocketFactory getSslcsf() {
        if (null == sslcsf) {
            setSsslcsf();
        }
        return sslcsf;
    }

    private static void setSsslcsf() {
        try {
            WxAppletProperties wxAppletProperties = SpringContextHolder.getBean(WxAppletProperties.class);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            Thread.currentThread().getContextClassLoader();
            InputStream instream = new WechatRefundApiResult().getClass().getResourceAsStream(wxAppletProperties.getCertName());
            try {
                keyStore.load(instream, wxAppletProperties.getMchId().toCharArray());
            } finally {
                instream.close();
            }
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, wxAppletProperties.getMchId().toCharArray()).build();
            sslcsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
