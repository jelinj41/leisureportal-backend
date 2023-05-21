package cz.cvut.fel.bp.leisureportalbackend.rest;

import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import cz.cvut.fel.bp.leisureportalbackend.model.UserType;
import cz.cvut.fel.bp.leisureportalbackend.rest.util.RestUtils;
import cz.cvut.fel.bp.leisureportalbackend.security.SecurityUtils;
import cz.cvut.fel.bp.leisureportalbackend.service.ActivityService;
import cz.cvut.fel.bp.leisureportalbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.security.Principal;

/**
 * Controller class for User
 */
@RestController
@RequestMapping("/rest/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ActivityService activityService;
    private final SecurityUtils securityUtils;

    @Autowired
    public UserController(UserService userService, ActivityService activityService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.activityService = activityService;
        this.securityUtils = securityUtils;
    }

    /**
     * Registers a new user.
     *
     * @param user The JSON representation of the user to register.
     * @return ResponseEntity with a status of 201.
     */
    @PreAuthorize("(!#user.isAdmin() && anonymous) || hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.createUser(user);
        LOG.debug("User {} successfully registered.", user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Registers the current user for an Activity.
     *
     * @param id The ID of the Activity to register for.
     */
    @PreAuthorize("hasAnyAuthority('USERTYPE_USER', 'USERTYPE_ORGANIZER') " )
    @PostMapping(value = "/registerForActivity/{id}")
    public void registerForActivity(@PathVariable Integer id) {
        User user = this.getCurrent();
        Activity activity = activityService.find(id);
        boolean success = userService.registerForActivity(activity,user);
        if(success){
            LOG.debug("User {} successfully registered for activity {}", user, activity);
        }
    }

    /**
     * Returns the current user.
     *
     * @return The current User.
     */
    //@PermitAll
    @PreAuthorize("hasAnyAuthority('USERTYPE_ADMIN', 'USERTYPE_USER', 'USERTYPE_GUEST', 'USERTYPE_ORGANIZER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent() {
        final User user = userService.find(securityUtils.getCurrentUser().getId());
        return user;
    }

    /**
     * Checks if a user is logged in.
     *
     * @param principal The Principal object representing the current user.
     * @return true if the user is logged in, false otherwise.
     */
    @PermitAll
    @GetMapping(value = "/isLoggedIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isLoggedIn(Principal principal) {
        boolean isLoggedIn =(principal!=null) ? true : false;
        return isLoggedIn;
    }

    /**
     * Retrieves a User by its ID.
     *
     * @param id The ID of the User.
     * @return The retrieved User.
     */
    @PermitAll
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Integer id) {
        User user = userService.find(id);
        return user;
    }

    /**
     * Allows a user to exit an Activity.
     *
     * @param id The ID of the Activity to exit.
     */
    @PreAuthorize("hasAnyAuthority('USERTYPE_USER', 'USERTYPE_ORGANIZER')")
    @PostMapping(value = "/exit/{id}")
    public void exitActivity(@PathVariable Integer id) {
        final User user = userService.find(getCurrent().getId());
        final Activity activity = activityService.find(id);
        boolean success = false;
        success = userService.exitActivity(activity,user);
        if(success){
            LOG.debug("User {} successfully exited activity {}", user, activity);
        }
    }

    /**
     * Updates a User by its ID.
     *
     * @param id   The ID of the User.
     * @param user The JSON representation of the updated User.
     * @return ResponseEntity with a status of 200 if the update was successful, or appropriate error responses.
     */
    @PermitAll
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User current = securityUtils.getCurrentUser();
        User original_user = userService.find(id);

        if(original_user == null){
            LOG.error("User not found.");
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
        if(current.getId() != id && current.getType() != UserType.ADMIN){
            LOG.error("Not updating current user.");
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
        }

        user.setPassword(original_user.getPassword());
        user.setType(original_user.getType());
        userService.update(user);

        LOG.info("Updated user info for {}.", original_user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    /**
     * Removes a User by its ID.
     *
     * @param id The ID of the User to remove.
     * @return ResponseEntity with a status of 200 if the removal was successful, or appropriate error responses.
     */
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/remove/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer id) {
        User user = userService.find(id);
        if(user == null){
            LOG.error("User not found.");
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
        else{
            userService.remove(user);
            LOG.info("Removed user {}.",user);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }
}
