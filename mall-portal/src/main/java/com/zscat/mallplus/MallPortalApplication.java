package com.zscat.mallplus;

import javafx.application.Application;
import lombok.Value;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.zscat.mallplus.mapper", "com.zscat.mallplus.ums.mapper", "com.zscat.mallplus.marking.mapper", "com.zscat.mallplus.cms.mapper", "com.zscat.mallplus.sys.mapper", "com.zscat.mallplus.oms.mapper", "com.zscat.mallplus.pms.mapper"})
@EnableTransactionManagement
@EnableScheduling
public class MallPortalApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {

        SpringApplication.run(MallPortalApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
