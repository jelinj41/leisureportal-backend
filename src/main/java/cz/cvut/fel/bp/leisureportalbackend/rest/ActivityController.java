package cz.cvut.fel.bp.leisureportalbackend.rest;

import cz.cvut.fel.bp.leisureportalbackend.exception.NotFoundException;
import cz.cvut.fel.bp.leisureportalbackend.exception.ValidationException;
import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.Category;
import cz.cvut.fel.bp.leisureportalbackend.model.User;
import cz.cvut.fel.bp.leisureportalbackend.rest.util.RestUtils;
import cz.cvut.fel.bp.leisureportalbackend.security.SecurityUtils;
import cz.cvut.fel.bp.leisureportalbackend.service.ActivityService;
import cz.cvut.fel.bp.leisureportalbackend.service.AddressService;
import cz.cvut.fel.bp.leisureportalbackend.service.CategoryService;
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

import java.text.ParseException;
import java.util.List;

/**
 * Controller class for Activity
 */
@RestController
@RequestMapping("/rest/activities")
public class ActivityController {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityController.class);

    private final ActivityService activityService;
    private final UserService userService;

    private final CategoryService categoryService;
    private final AddressService addressService;

    private final SecurityUtils securityUtils;

    @Autowired
    public ActivityController(ActivityService activityService, UserService userService, CategoryService categoryService, AddressService addressService, SecurityUtils securityUtils) {
        this.activityService = activityService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.addressService = addressService;
        this.securityUtils = securityUtils;
    }

    /**
     * Returns all activities.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Activity> getActivities() {
        return activityService.findAll();
    }

    /**
     * Returns the activity with the specified ID.
     *
     * @param id The ID of the activity.
     * @return The activity with the specified ID.
     * @throws NotFoundException If the activity is not found.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Activity getActivityById(@PathVariable Integer id) throws NotFoundException {
        final Activity a = activityService.find(id);
        if (a == null) {
            throw NotFoundException.create("Activity", id);
        }
        return a;
    }

    /**
     * Removes an  activity.
     *
     * @param id The activity id to be removed.
     * @return ResponseEntity with HTTP status 201 CREATED and location header.
     * @throws NotFoundException If there is an error parsing the activity data.
     */
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeActivity(@PathVariable Integer id) throws NotFoundException {
        final Activity original = getActivityById(id);
        activityService.remove(original);
        LOG.debug("Removed activity {}.", original);
    }

    /**
     * Creates a new activity.
     *
     * @param activity The activity to create.
     * @return ResponseEntity with HTTP status 201 CREATED and location header.
     * @throws ParseException If there is an error parsing the activity data.
     */
    @PreAuthorize("hasAnyAuthority('USERTYPE_ADMIN', 'USERTYPE_ORGANIZER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createActivity(@RequestBody Activity activity) throws ParseException {
        activityService.create(activity);
        LOG.debug("Created new activity {}.", activity);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", activity.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Updates an activity.
     *
     * @param id       The ID of the activity to update.
     * @param activity The new activity data.
     * @throws NotFoundException   If the activity is not found.
     * @throws ValidationException If there is a validation error.
     */
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateActivity(@PathVariable Integer id, @RequestBody Activity activity) throws NotFoundException {
        final Activity original = getActivityById(id);
        if (!original.getId().equals(activity.getId())) {
            throw new ValidationException("Race identifier in the data does not match the one in the request URL.");
        }
        activityService.update(activity);
        LOG.debug("Updated race {}.", activity);
    }

    /**
     * Returns the participants in an activity.
     *
     * @param id The ID of the activity.
     * @return The list of participants in the activity.
     * @throws NotFoundException If the activity is not found.
     */
    @GetMapping(value = "/participants/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getParticipantsInActivity(@PathVariable Integer id) throws NotFoundException {
        final Activity a = activityService.find(id);
        if (a == null) {
            throw NotFoundException.create("Activity", id);
        }
        return activityService.getParticipants(a);
    }

    /**
     * Returns activities in a city.
     *
     * @param name The name of the city.
     * @return The list of activities in the city.
     * @throws NotFoundException If the city is not found.
     */
    @GetMapping(value = "/city/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Activity> getActivityByCity(@PathVariable String name) throws NotFoundException {
        Address address = (Address) addressService.findByCity(name);
        if (address == null) {
            throw NotFoundException.create("City", address);
        }
        final List<Activity> a = activityService.findByAddress(address);
        return a;
    }

    /**
     * Returns activities in a category.
     *
     * @param name The name of the category.
     * @return The list of activities in the category.
     * @throws NotFoundException If the category is not found.
     */
    @GetMapping(value = "/category/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Activity> getActivitiesByCategory(@PathVariable String name) throws NotFoundException {
        Category category = (Category) categoryService.findByName(name);
        if (category == null) {
            throw NotFoundException.create("Name", name);
        }
        final List<Activity> c = activityService.findByCategory(category);
        return c;
    }
}
