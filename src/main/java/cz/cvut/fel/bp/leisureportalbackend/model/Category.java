package cz.cvut.fel.bp.leisureportalbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for categories
 */
@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false, length = 30, unique = true)
    @NotBlank(message = "Name of category cannot be blank")
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Activity> activities;

    /**
     * constructor
     */
    public Category() {
    }

    public Category(@NotBlank(message = "Name of category cannot be blank") String name) {
        this.name = name;
        this.activities = new ArrayList<>();
    }

    /**
     * Adds an activity to the category.
     *
     * @param activity The activity to be added.
     * @return true if the activity is added successfully, false otherwise.
     */
    public boolean add(Activity activity){
        return activities.add(activity);
    }

    /**
     * @return get name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * set name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return get activities
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * Adds an activity to the category.
     *
     * @param activity The activity to be added.
     * @return true if the activity is added successfully, false otherwise.
     */
    public boolean addActivity(Activity activity) {
        if (activity != null && !activities.contains(activity)) {
            activities.add(activity);
            activity.setCategory(this);
            return true;
        }
        return false;
    }

    /**
     * Removes an activity from the category.
     *
     * @param activity The activity to be removed.
     * @return true if the activity is removed successfully, false otherwise.
     */
    public boolean removeActivity(Activity activity) {
        if (activity != null && activities.contains(activity)) {
            activities.remove(activity);
            activity.setCategory(null);
            return true;
        }
        return false;
    }
}
