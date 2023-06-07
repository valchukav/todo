package ru.avalc.todobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.todobackend.entity.Stat;
import ru.avalc.todobackend.repo.StatRepository;

/**
 * @author Alexei Valchuk, 07.06.2023, email: a.valchukav@gmail.com
 */

@Service
@Transactional
public class StatService {

    public final static Long DEFAULT_ID = 1L;

    private final StatRepository statRepository;

    @Autowired
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public Stat get() {
        return statRepository.findById(DEFAULT_ID).get();
    }
}
