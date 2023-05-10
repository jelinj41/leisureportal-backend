package cz.cvut.fel.bp.leisureportalbackend.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract entity model
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * @return get id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     * set id
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
