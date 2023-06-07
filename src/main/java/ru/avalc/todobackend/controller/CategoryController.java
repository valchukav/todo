package ru.avalc.todobackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.todobackend.entity.Category;
import ru.avalc.todobackend.search.CategorySearchValues;
import ru.avalc.todobackend.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Alexei Valchuk, 04.06.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody @Valid Category category) {
        return ResponseEntity.ok(categoryService.add(category));
    }

    @PutMapping("/update")
    public ResponseEntity<Category> update(@RequestBody @Valid Category category) {
        return ResponseEntity.ok(categoryService.update(category));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues searchValues) {
        return ResponseEntity.ok(categoryService.searchByTitle(searchValues));
    }
}
