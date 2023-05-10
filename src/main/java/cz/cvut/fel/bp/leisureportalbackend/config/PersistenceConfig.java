package cz.cvut.fel.bp.leisureportalbackend.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties({JpaProperties.class})
@Configuration
@EnableJpaRepositories("cz.cvut.fel.bp.leisureportalbackend.dao")
@PropertySource("classpath:eclipselink.properties")     // Contains additional, Eclipselink-specific configuration
public class PersistenceConfig extends JpaBaseConfiguration {

    private final Environment environment;

    protected PersistenceConfig(DataSource dataSource,
                                JpaProperties properties,
                                ObjectProvider<JtaTransactionManager> jtaTransactionManager,
                                Environment environment) {
        super(dataSource, properties, jtaTransactionManager);
        this.environment = environment;
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        final Map<String, Object> props = new HashMap<>();
        props.put("eclipselink.weaving", "static");
        props.put("eclipselink.ddl-generation", environment.getRequiredProperty("eclipselink.ddl-generation"));
        return props;
    }
}
