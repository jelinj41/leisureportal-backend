package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ActivityDao extends BaseDao<Activity>{

    /**
     * constructor
     */
    public ActivityDao() {
        super(Activity.class);
    }

    /**
     * @param id
     * @return query
     * find - id
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


    public List<Activity> findByCity(Address address){
        Objects.requireNonNull(address);
        return em.createNamedQuery("Activity.findByCity", Activity.class).setParameter("address", address)
                .getResultList();
    }

    public List<Activity> findByCategory(Category category){
        Objects.requireNonNull(category);
        return em.createNamedQuery("Activity.findByCategory", Activity.class).setParameter("category", category)
                .getResultList();
    }

    public List<User> getParticipants(Activity activity){
        Objects.requireNonNull(activity);
        return em.createNamedQuery("Activity.getParticipants", User.class).setParameter("activity", activity)
                .getResultList();
    }

    public List<Participation> getParticipations(Activity activity){
        Objects.requireNonNull(activity);
        return activity.getParticipations();
    }

    public void addResult(Activity activity, User user){
        Objects.requireNonNull(activity);
        Participation par = new Participation(user,activity);
        activity.addParticipation(par);
    }
}
