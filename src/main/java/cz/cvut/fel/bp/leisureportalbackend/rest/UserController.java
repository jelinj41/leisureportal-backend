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

    @PreAuthorize("(!#user.isAdmin() && anonymous) || hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.createUser(user);
        LOG.debug("User {} successfully registered.", user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Registers current user for Activity.
     *
     * @param id Race id
     */
    @PreAuthorize("hasAnyAuthority('USERTYPE_USER')")
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
     * Return current user.
     */
    //@PermitAll
    @PreAuthorize("hasAnyAuthority('USERTYPE_ADMIN', 'USERTYPE_USER', 'USERTYPE_GUEST', 'USERTYPE_ORGANIZER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent() {
        final User user = userService.find(securityUtils.getCurrentUser().getId());
        return user;
    }

    @PermitAll
    @GetMapping(value = "/isLoggedIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isLoggedIn(Principal principal) {
        boolean isLoggedIn =(principal!=null) ? true : false;
        return isLoggedIn;
    }

    @PermitAll
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Integer id) {
        User user = userService.find(id);
        return user;
    }

    @PreAuthorize("hasAnyAuthority('USERTYPE_USER')")
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
