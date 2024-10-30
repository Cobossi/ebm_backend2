package com.expert.beaute.mobile_backend.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

public class ConfigurableIdGenerator implements IdentifierGenerator {
    public static final String PREFIX_PARAM = "prefix";
    private String prefix = "";

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) {
        // Récupère le préfixe des paramètres de configuration
        prefix = parameters.getProperty(PREFIX_PARAM, "");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return prefix.isEmpty() ? uuid : prefix + "-" + uuid;
    }
}