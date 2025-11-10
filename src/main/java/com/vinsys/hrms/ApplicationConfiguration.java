package com.vinsys.hrms;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ApplicationConfiguration {

	@Bean
	ClassLoaderTemplateResolver emailTemplateResolver() {
		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
		emailTemplateResolver.setPrefix("templates/");
		emailTemplateResolver.setTemplateMode("HTML5");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode("XHTML");
		emailTemplateResolver.setCharacterEncoding("UTF-8");
		emailTemplateResolver.setOrder(1);
		return emailTemplateResolver;
	}

	@Value("${hb.hibernateDialect}")
	private String hibernateDialect;
	@Value("${hb.packagesToScan}")
	private String packagesToScan;
	@Value("${db.driver}")
	private String driver;
	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;
	@Value("${hb.showSql}")
	private String showSQL;
	@Value("${db.pool.min}")
	private String minPool;
	@Value("${db.pool.max}")
	private String maxPool;
	@Value("${db.pool.idlePeriod}")
	private String idlePeriod;

	@Value("${sqldb.driver}")
	private String sqldriver;
	@Value("${sqldb.url}")
	private String sqlurl;
	@Value("${sqldb.username}")
	private String sqlusername;
	@Value("${sqldb.password}")
	private String sqlpassword;
	@Value("${sqldb.pool.min}")
	private String sqlMinPool;
	@Value("${sqldb.pool.max}")
	private String sqlMaxPool;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactory.setDataSource(dataSource);
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.dialect", hibernateDialect);
		jpaProperties.setProperty("hibernate.show_sql", showSQL);
//		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "validate");
//		jpaProperties.setProperty("hibernate.hbm2ddl.schema_filter_provider", "com.vinsys.hrms.MySchemaFilterProvider");
		entityManagerFactory.setJpaProperties(jpaProperties);
		entityManagerFactory.setPackagesToScan(packagesToScan);
		return entityManagerFactory;
	}

	@Bean(name = "dataSourceSQLServer")
	DataSource dataSourceSQLServer() {
		// a named Data Source is best practice for later jmx monitoring
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(sqldriver);
		dataSource.setJdbcUrl(sqlurl);
		dataSource.setUsername(sqlusername);
		dataSource.setPassword(sqlpassword);
		dataSource.setMinimumIdle(Integer.parseInt(sqlMinPool));
		dataSource.setMaximumPoolSize(Integer.parseInt(sqlMaxPool));

		return dataSource;
	}

	@Bean
	@Primary
	DataSource dataSource() {
		// a named Data Source is best practice for later jmx monitoring
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMinimumIdle(Integer.parseInt(minPool));
		dataSource.setMaximumPoolSize(Integer.parseInt(maxPool));
		return dataSource;
	}

}
