package com.zscat.mallplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用启动入口
 * https://github.com/shenzhuan/mallplus on 2018/4/26.
 */
@SpringBootApplication
@MapperScan({"com.zscat.mallplus.mbg.ums.mapper", "com.zscat.mallplus.mbg.marking.mapper", "com.zscat.mallplus.mbg.cms.mapper", "com.zscat.mallplus.mbg.sys.mapper", "com.zscat.mallplus.mbg.oms.mapper", "com.zscat.mallplus.mbg.pms.mapper"})
@EnableTransactionManagement
public class MallAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApplication.class, args);
    }
}
