package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.ParticipationDao;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Participation
 */
@Service
public class ParticipationService {

    private final ParticipationDao dao;

    public ParticipationService(ParticipationDao dao) {
        this.dao = dao;
    }

    /**
     * Updates a participation.
     *
     * @param participation The participation to update.
     */
    @Transactional
    public void update(Participation participation) {
        Participation orig = dao.findByUserAndActivity(participation.getUser(),participation.getActivity());
        dao.update(orig);
    }

    /**
     * Persists a participation.
     *
     * @param participation The participation to persist.
     */
    @Transactional
    public void persist(Participation participation) {
        dao.persist(participation);
    }

    /**
     * Finds a participation by user and activity.
     *
     * @param user     The user of the participation.
     * @param activity The activity of the participation.
     * @return The found Participation object.
     */
    @Transactional
    public Participation findByUserAndActivity(User user, Activity activity) {
        return dao.findByUserAndActivity(user, activity);
    }

    /**
     * Finds participations by user.
     *
     * @param user The user to search for.
     * @return A list of participations associated with the user.
     */
    public List<Participation> findByUser(User user) {
        return dao.findByUser(user);
    }
}
