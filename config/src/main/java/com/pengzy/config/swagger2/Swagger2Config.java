package com.pengzy.config.swagger2;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


@EnableSwagger2WebMvc
@EnableKnife4j
@Component
@Configuration
public class Swagger2Config {

    /**
     * 前台API分组
     *
     * @return
     */
    @Bean(value = "indexApi")
    public Docket indexApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("前台API分组")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pengzy.server.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 后台API分组
     *
     * @return
     */
    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pengzy"))
                .paths(PathSelectors.any())
                .build();
    }
//
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                //.title("swagger-bootstrap-ui RESTful APIs")
//                .description("swagger-bootstrap-ui")
//                .termsOfServiceUrl("http://localhost:8088/")
//                .contact(new Contact("pengzy框架搭建","http://localhost:8088/","developer@mail.com"))
//                .version("1.0")
//                .build();
//    }


//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .useDefaultResponseMessages(false)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.pengzy.**.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("knife4j在线API接口文档")
                .contact(new Contact("Roc-xb", "https://www.loveiu.top/", "2831053223@qq.com"))
                .version("v3.0.0")
                .title("knife4j在线API接口文档")
                .build();
    }
}
