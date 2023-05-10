package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.Activity;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CategoryDao extends  BaseDao<Category>{

    protected CategoryDao() {
        super(Category.class);
    }

    public boolean add(Category category, Activity activity){
        return category.add(activity);
    }

    public List<Category> findByName(String name){
        Objects.requireNonNull(name);
        return em.createNamedQuery("Category.findByName", Category.class).setParameter("name", name)
                .getResultList();
    }
}
