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

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    /**
     * Create new Category.
     * @param category
     */
    @Transactional
    public void create(Category category){
        Objects.requireNonNull(category);
        categoryDao.persist(category);
    }


    /**
     * Update Category by id.
     * @param id
     * @param category
     * @throws NotFoundException if category with this id is not in database.
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
     * Get Category by id.
     * @param id
     * @return Category
     * @throws NotFoundException if category with this id is not in database.
     */
    public Category find(Integer id) throws NotFoundException {
        Category found = categoryDao.find(id);
        if (found == null) throw new NotFoundException();
        return found;
    }


    /**
     * Get all categories from database.
     * @return List<Category>
     */
    public List<Category> findAll(){
        return categoryDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Category> findByName(String name) {
        return categoryDao.findByName(name);
    }


}
