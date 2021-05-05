package com.lbf.pack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author lyr
 */

@Configuration
@EnableSwagger2
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket api() {
//        List<RequestParameter> globalRequestParameters = new ArrayList<>();
//        RequestParameter build = new RequestParameterBuilder().name("abc").description("添加swagger公共请求参数abc").required(true).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lbf.pack.controller"))
                .paths(PathSelectors.any())
                .build()
//                .globalRequestParameters(globalRequestParameters)
//                .securitySchemes(securitySchemes()) //添加token
//                .securityContexts(securityContexts()) //swagger配置页面访问是否需要传token
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("LoneybearForum接口文档")
                .description("swgger2 3.0.0")
                .termsOfServiceUrl("Terms of service")
                .version("1.0.0")
                .build();
    }


}

