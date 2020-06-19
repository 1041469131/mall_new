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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallAdminApplication.class)
public class TestGeneratePassword {
    @Autowired
    ISmsService smsService;
    @Test
    public void testGeneratePassword(){
        PasswordEncoder passwordEncoder = SpringContextHolder.getBean(PasswordEncoder.class);
        System.out.println(passwordEncoder.encode("123456"));
    }


    @Test
    public void testSendMsg(){
        smsService.sendUserName("13026219036","lx123456");
    }
}
