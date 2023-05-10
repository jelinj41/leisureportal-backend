package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.ActivityDao;
import cz.cvut.fel.bp.leisureportalbackend.dao.ParticipationDao;
import cz.cvut.fel.bp.leisureportalbackend.dao.UserDao;
import cz.cvut.fel.bp.leisureportalbackend.exception.BadPassword;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import cz.cvut.fel.bp.leisureportalbackend.model.UserType;
import cz.cvut.fel.bp.leisureportalbackend.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserDao dao;

    private final ActivityDao activityDao;

    private final ParticipationDao parDao;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserDao dao, ActivityDao activityDao, ParticipationDao parDao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.activityDao = activityDao;
        this.parDao = parDao;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    public User find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Transactional
    public void createUser(User user) {
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (user.getType() == null) {
            user.setType(Constants.DEFAULT_ROLE);
        }
        dao.persist(user);

    }


    @Transactional
    public boolean registerForActivity(Activity activity, User user) {
        Objects.requireNonNull(activity);
        Objects.requireNonNull(user);
        List<User> participants = activityDao.getParticipants(activity);
        for (User user1 : participants) {
            LOG.debug(user1.getEmail());
            if (user1.getEmail().equals(user.getEmail())) {
                LOG.info("User {} is already registered for the activity.", user);
                return false;
            }
        }
        if(user.getAge() <= activity.getMax_age() &&  user.getAge() >= activity.getMin_age()){
            activity.addParticipation(new Participation(user, activity));
            activityDao.update(activity);
            return true;
        }else {
            LOG.info("User {} {} doesnt have the right age", user.getAge(), activity.getMin_age(), activity.getMax_age());
            return false;
        }
    }

    @Transactional
    public boolean exitActivity(Activity activity, User user) {
        Objects.requireNonNull(activity);
        Objects.requireNonNull(user);

        boolean participating = false;
        List<User> participants = activityDao.getParticipants(activity);

        for(User u : participants){
            LOG.debug(u.getEmail() + " is participating in the activity");
            if(u.getId().equals(user.getId())){
                participating = true;
                break;
            }
        }

        if(!participating){
            LOG.info("User {} is not participating in the activity.", user);
            return false;
        }

        Participation par = parDao.findByUserAndActivity(user, activity);
        LOG.info(par.getUser().getEmail() + " " + par.getActivity().getName());
        activity.removeParticipation(par);
        activityDao.update(activity);
        parDao.remove(par);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean exists(String email) {
        return dao.findByEmail(email) != null;
    }

    @Transactional(readOnly = true)
    public void remove(User user) {
        dao.remove(user);
    }

    @Transactional
    public void update(User user) {
        dao.update(user);
    }
}
