package cz.cvut.fel.bp.leisureportalbackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for user
 */
@Entity
@Table(name = "APP_USER")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})

public class User{

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Integer id;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {this.id = id;}


    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    @Size(max = 30, min = 1, message = "First name is in incorrect format.")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 30, min = 1, message = "Last name is in incorrect format.")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer age;

    @Email(message = "Email should be valid")
    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 255, min = 6, message = "Password is in incorrect format.")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Participation> participations_user;

    /**
     * constructor
     */
    public User() {
        this.type = UserType.GUEST;
    }

    /**
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * constructor
     */
    public User(String password, String firstName, String lastName, String email) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email= email;
        participations_user = new ArrayList<>();
    }

    /**
     * @return get role
     */
    public UserType getType() {
        return type;
    }

    /**
     * @param type
     * set role
     */
    public void setType(UserType type) {
        this.type = type;
    }

    /**
     * @return get first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     * set first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return get last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     * set last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return get age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     * set age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @param email
     * set email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return get password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     * set password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * erase password
     */
    public void erasePassword() {
        this.password = null;
    }

    /**
     * @return get email
     */
    public String getEmail() {
        return email;
    }

    /**
     * encode password
     */
    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    /**
     * @return get joined activities
     */
    public List<Participation> getParticipations_user() {
        return participations_user;
    }

    /**
     * @param participations_user
     * set joined activities
     */
    public void setParticipations_user(List<Participation> participations_user) {
        this.participations_user = participations_user;
    }

    public boolean isAdmin() {
        return type == UserType.ADMIN;
    }

    public boolean isUser() {
        return type == UserType.USER;
    }

    public boolean isOrganizor() {
        return type == UserType.ORGANIZER;
    }

    public boolean isGuest() {
        return type == UserType.GUEST;
    }
}
