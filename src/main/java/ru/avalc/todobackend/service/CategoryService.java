package ru.avalc.todobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.todobackend.controller.exception.InvalidIDException;
import ru.avalc.todobackend.entity.Category;
import ru.avalc.todobackend.repo.CategoryRepository;
import ru.avalc.todobackend.search.CategorySearchValues;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 07.06.2023, email: a.valchukav@gmail.com
 */

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category add(Category category) {
        try {
            Long id = category.getId();
            if (id != null || id > 0) {
                throw new InvalidIDException("id must be null");
            }
        } catch (NullPointerException ignored) {
        }

        return categoryRepository.save(category);
    }

    public Category update(Category category) {
        if (category.getId() == null || category.getId() <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Category> entity = categoryRepository.findById(category.getId());
            if (entity.isPresent()) {
                return categoryRepository.save(category);
            } else {
                throw new InvalidIDException("Category with such id does not exist");
            }
        }
    }

    public Category get(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Category> entity = categoryRepository.findById(id);
            if (entity.isPresent()) {
                return entity.get();
            } else {
                throw new InvalidIDException("Category with such id does not exist");
            }
        }
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            categoryRepository.deleteById(id);
        }
    }

    public List<Category> getAll() {
        return categoryRepository.findAllByOrderByTitleAsc();
    }

    public List<Category> searchByTitle(CategorySearchValues searchValues) {
        return categoryRepository.findByTitle(searchValues.getTitle());
    }
}
