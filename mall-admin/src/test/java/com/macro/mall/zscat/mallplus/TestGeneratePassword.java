package com.macro.mall.zscat.mallplus;

import com.zscat.mallplus.MallAdminApplication;
import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.utils.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallAdminApplication.class)
public class TestGeneratePassword {
    @Autowired
    ISmsService smsService;
    @Autowired
    RestTemplate restTemplate;
    @Test
    public void testGeneratePassword(){
        PasswordEncoder passwordEncoder = SpringContextHolder.getBean(PasswordEncoder.class);
        System.out.println(passwordEncoder.encode("123456"));
    }


    @Test
    public void testSendMsg(){
        smsService.sendUserName("13026219036","lx123456");
    }

    @Test
    public void testOrderMsg(){
        smsService.deliveryNotify("15657191959","lx123456","公司");
    }


    @Test
    public void testweather(){
        String url="http://t.weather.sojson.com/api/weather/city/101240714";
        String jstoken = restTemplate.getForObject(url,String.class);
        //获取到token
        System.out.println(jstoken);


    }
}
