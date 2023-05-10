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

@Service
public class ActivityService {

    private final ActivityDao dao;

    private final ParticipationDao parDao;

    public ActivityService(ActivityDao dao, ParticipationDao parDao) {
        this.dao = dao;
        this.parDao = parDao;
    }

    @Transactional(readOnly = true)
    public Activity find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Activity> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Activity> findByAddress(Address city){
        return dao.findByCity(city);
    }


    @Transactional(readOnly = true)
    public List<Activity> findByCategory(Category category){
        return dao.findByCategory(category);
    }

    @Transactional
    public Activity create(Activity activity) {
        dao.persist(activity);
        return activity;
    }

    @Transactional
    public List<User> getParticipants(Activity activity) {
        return dao.getParticipants(activity);
    }

    @Transactional
    public void update(Activity activity) {
        dao.update(activity);
    }

    @Transactional
    public void remove(Activity activity) {
        dao.remove(activity);
    }
}
