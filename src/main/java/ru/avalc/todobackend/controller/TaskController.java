package ru.avalc.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.todobackend.entity.Task;
import ru.avalc.todobackend.search.TaskSearchValues;
import ru.avalc.todobackend.service.TaskService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Alexei Valchuk, 06.06.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody @Valid Task task) {
        return ResponseEntity.ok(taskService.add(task));
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody @Valid Task task) {
        return ResponseEntity.ok(taskService.update(task));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.get(id));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues searchValues) {
        return ResponseEntity.ok(taskService.search(searchValues));
    }
}
