
package com.lbf.pack;

import com.lbf.pack.mapper.UserLoginMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

//@SpringBootConfiguration
//@EnableAutoConfiguration
//@ComponentScan("com.lbf.pack")

@SpringBootApplication

public class LoneybearForumApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(LoneybearForumApplication.class, args);
//        String[] names = run.getBeanDefinitionNames();
//        for(String name:names){
//            System.out.println(name);
//        }
        UserLoginMapper users;

    }



}
