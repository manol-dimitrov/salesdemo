package org.sdl.salesdemo.config;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan({"org.sdl.salesdemo.*"})
@PropertySource("classpath:application.properties")
@EnableTransactionManagement

/**
 * The following class is the Sales Tax application configuration class it
 * will configure the database beans (Session Factory and load the hibernate
 * properties)
 * 
 */
public class AppConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private Environment env;
    
    /**
     * The following method will return the session factory.  It
     * will also configure the session builder to scan the domain
     * package to load the entities
     * 
     * @return SessionFactory
     */
    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder
                .scanPackages("org.sdl.salesdemo.domain")
                .addProperties(getHibernateProperties());

        return builder.buildSessionFactory();
    }

    /**
     * The following method will load the hibernate properties
     * 
     * @return Properties 
     */
    private Properties getHibernateProperties() {

        Properties prop = new Properties();
        prop.put("hibernate.show_sql", "true");
        prop.put("hbm2ddl.auto", "true");
        prop.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        return prop;
    }

    
    /**
     * The following method will load the basic data source.  It will set
     * up the data source connection to use a derby in memory db
     * @return BasicDataSource
     */
    @Bean(name = "dataSource")
    public BasicDataSource dataSource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        
        ds.setUrl( env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.user"));
        ds.setPassword(env.getProperty("db.password"));

        return ds;
    }

    
    /**
     * The following method will return the Hibernate Transaction Manager
     * @return HibernateTransactionManager
     */
    @Bean
    public HibernateTransactionManager txManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    
    /**
     * The following is the internal resource view resolver.  It will map 
     * view requests to jsp pages in the /WEB-INF/pages directory
     * 
     * @return InternalResourceViewResolver
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    
    @Bean
    public SalesTaxDatabaseConfig createDBConfig(){
        SalesTaxDatabaseConfig initService;
        initService = new SalesTaxDatabaseConfig( dataSource(), sessionFactory());
        
        return initService;
        
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }
    
    


}
