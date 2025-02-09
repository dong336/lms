package com.system.lms.fo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:key.properties")
@PropertySource("classpath:uri.properties")
public class AppConfig {
}
