package ru.avalc.todobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.avalc.todobackend.entity.Stat;
import ru.avalc.todobackend.service.StatService;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/stat")
public class StatController {

    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/get")
    public ResponseEntity<Stat> findById() {
        return ResponseEntity.ok(statService.get());
    }
}
