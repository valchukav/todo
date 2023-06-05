package ru.avalc.todobackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.todobackend.controller.exception.InvalidIDException;
import ru.avalc.todobackend.entity.Category;
import ru.avalc.todobackend.repo.CategoryRepository;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 04.06.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody @Valid Category category) {
        try {
            Long id = category.getId();
            if (id != null || id > 0) {
                throw new InvalidIDException("id must be null");
            }
        } catch (NullPointerException ignored) {
        }

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("/update")
    public ResponseEntity<Category> update(@RequestBody @Valid Category category) {
        if (category.getId() == null || category.getId() <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Category> entity = categoryRepository.findById(category.getId());
            if (entity.isPresent()) {
                return ResponseEntity.ok(categoryRepository.save(category));
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Category> entity = categoryRepository.findById(id);
            if (entity.isPresent()) {
                return ResponseEntity.ok(entity.get());
            } else {
                throw new InvalidIDException("Category with such id does not exist");
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            categoryRepository.deleteById(id);
        }
    }
}
