package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.AddressDao;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AddressService {

    private final AddressDao dao;

    @Autowired
    public AddressService(AddressDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Address> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Address find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Address> findByCity(String city) {
        return dao.findByCity(city);
    }

    public List<Address> findByUser(User user) {
        Objects.requireNonNull(user);
        return dao.findByUser(user);
    }

    @Transactional
    public void persist(Address address) {
        Objects.requireNonNull(address);
        dao.persist(address);
    }

    @Transactional
    public void remove(Address address) {
        Objects.requireNonNull(address);
        dao.remove(address);
    }
    @Transactional
    public void update(Address address) {
        dao.update(address);
    }
}
