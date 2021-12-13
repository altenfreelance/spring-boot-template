package com.alten.template.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "template-api")
@Data
public class ExampleProperties {
    private String customPropOne;
}