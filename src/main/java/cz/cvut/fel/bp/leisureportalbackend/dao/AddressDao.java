package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Dao for Address - street, number, city, state...
 */
@Repository
public class AddressDao extends BaseDao<Address> {

    /**
     * constructor
     */
    public AddressDao() {

        super(Address.class);
    }

    public List<Address> findByCity(String city){
        Objects.requireNonNull(city);
        return em.createNamedQuery("Address.findByCity", Address.class).setParameter("city", city)
                .getResultList();
    }

    public List<Address> findByUser(User user){
        Objects.requireNonNull(user);
        return em.createNamedQuery("Address.findByUser", Address.class).setParameter("user", user)
                .getResultList();
    }
}
