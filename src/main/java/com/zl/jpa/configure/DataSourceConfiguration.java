package com.zl.jpa.configure;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: cxy@acmtc.com
 * \* Date: 2018/8/8
 * \* Time: 18:47
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Configuration
public class DataSourceConfiguration {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    @Value("${zl.druid.allow:}")
    private String allow;
    @Value("${zl.druid.deny:}")
    private String deny;
    @Value("${zl.druid.username:}")
    private String druidUser;
    @Value("${zl.druid.password:}")
    private String druidPwd;

    @Bean // 声明其为Bean实例
    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);

        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setDefaultAutoCommit(true);
        dataSource.setTimeBetweenEvictionRunsMillis(3600000);
        dataSource.setMinEvictableIdleTimeMillis(3600000);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("select 1");
        List<Filter> filters = new ArrayList<Filter>(){
            {
                StatFilter statFilter = new StatFilter();
                statFilter.setSlowSqlMillis(30000);
                statFilter.setLogSlowSql(true);
                statFilter.setMergeSql(true);
                add(statFilter);
                WallFilter wallFilter = new WallFilter();
                WallConfig config = new WallConfig();
                config.setConditionAndAlwayTrueAllow(true);
                config.setDir("META-INF/druid/wall/mysql");
                config.init();
                wallFilter.setConfig(config);
                add(wallFilter);
                Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
                slf4jLogFilter.setResultSetLogEnabled(true);
                slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
                add(slf4jLogFilter);
            }
        };

        dataSource.setProxyFilters(filters);
        return dataSource;
    }
    /**
     * 注册一个StatViewServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet(){
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //添加初始化参数：initParams //白名单：
        if(!StringUtils.isEmpty(allow)){
            servletRegistrationBean.addInitParameter("allow",allow);
        }
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        if(!StringUtils.isEmpty(deny)){
            servletRegistrationBean.addInitParameter("deny",deny);
        }
        //登录查看信息的账号密码.
        if(!StringUtils.isEmpty(druidUser)){
            servletRegistrationBean.addInitParameter("loginUsername",druidUser);
            if(!StringUtils.isEmpty(druidPwd)){
                servletRegistrationBean.addInitParameter("loginPassword",druidPwd);
            }
        }
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean; }
}