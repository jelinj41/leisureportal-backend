package cz.cvut.fel.bp.leisureportalbackend.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Model for addresses
 */
@Entity
@Table(name = "ADDRESS")
@NamedQueries({
        @NamedQuery(name = "Address.findByCity", query = "SELECT a from Address a WHERE a.city = :city"),
        @NamedQuery(name = "Address.findByUser", query = "SELECT a from Address a WHERE a.author = :user")

})
public class Address extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 100, min = 1)
    @NotBlank(message = "City cannot be blank")
    private String city;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 100, min = 1)
    @NotBlank(message = "Street cannot be blank")
    private String street;

    @Basic(optional = false)
    @Column(nullable = false)
    private int houseNumber;

    @Basic(optional = false)
    @Column(nullable = false)
    @NotBlank(message = "ZIP code cannot be blank")
    @Size(max = 5, min = 5)
    private String zipCode;

    @Basic(optional = false)
    @Column(nullable = false)
    @NotBlank(message = "Country cannot be blank")
    private String country;

    @ManyToOne
    private User author;

    /**
     * @param city
     * @param street
     * @param houseNumber
     * @param zipCode
     * @param country
     * constructor
     */
    /*
    public Address(@NotBlank(message = "City cannot be blank") String city,
                   @NotBlank(message = "Street cannot be blank") String street,
                   int houseNumber,
                   @NotBlank(message = "ZIP code cannot be blank") String zipCode,
                   @NotBlank(message = "Country cannot be blank") String country) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.country = country;
    }
    */


    /**
     * constructor
     */
    public Address() {

    }

    /**
     * @return get city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return get street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @return get house number
     */
    public int getHouseNumber() {
        return houseNumber;
    }

    /**
     * @return get zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @return get country
     */
    public String getCountry() {
        return country;
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
     * @param city
     * set city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @param street
     * set street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @param houseNumber
     * set house number
     */
    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * @param zipCode
     * set zip code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @param country
     * set country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber=" + houseNumber +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
