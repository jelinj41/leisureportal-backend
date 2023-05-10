package cz.cvut.fel.bp.leisureportalbackend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "cz.cvut.fel.bp.leisureportalbackend.service")
public class ServiceConfig {
}
