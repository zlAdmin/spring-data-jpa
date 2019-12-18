package com.zl.jpa.common.dao;

import com.zl.jpa.common.dao.support.Page;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义jpa查询
 *
 * @author zhanglei
 * @ProjectName: jpa
 * @create 2019-12-18 14:31
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2019</p>
 **/
@NoRepositoryBean
public interface IRepository <T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * 执行HQL更新
     *
     * @param hql
     * @param values
     * @return
     */
    int executeUpdate(final String hql, final Object... values);

    /**
     * 查询,values对应hql中变量
     *
     * @param hql
     * @param values
     * @return
     */
    List find(final String hql, final Object... values);


    Page pagedNativeQuery(final String sql, final QueryMap qm, final Object... values);

    Query createQuery(String hql, Object... values);

    /**
     * 不带条件的查询
     *
     * @param hql
     * @return
     */
    Query createSQLQuery(String hql);

    /**
     * 执行原生SQL，用于执行查询
     *
     * @param sql
     * @return
     */
    javax.persistence.Query createSQLNativeQuery(String sql, Object... values);

    /**
     * 执行原生SQL，一般用于增删改
     *
     * @param sqlString
     */
    void execSQL(String sqlString, Object... values);
}
