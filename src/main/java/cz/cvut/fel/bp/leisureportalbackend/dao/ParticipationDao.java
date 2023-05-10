package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ParticipationDao extends BaseDao<Participation> {

    public ParticipationDao() {
        super(Participation.class);
    }

    public Participation findByUserAndActivity(User participant, Activity activity){
        Objects.requireNonNull(participant);
        Objects.requireNonNull(activity);
        return em.createNamedQuery("Participation.findByUserAndActivity", Participation.class).setParameter("participant", participant).setParameter("activity", activity)
                .getSingleResult();
    }
    public List<Participation> findByUser(User participant){
        Objects.requireNonNull(participant);
        return em.createNamedQuery("Participation.findByUser", Participation.class).setParameter("participant", participant)
                .getResultList();
    }
}
