package com.zl.jpa.common.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class RepositoryFactoryBean<T extends JpaRepository<Object, Serializable>>
		extends JpaRepositoryFactoryBean<T, Object, Serializable> {

	public RepositoryFactoryBean (Class<? extends T> repositoryInterface){
		super(repositoryInterface);
	}
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {

		return new BaseRepositoryFactory(em);
	}
}