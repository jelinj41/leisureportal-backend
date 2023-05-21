package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Dao for Category class
 */
@Repository
public class CategoryDao extends  BaseDao<Category>{

    /**
    * constructor
     */
    protected CategoryDao() {
        super(Category.class);
    }

    /**
     * Adds an activity to a category.
     *
     * @param category The category to add the activity to.
     * @param activity The activity to add.
     * @return true if the activity was added successfully, false otherwise.
     */
    public boolean add(Category category, Activity activity){
        return category.add(activity);
    }

    /**
     * Finds categories by name.
     *
     * @param name The name of the category to search for.
     * @return A list of categories with the specified name.
     * @throws NullPointerException if the name is null.
     */
    public List<Category> findByName(String name){
        Objects.requireNonNull(name);
        return em.createNamedQuery("Category.findByName", Category.class).setParameter("name", name)
                .getResultList();
    }
}
