package ru.avalc.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.avalc.todobackend.entity.Stat;
import ru.avalc.todobackend.repo.StatRepository;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

@RestController
@RequestMapping("/stat")
public class StatController {

    public final static Long DEFAULT_ID = 1L;

    private final StatRepository statRepository;

    @Autowired
    public StatController(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @GetMapping("/get")
    public ResponseEntity<Stat> findById() {
        return ResponseEntity.ok(statRepository.findById(DEFAULT_ID).get());
    }
}
