package cz.cvut.fel.bp.leisureportalbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for user types in the system - user, admin, organizer, guest
 */
public enum UserType {

    @JsonProperty("guest")
    GUEST("USERTYPE_GUEST"),

    @JsonProperty("user")
    USER("USERTYPE_USER"),

    @JsonProperty("admin")
    ADMIN("USERTYPE_ADMIN"),

    @JsonProperty("organizer")
    ORGANIZER("USERTYPE_ORGANIZER");

    private final String type;

    /**
     * @param type
     * constructor
     */
    UserType(String type) {
        this.type = type;
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        return type;
    }

}
