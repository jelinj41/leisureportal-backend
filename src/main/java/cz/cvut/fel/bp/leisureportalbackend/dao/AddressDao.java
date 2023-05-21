package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Dao for Address class
 */
@Repository
public class AddressDao extends BaseDao<Address> {

    /**
     * constructor
     */
    public AddressDao() {

        super(Address.class);
    }

    /**
     * Finds addresses by city.
     *
     * @param city The city to search for.
     * @return A list of addresses in the specified city.
     */
    public List<Address> findByCity(String city){
        Objects.requireNonNull(city);
        return em.createNamedQuery("Address.findByCity", Address.class).setParameter("city", city)
                .getResultList();
    }

    /**
     * Finds addresses by user.
     *
     * @param user The user whose addresses are to be found.
     * @return A list of addresses associated with the specified user.
     */
    public List<Address> findByUser(User user){
        Objects.requireNonNull(user);
        return em.createNamedQuery("Address.findByUser", Address.class).setParameter("user", user)
                .getResultList();
    }
}
