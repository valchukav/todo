package ru.avalc.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.todobackend.controller.exception.InvalidIDException;
import ru.avalc.todobackend.entity.Priority;
import ru.avalc.todobackend.repo.PriorityRepository;
import ru.avalc.todobackend.search.PrioritySearchValues;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityRepository priorityRepository;

    @Autowired
    public PriorityController(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody @Valid Priority priority) {
        try {
            Long id = priority.getId();
            if (id != null || id > 0) {
                throw new InvalidIDException("id must be null");
            }
        } catch (NullPointerException ignored) {
        }

        return ResponseEntity.ok(priorityRepository.save(priority));
    }

    @PutMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody @Valid Priority priority) {
        if (priority.getId() == null || priority.getId() <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Priority> entity = priorityRepository.findById(priority.getId());
            if (entity.isPresent()) {
                return ResponseEntity.ok(priorityRepository.save(priority));
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Priority> findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Priority> entity = priorityRepository.findById(id);
            if (entity.isPresent()) {
                return ResponseEntity.ok(entity.get());
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            priorityRepository.deleteById(id);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Priority>> getAll() {
        return ResponseEntity.ok(priorityRepository.findAllByOrderByIdAsc());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues searchValues) {
        return ResponseEntity.ok(priorityRepository.findByTitle(searchValues.getTitle()));
    }
}
