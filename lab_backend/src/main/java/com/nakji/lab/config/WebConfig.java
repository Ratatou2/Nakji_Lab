package com.nakji.lab.config;

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
        return environment.getProperty(key);
    }

    // env로 CORS 매핑
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(getProperty("local1"), getProperty("local2"), getProperty("server1"), getProperty("server2"))
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}