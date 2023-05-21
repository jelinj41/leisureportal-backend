package cz.cvut.fel.bp.leisureportalbackend.rest;

import cz.cvut.fel.bp.leisureportalbackend.exception.NotFoundException;
import cz.cvut.fel.bp.leisureportalbackend.model.Category;
import cz.cvut.fel.bp.leisureportalbackend.security.SecurityConstants;
import cz.cvut.fel.bp.leisureportalbackend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for Category
 */
@RestController
@RequestMapping("/rest/categories")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Retrieves all Categories.
     *
     * @return ResponseEntity containing the list of Categories.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }

    /**
     * Retrieves a Category by its ID.
     *
     * @param id The ID of the Category.
     * @return ResponseEntity containing the retrieved Category.
     * @throws NotFoundException If the Category was not found.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> get(@PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.find(id));
    }

    /**
     * Creates a new Category. Only accessible by users with the ADMIN role.
     *
     * @param category The JSON representation of the Category.
     * @return ResponseEntity with a status of 201.
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody Category category){
        categoryService.create(category);
        LOG.info("Category with ID:{} and name '{}' created.", category.getId(), category.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates a Category by its ID. Only accessible by users with the ADMIN role.
     *
     * @param id       The ID of the Category.
     * @param category The JSON representation of the updated Category.
     * @return ResponseEntity with a status of 204.
     * @throws NotFoundException If the Category was not found.
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody Category category) throws NotFoundException {
        categoryService.update(id,category);
        LOG.info("Category with ID:{} and updated.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
