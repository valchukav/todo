package ru.avalc.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.todobackend.controller.exception.InvalidIDException;
import ru.avalc.todobackend.entity.Task;
import ru.avalc.todobackend.repo.TaskRepository;
import ru.avalc.todobackend.search.TaskSearchValues;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 06.06.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody @Valid Task task) {
        try {
            Long id = task.getId();
            if (id != null || id > 0) {
                throw new InvalidIDException("id must be null");
            }
        } catch (NullPointerException ignored) {
        }

        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody @Valid Task task) {
        if (task.getId() == null || task.getId() <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Task> entity = taskRepository.findById(task.getId());
            if (entity.isPresent()) {
                return ResponseEntity.ok(taskRepository.save(task));
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Task> entity = taskRepository.findById(id);
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
            taskRepository.deleteById(id);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Task>> search(@RequestBody TaskSearchValues searchValues) {
        return ResponseEntity.ok(
                taskRepository.findByParams(
                        searchValues.getTitle(),
                        searchValues.getCompleteType(),
                        searchValues.getPriorityId(),
                        searchValues.getCategoryId()
                )
        );
    }
}
