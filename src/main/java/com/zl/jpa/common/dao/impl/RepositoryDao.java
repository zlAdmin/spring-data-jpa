package com.zl.jpa.common.dao.impl;

import com.zl.jpa.common.dao.IRepository;
import com.zl.jpa.common.dao.QueryMap;
import com.zl.jpa.common.dao.support.Page;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @ProjectName: jpa
 * @create 2019-12-18 14:42
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2019</p>
 **/
@NoRepositoryBean
@Slf4j
public class RepositoryDao <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements IRepository<T, ID> {

    private final EntityManager entityManager;

    public RepositoryDao(final JpaEntityInformation<T, ?> entityInformation, final EntityManager em) {
        super(entityInformation, em);
        entityManager = em;
    }

    public RepositoryDao(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        entityManager = em;
    }

    @Override
    public int executeUpdate(String hql, Object... values) {
        Session session = this.getJpaSession();
        int result = 0;
        try {
            Query query = session.createQuery(hql);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    query.setParameter(i, values[i]);
                }
            }
            result = query.executeUpdate();
        } catch (HibernateException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List find(String hql, Object... values) {
        Assert.hasText(hql, "需要为字符串类型");
        return createQuery(hql, values).list();
    }

    @Override
    public Page pagedNativeQuery(String sql, QueryMap qm, Object... values) {
        Page page = null;
        javax.persistence.Query query = null;
        query = createSQLNativeQuery(sql , values);

        javax.persistence.Query countQuery = createSQLNativeQuery("select count(*) from ("+sql+") as logCount" , values);
        Object o = countQuery.getSingleResult();
        int totalCount =0;
        if(o instanceof BigInteger){
            totalCount = ((BigInteger)o).intValue();
        }
        page = pagedNativeQuery(query, qm.getPageNum(), qm.getCURRENT_ROWS_SIZE(),totalCount);

        return page;
    }

    protected Page pagedNativeQuery(final javax.persistence.Query query, final int pageNo,
                                    final int pageSize,int totalCount) {
        if (totalCount<1) {
            return new Page(0, 0, pageSize, new ArrayList());
        }
        int startIndex = Page.getStartOfPage(pageNo, pageSize, totalCount);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).getResultList();

        return new Page(startIndex, totalCount, pageSize, list);
    }

    @Override
    public Query createQuery(String hql, Object... values) {
        Assert.hasText(hql, "类型错误");
        Query query = getJpaSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    @Override
    public Query createSQLQuery(String sql) {
        Query query = getJpaSession().createSQLQuery(sql);
        return query;
    }

    @Override
    public javax.persistence.Query createSQLNativeQuery(String sql, Object... values) {
        javax.persistence.Query query = entityManager
                .createNativeQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i+1, values[i]);
            }
        }
        return query;
    }

    @Override
    public void execSQL(String sqlString, Object... values) {
        javax.persistence.Query query = entityManager
                .createNativeQuery(sqlString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i+1, values[i]);
            }
        }
        query.executeUpdate();
    }

    private Session getJpaSession() {
        return entityManager.unwrap(Session.class);
    }
}
