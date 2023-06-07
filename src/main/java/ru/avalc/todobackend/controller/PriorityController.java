package ru.avalc.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.todobackend.entity.Priority;
import ru.avalc.todobackend.search.PrioritySearchValues;
import ru.avalc.todobackend.service.PriorityService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody @Valid Priority priority) {
        return ResponseEntity.ok(priorityService.add(priority));
    }

    @PutMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody @Valid Priority priority) {
        return ResponseEntity.ok(priorityService.update(priority));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Priority> findById(@PathVariable Long id) {
        return ResponseEntity.ok(priorityService.get(id));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        priorityService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Priority>> getAll() {
        return ResponseEntity.ok(priorityService.getAll());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues searchValues) {
        return ResponseEntity.ok(priorityService.searchByTitle(searchValues));
    }
}
