package com.system.lms.fo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:key-${spring.profiles.active}.properties")
@PropertySource("classpath:uri-${spring.profiles.active}.properties")
public class AppConfig {
}
