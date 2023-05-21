package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Dao of the Activity class
 */
@Repository
public class ActivityDao extends BaseDao<Activity>{

    /**
     * constructor
     */
    public ActivityDao() {
        super(Activity.class);
    }

    /**
     * Finds an activity by its ID.
     *
     * @param id The ID of the activity.
     * @return The activity with the specified ID, or null if not found.
     */
    public Activity find(String id){
        {
            try {
                return em.createNamedQuery("Activity.findByStringId", Activity.class).setParameter("id", id)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    /**
     * Finds activities by city.
     *
     * @param address The address representing the city.
     * @return A list of activities in the specified city.
     */
    public List<Activity> findByCity(Address address){
        Objects.requireNonNull(address);
        return em.createNamedQuery("Activity.findByCity", Activity.class).setParameter("address", address)
                .getResultList();
    }

    /**
     * Finds activities by category.
     *
     * @param category The category of the activities.
     * @return A list of activities in the specified category.
     */
    public List<Activity> findByCategory(Category category){
        Objects.requireNonNull(category);
        return em.createNamedQuery("Activity.findByCategory", Activity.class).setParameter("category", category)
                .getResultList();
    }

    /**
     * Gets the participants of an activity.
     *
     * @param activity The activity.
     * @return A list of users who are participants of the activity.
     */
    public List<User> getParticipants(Activity activity){
        Objects.requireNonNull(activity);
        return em.createNamedQuery("Activity.getParticipants", User.class).setParameter("activity", activity)
                .getResultList();
    }

    /**
     * Gets the participations of an activity.
     *
     * @param activity The activity.
     * @return A list of participations in the activity.
     */
    public List<Participation> getParticipations(Activity activity){
        Objects.requireNonNull(activity);
        return activity.getParticipations();
    }

    /**
     * Adds a result for an activity and a user.
     *
     * @param activity The activity.
     * @param user The user.
     */
    public void addResult(Activity activity, User user){
        Objects.requireNonNull(activity);
        Participation par = new Participation(user,activity);
        activity.addParticipation(par);
    }
}
