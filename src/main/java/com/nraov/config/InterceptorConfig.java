package com.nraov.config;

import com.nraov.admin.interceptors.AdminTenantInterceptor;
import com.nraov.admin.interceptors.LoggingInterceptor;
import com.nraov.tenant.interceptors.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor())
                .addPathPatterns("/tenant/**");
        registry.addInterceptor(new AdminTenantInterceptor())
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/v2/api-docs", "/configuration/ui",
                        "/swagger-resources/**", "/configuration/**", "/swagger-ui.html"
                        , "/webjars/**", "/csrf", "/");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
