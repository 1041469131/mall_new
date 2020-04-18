package com.macro.mall.zscat.mallplus;

import com.zscat.mallplus.MallAdminApplication;
import com.zscat.mallplus.manage.utils.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallAdminApplication.class)
public class TestGeneratePassword {

    @Test
    public void testGeneratePassword(){
        PasswordEncoder passwordEncoder = SpringContextHolder.getBean(PasswordEncoder.class);
        System.out.println(passwordEncoder.encode("123456"));
    }
}
