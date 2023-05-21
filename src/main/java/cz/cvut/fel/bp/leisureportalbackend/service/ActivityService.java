package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.ActivityDao;
import cz.cvut.fel.bp.leisureportalbackend.dao.ParticipationDao;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.Category;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Activity
 */
@Service
public class ActivityService {

    private final ActivityDao dao;

    private final ParticipationDao parDao;

    public ActivityService(ActivityDao dao, ParticipationDao parDao) {
        this.dao = dao;
        this.parDao = parDao;
    }

    /**
     * Finds an activity by its ID.
     *
     * @param id The ID of the activity.
     * @return The found Activity object, or null if not found.
     */
    @Transactional(readOnly = true)
    public Activity find(Integer id) {
        return dao.find(id);
    }

    /**
     * Retrieves all activities.
     *
     * @return A list of all activities.
     */
    @Transactional(readOnly = true)
    public List<Activity> findAll() {
        return dao.findAll();
    }

    /**
     * Finds activities by address (city).
     *
     * @param city The city address to search for.
     * @return A list of activities found in the specified city.
     */
    @Transactional(readOnly = true)
    public List<Activity> findByAddress(Address city){
        return dao.findByCity(city);
    }

    /**
     * Finds activities by category.
     *
     * @param category The category to search for.
     * @return A list of activities in the specified category.
     */
    @Transactional(readOnly = true)
    public List<Activity> findByCategory(Category category){
        return dao.findByCategory(category);
    }

    /**
     * Creates a new activity.
     *
     * @param activity The activity to create.
     * @return The created Activity object.
     */
    @Transactional
    public Activity create(Activity activity) {
        dao.persist(activity);
        return activity;
    }

    /**
     * Retrieves the participants of an activity.
     *
     * @param activity The activity to retrieve participants for.
     * @return A list of users who participate in the activity.
     */
    @Transactional
    public List<User> getParticipants(Activity activity) {
        return dao.getParticipants(activity);
    }

    /**
     * Updates an existing activity.
     *
     * @param activity The activity to update.
     */
    @Transactional
    public void update(Activity activity) {
        dao.update(activity);
    }

    /**
     * Removes an activity.
     *
     * @param activity The activity to remove.
     */
    @Transactional
    public void remove(Activity activity) {
        dao.remove(activity);
    }
}
