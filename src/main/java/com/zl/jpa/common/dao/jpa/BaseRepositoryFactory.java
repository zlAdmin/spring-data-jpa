package com.zl.jpa.common.dao.jpa;

import com.zl.jpa.common.dao.impl.RepositoryDao;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

@SuppressWarnings("unchecked")
public class BaseRepositoryFactory extends JpaRepositoryFactory {

    public BaseRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
                                                            EntityManager em) {

        JpaEntityInformation<Object, Serializable> entityMetadata2 = (JpaEntityInformation<Object, Serializable>) JpaEntityInformationSupport
            .getEntityInformation((Class<Object>) metadata.getDomainType(), em);
        return new RepositoryDao<Object, Serializable>(entityMetadata2, em);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return RepositoryDao.class;
    }
}
