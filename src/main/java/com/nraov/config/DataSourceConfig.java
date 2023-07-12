package com.nraov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dsurl;

    @Value("${spring.datasource.username}")
    private String dsuser;

    @Value("${spring.datasource.password}")
    private String dspwd;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(dsurl)
                .username(dsuser)
                .password(dspwd)
                .build();
    }
}
