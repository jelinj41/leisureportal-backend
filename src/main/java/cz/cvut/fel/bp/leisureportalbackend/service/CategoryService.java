package cz.cvut.fel.bp.leisureportalbackend.service;

import cz.cvut.fel.bp.leisureportalbackend.dao.CategoryDao;
import cz.cvut.fel.bp.leisureportalbackend.exception.NotFoundException;
import cz.cvut.fel.bp.leisureportalbackend.model.Address;
import cz.cvut.fel.bp.leisureportalbackend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for Category
 */
@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    /**
     * Creates a new category.
     *
     * @param category The category to create.
     * @throws NullPointerException if the category is null.
     */
    @Transactional
    public void create(Category category){
        Objects.requireNonNull(category);
        categoryDao.persist(category);
    }


    /**
     * Updates a category by its ID.
     *
     * @param id       The ID of the category to update.
     * @param category The updated category object.
     * @throws NotFoundException if the category with the specified ID is not found.
     * @throws NullPointerException if the category is null.
     */
    @Transactional
    public void update(Integer id, Category category) throws NotFoundException {
        Objects.requireNonNull(category);
        Category found = categoryDao.find(id);
        if (found == null) throw new NotFoundException();
        found.setName(category.getName());
        categoryDao.update(found);
    }


    /**
     * Finds a category by its ID.
     *
     * @param id The ID of the category to find.
     * @return The found Category object.
     * @throws NotFoundException if the category with the specified ID is not found.
     */
    public Category find(Integer id) throws NotFoundException {
        Category found = categoryDao.find(id);
        if (found == null) throw new NotFoundException();
        return found;
    }


    /**
     * Retrieves all categories.
     *
     * @return A list of all categories.
     */
    public List<Category> findAll(){
        return categoryDao.findAll();
    }

    /**
     * Finds categories by name.
     *
     * @param name The name to search for.
     * @return A list of categories with the specified name.
     */
    @Transactional(readOnly = true)
    public List<Category> findByName(String name) {
        return categoryDao.findByName(name);
    }


}
