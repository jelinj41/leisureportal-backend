package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Dao for Participation class
 */
@Repository
public class ParticipationDao extends BaseDao<Participation> {

    /**
     * constructor
     */
    public ParticipationDao() {
        super(Participation.class);
    }

    /**
     * Finds a participation record by the user and activity.
     *
     * @param participant The participant user.
     * @param activity    The activity.
     * @return The participation record for the user and activity.
     * @throws NullPointerException if the participant or activity is null.
     */
    public Participation findByUserAndActivity(User participant, Activity activity){
        Objects.requireNonNull(participant);
        Objects.requireNonNull(activity);
        return em.createNamedQuery("Participation.findByUserAndActivity", Participation.class).setParameter("participant", participant).setParameter("activity", activity)
                .getSingleResult();
    }

    /**
     * Finds participation records by the user.
     *
     * @param participant The participant user.
     * @return A list of participation records for the user.
     * @throws NullPointerException if the participant is null.
     */
    public List<Participation> findByUser(User participant){
        Objects.requireNonNull(participant);
        return em.createNamedQuery("Participation.findByUser", Participation.class).setParameter("participant", participant)
                .getResultList();
    }
}
