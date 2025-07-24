package com.zwap.config.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.SessionManager;
import org.springframework.vault.config.AbstractVaultConfiguration;

@Configuration
public class VaultSessionManagerConfig {

    @Bean
    public BeanPostProcessor sessionManagerBeanPostProcessor(AbstractVaultConfiguration vaultConfiguration) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (SessionManager.class.isInstance(bean)) {
                    return new NoSessionManager(vaultConfiguration.clientAuthentication());
                }
                return bean;
            }
        };
    }
}