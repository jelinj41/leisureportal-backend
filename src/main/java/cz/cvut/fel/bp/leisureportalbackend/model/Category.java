package cz.cvut.fel.bp.leisureportalbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

    public Category() {
    }

    public Category(@NotBlank(message = "Name of category cannot be blank") String name) {
        this.name = name;
        this.activities = new ArrayList<>();
    }

    public boolean add(Activity activity){
        return activities.add(activity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public boolean addActivity(Activity activity) {
        if (activity != null && !activities.contains(activity)) {
            activities.add(activity);
            activity.setCategory(this);
            return true;
        }
        return false;
    }

    public boolean removeActivity(Activity activity) {
        if (activity != null && activities.contains(activity)) {
            activities.remove(activity);
            activity.setCategory(null);
            return true;
        }
        return false;
    }
}
