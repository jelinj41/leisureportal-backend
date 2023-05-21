package cz.cvut.fel.bp.leisureportalbackend.service.security;

import cz.cvut.fel.bp.leisureportalbackend.dao.UserDao;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service handling user details
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Loads the user details by username.
     *
     * @param username The username of the user to load.
     * @return The UserDetails object representing the user.
     * @throws UsernameNotFoundException If the user with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userDao.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }

        return new cz.cvut.fel.bp.leisureportalbackend.security.model.UserDetails(user);
    }
}
