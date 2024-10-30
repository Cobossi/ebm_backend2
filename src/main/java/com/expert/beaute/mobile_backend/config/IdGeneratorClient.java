package com.expert.beaute.mobile_backend.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.util.UUID;
public class IdGeneratorClient implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return "client-" + UUID.randomUUID().toString().replace("-", "");
    }
}
