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
public class ParticipationServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private ActivityService activityService;

    User user = new User();

    Activity activity = new Activity();

    Participation participation = new Participation();

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

        participation.setUser(user);
        participation.setActivity(activity);
        activity.addParticipation(participation);
        participationService.persist(participation);
    }

    @Test
    public void findByUserAndActivity_InDatabase(){
        Participation found = participationService.findByUserAndActivity(user,activity);
        Assert.assertEquals(participation,found);
    }

    @Test
    public void findByDriver_foundInDatabase(){
        List<Participation> found = participationService.findByUser(user);
        List<Participation> expectedList = new ArrayList<>();
        expectedList.add(participation);
        Assert.assertEquals(expectedList,found);
    }
}
