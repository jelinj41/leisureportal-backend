package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.LeisureportalBackendApplication;
import cz.cvut.fel.bp.leisureportalbackend.config.PersistenceConfig;
import cz.cvut.fel.bp.leisureportalbackend.config.ServiceConfig;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "transactionManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = LeisureportalBackendApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private ActivityService activityService;

    Activity activity = new Activity();
    User user = new User();

    @Before
    public void setUp() {

        user.setPassword("123456");
        user.setEmail("jaroslav@gmail.com");
        user.setFirstName("Jaroslav");
        user.setLastName("Jelinek");
        user.setAge(21);
        activity.setAuthor(user);
        userService.createUser(user);

        activity.setId(1);
        activity.setName("Tenis");
        activity.setDescription("Tenis pre dospelych");
        activity.setFromDate(new Date());
        activity.setToDate(new Date());
        activity.setCapacity(20);
        activity.setMin_age(1);
        activity.setMax_age(99);
        activityService.create(activity);

    }

    @Test
    public void findUser_InDatabase(){
        User found = userService.find(user.getId());
        Assert.assertEquals(user,found);
    }
    @Test
    public void findByEmail_InDatabase(){
        User found = userService.findByEmail("jaroslav@gmail.com");
        Assert.assertEquals(user,found);
    }

    @Test
    public void registerForActivity_foundResult(){
        userService.registerForActivity(activity,user);
        Participation par = participationService.findByUserAndActivity(user,activity);
        Assert.assertNotNull(par);
    }

    @Test
    public void exitActivity_NoResultFound(){
        userService.exitActivity(activity,user);
        Assert.assertThrows(EmptyResultDataAccessException.class,()->{participationService.findByUserAndActivity(user,activity);});
    }
}
