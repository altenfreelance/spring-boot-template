package com.alten.template.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.List;

public class ProfileChecker implements EnvironmentPostProcessor {
    private static final List<String> PROFILES = Arrays.asList("dev", "test", "qa", "prod", "test-suite");

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

        if (!(activeProfiles.size() == 1 && PROFILES.contains(CollectionUtils.extractSingleton(activeProfiles)))) {
            throw new IllegalStateException(String.format("One and only one profile should be active. Allowed profiles: %s", PROFILES));
        }
    }
}