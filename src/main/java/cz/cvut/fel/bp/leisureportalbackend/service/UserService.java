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

/**
 * Service for User
 */
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

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user.
     * @return The found User object.
     */
    @Transactional(readOnly = true)
    public User find(Integer id) {
        return dao.find(id);
    }

    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The found User object.
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return dao.findByEmail(email);
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     */
    @Transactional
    public void createUser(User user) {
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (user.getType() == null) {
            user.setType(Constants.DEFAULT_ROLE);
        }
        dao.persist(user);

    }

    /**
     * Registers a user for an activity.
     *
     * @param activity The activity to register for.
     * @param user     The user to register.
     * @return True if the registration was successful, false otherwise.
     */
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

    /**
     * Removes a user from an activity.
     *
     * @param activity The activity to remove the user from.
     * @param user     The user to remove.
     * @return True if the user was successfully removed, false otherwise.
     */
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

    /**
     * Checks if a user with the specified email already exists.
     *
     * @param email The email to check.
     * @return True if a user with the email exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean exists(String email) {
        return dao.findByEmail(email) != null;
    }

    /**
     * Removes a user.
     *
     * @param user The user to remove.
     */
    @Transactional(readOnly = true)
    public void remove(User user) {
        dao.remove(user);
    }

    /**
     * Updates a user.
     *
     * @param user The user to update.
     */
    @Transactional
    public void update(User user) {
        dao.update(user);
    }
}
