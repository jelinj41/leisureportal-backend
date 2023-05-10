package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.ParticipationDao;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Participation;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipationService {

    private final ParticipationDao dao;

    public ParticipationService(ParticipationDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void update(Participation participation) {
        Participation orig = dao.findByUserAndActivity(participation.getUser(),participation.getActivity());
        dao.update(orig);
    }

    @Transactional
    public void persist(Participation participation) {
        dao.persist(participation);
    }

    @Transactional
    public Participation findByUserAndActivity(User user, Activity activity) {
        return dao.findByUserAndActivity(user, activity);
    }

    public List<Participation> findByUser(User user) {
        return dao.findByUser(user);
    }
}
