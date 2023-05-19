package cz.cvut.fel.bp.leisureportalbackend.model;

/**
 * Model for activity
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ACTIVITY")
@NamedQueries({
        @NamedQuery(name = "Activity.findByStringId", query = "SELECT t FROM Activity t WHERE t.name = :id"),
        @NamedQuery(name = "Activity.getParticipants", query = "SELECT u from User u join Participation par on u = par.user where par.activity = :activity"),
        @NamedQuery(name = "Activity.getByUser", query = "SELECT a from Activity a join Participation par on a = par.activity where par.user = :user"),
        @NamedQuery(name = "Activity.findByCity", query = "SELECT a from Activity a WHERE a.address = :city"),
        @NamedQuery(name = "Activity.findByCategory", query = "SELECT a from Activity a WHERE a.category = :category"),
        })
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Activity extends AbstractEntity{

    @Id
    @GeneratedValue
    @Column(name = "ACTIVITY_ID")
    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 255, min = 3, message = "Name has to be from 3 to 255 characters.")
    @NotBlank(message = "Name has to be from 3 to 255 characters.")
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 3000, min = 0, message = "Max 3000 characters.")
    private String description;

    @ManyToOne
    private Address address;

    @Basic(optional = false)
    @Column(nullable = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 10000, message = "Max 10 000")
    private double price;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 100000, message = "Max 100 000")
    private Integer capacity;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 150, message = "Max 150")
    private Integer min_age;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 150, message = "Max 150")
    private Integer max_age;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false, columnDefinition = "date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date fromDate;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false, columnDefinition = "date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date toDate;

    @ManyToOne
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy="activity", cascade = CascadeType.REMOVE)
    private List<Participation> participations_activity;

    public Activity(){
    }

    /**
     * @param name
     * @param description
     * @param price
     * @param capacity
     * @param min_age
     * @param max_age
     * @param address
     * @param author
     * constructor
     */
    public Activity(@Size(max = 255, min = 3, message = "Name has to be from 3 to 255 characters.")  @NotBlank(message = "Name has to be from 3 to 255 characters.") String name,
                    @Size(max = 3000, min = 0, message = "Max 3000 characters.") String description,
                    @Min(value = 0, message = "Min 0") @Max(value = 10000, message = "Max 10 000") double price,
                    @Min(value = 0, message = "Min 0") @Max(value = 10000, message = "Max 100 000") Integer capacity,
                    @Min(value = 0, message = "Min 0") @Max(value = 150, message = "Max 150") Integer min_age,
                    @Min(value = 0, message = "Min 0") @Max(value = 150, message = "Max 150") Integer max_age,
                    Address address, Category category,
                    User author
    ) {
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.address= address;
        this.category = category;
        this.min_age = min_age;
        this.max_age = max_age;
        this.author = author;
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
     * @return get description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     * set description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return get address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @return get price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price
     * set price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getMin_age() {
        return min_age;
    }

    public void setMin_age(Integer min_age) {
        this.min_age = min_age;
    }

    public Integer getMax_age() {
        return max_age;
    }

    public void setMax_age(Integer max_age) {
        this.max_age = max_age;
    }

    /**
     * @return get price
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * @param capacity
     * set price
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @return get author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author
     * set author
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * @return get fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate
     * set fromDate
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return get toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate
     * set toDate
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return get category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category
     * set category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    public void addParticipation(Participation par){
        if(participations_activity == null){
            participations_activity = new ArrayList<Participation>();
            participations_activity.add(par);
            return;
        }
        Objects.requireNonNull(par);
        participations_activity.add(par);
    }

    public void removeParticipation(Participation par){
        Objects.requireNonNull(par);
        if(participations_activity == null){
            return;
        }
        participations_activity.remove(par);
    }

    public List<Participation> getParticipations() {
        return participations_activity;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "Offer{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", salary=" + price +
                ", location='" + address + '\'' +
                ", min age" + min_age +
                ", max age" + max_age +
                ", from date=" + fromDate +
                ", to date=" + toDate +
                ", category=" + category +
                '}';
    }
}
