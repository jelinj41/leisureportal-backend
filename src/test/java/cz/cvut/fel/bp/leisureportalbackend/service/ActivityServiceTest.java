package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.LeisureportalBackendApplication;
import cz.cvut.fel.bp.leisureportalbackend.config.PersistenceConfig;
import cz.cvut.fel.bp.leisureportalbackend.config.ServiceConfig;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
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
public class ActivityServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private ActivityService activityService;


    Address address = new Address();

    User user = new User();

    User organizer = new User();

    Activity activity = new Activity();

    Participation participation = new Participation();

    @Before
    public void setUp(){
        address.setId(1);
        address.setCity("Bardejov");
        address.setStreet("Slovenská");
        address.setHouseNumber(22);
        address.setZipCode("08501");
        address.setCountry("Slovensko");
        addressService.persist(address);

        user.setPassword("123456");
        user.setEmail("jaroslav@gmail.com");
        user.setFirstName("Jaroslav");
        user.setLastName("Jelinek");
        user.setAge(21);
        userService.createUser(user);

        organizer.setPassword("123456");
        organizer.setEmail("timik@gmail.com");
        organizer.setFirstName("Timik");
        organizer.setLastName("Barbus");
        organizer.setAge(69);
        activity.setAuthor(organizer);
        userService.createUser(organizer);

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
    public void newActivity_InDatabase(){
        Activity foundActivity = activityService.find(1);
        Assert.assertEquals(activity,foundActivity);
    }

    /*
    @Test
    public void findRaceByCircuit_findsRace(){
        List<Activity> foundActivity = activityService.findByAddress(address);
        List<Activity> activityList = new ArrayList<>();
        activityList.add(activity);
        Assert.assertEquals(activityList,foundActivity);
    }
    */

    @Test
    public void getParticipants_returnsArray(){
        List<User> participants = activityService.getParticipants(activity);
        List<User> expected = new ArrayList<>();
        expected.add(user);
        Assert.assertEquals(expected,participants);
    }

    @Test
    public void updateName_ValuesChange(){
        activity.setName("Stolny tenis");
        activityService.update(activity);
        Activity findActivity = activityService.find(1);
        Assert.assertEquals("Stolny tenis",findActivity.getName());
    }

    @Test
    public void removeActivity_NotFound(){
        activityService.remove(activity);
        Activity findActivity = activityService.find(1);
        Assert.assertEquals(null,findActivity);
    }
}
