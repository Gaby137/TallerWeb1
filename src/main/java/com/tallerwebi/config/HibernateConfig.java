package com.tallerwebi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver"); /*poner motor de base de datos*/
        dataSource.setUrl("jdbc:hsqldb:mem:db_"); /*iria localhost:3306*/
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    /*sesion principal de hybernate q nos deja comunicarnos con la bdd*/
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) { /*spring crea una sesion*/
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.tallerwebi.dominio"); /*va a buscar los @Entity*/
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory(dataSource()).getObject());
    } /*se manejan errores q puedan aparecer en la transaccion (si se cumplen todos los pasos se hace el commit sino no se hace nada)*/

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect"); /*cambiar segun el moto de bdd q usemos*/
        properties.setProperty("hibernate.show_sql", "true"); /*permite mostrar en pantalla (ponerlo en false mientras desarrollemos)*/
        properties.setProperty("hibernate.format_sql", "true"); /*permite mostrar en pantalla (ponerlo en false mientras desarrollemos)*/
        properties.setProperty("hibernate.hbm2ddl.auto", "create"); /*q va a hacer hybernate con el esquema de bdd todo lo q tenga @Entity lo va a transofrmar en una tabla,
        borra todo lo que haya cargado en la bdd y muestra lo mas nuevo hecho, en modo produccion poner en NONE*/
        return properties;
    }
}
