package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.model.User;
import cz.cvut.fel.bp.leisureportalbackend.model.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

@Component
public class SystemInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(SystemInitializer.class);

    /**
     * Default admin username
     */
    private static final String ADMIN_USERNAME = "admin";

    private final UserService userService;

    private final PlatformTransactionManager txManager;

    @Autowired
    public SystemInitializer(UserService userService,
                             PlatformTransactionManager txManager) {
        this.userService = userService;
        this.txManager = txManager;
    }

    @PostConstruct
    private void initSystem() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute((status) -> {
            //generateAdmin();
            return null;
        });
    }


    /**
     * Generates an admin account if it does not already exist.
     */

    /*
    private void generateAdmin() {
        if (userService.exists(ADMIN_USERNAME)) {
            return;
        }
        final User admin = new User();
        admin.setEmail(ADMIN_USERNAME);
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setPassword("adm1n");
        admin.setAge(0);
        admin.setEmail("none");
        admin.setType(UserType.ADMIN);
        LOG.info("Generated admin user with credentials " + admin.getEmail() + "/" + admin.getPassword());
        userService.createUser(admin);
    }
     */
}
