package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.AddressDao;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for Address
 */
@Service
public class AddressService {

    private final AddressDao dao;

    @Autowired
    public AddressService(AddressDao dao) {
        this.dao = dao;
    }

    /**
     * Retrieves all addresses.
     *
     * @return A list of all addresses.
     */
    @Transactional(readOnly = true)
    public List<Address> findAll() {
        return dao.findAll();
    }

    /**
     * Finds an address by its ID.
     *
     * @param id The ID of the address.
     * @return The found Address object, or null if not found.
     */
    @Transactional(readOnly = true)
    public Address find(Integer id) {
        return dao.find(id);
    }

    /**
     * Finds addresses by city.
     *
     * @param city The city to search for.
     * @return A list of addresses in the specified city.
     */
    @Transactional(readOnly = true)
    public List<Address> findByCity(String city) {
        return dao.findByCity(city);
    }

    /**
     * Finds addresses associated with a user.
     *
     * @param user The user to find addresses for.
     * @return A list of addresses associated with the user.
     * @throws NullPointerException if the user is null.
     */
    public List<Address> findByUser(User user) {
        Objects.requireNonNull(user);
        return dao.findByUser(user);
    }

    /**
     * Persists an address.
     *
     * @param address The address to persist.
     * @throws NullPointerException if the address is null.
     */
    @Transactional
    public void persist(Address address) {
        Objects.requireNonNull(address);
        dao.persist(address);
    }

    /**
     * Removes an address.
     *
     * @param address The address to remove.
     * @throws NullPointerException if the address is null.
     */
    @Transactional
    public void remove(Address address) {
        Objects.requireNonNull(address);
        dao.remove(address);
    }

    /**
     * Updates an address.
     *
     * @param address The address to update.
     */
    @Transactional
    public void update(Address address) {
        dao.update(address);
    }
}
