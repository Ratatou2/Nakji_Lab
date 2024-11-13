package com.nakji.lab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@PropertySource("classpath:env.properties")
public class WebConfig implements WebMvcConfigurer {
    private final Environment environment;

    public WebConfig(Environment environment) {
        this.environment = environment;
    }
    public String getProperty(String key){
        String temp = environment.getProperty(key);
        System.out.println(temp);
        return temp;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081", "http://babywombat.zapto.org", "http://babywombat.zapto.org:10260")
                .allowedMethods("*")
                .allowedHeaders("*");

        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8081", "http://babywombat.zapto.org", "http://babywombat.zapto.org:10260")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(getProperty("local1"), getProperty("local2"), getProperty("server"), "http://babywombat.zapto.org", "http://babywombat.zapto.org:10260")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*");
//
//        registry.addMapping("/api/**")
//                .allowedOrigins(getProperty("server"), "http://babywombat.zapto.org", "http://babywombat.zapto.org:10260")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*");
//    }
}