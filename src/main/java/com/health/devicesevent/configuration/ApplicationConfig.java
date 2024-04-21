package com.health.devicesevent.configuration;


import com.health.devicesevent.interceptor.TenantNameInterceptor;
import com.health.devicesevent.listener.KafkaConsumer;
import com.health.devicesevent.processor.S3Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ApplicationConfig implements WebMvcConfigurer {

    @Bean
    TenantNameInterceptor tenantNameInterceptor() {
        return new TenantNameInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( tenantNameInterceptor());
    }
}
