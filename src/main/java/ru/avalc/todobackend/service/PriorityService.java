package ru.avalc.todobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.todobackend.controller.exception.InvalidIDException;
import ru.avalc.todobackend.entity.Priority;
import ru.avalc.todobackend.repo.PriorityRepository;
import ru.avalc.todobackend.search.PrioritySearchValues;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 07.06.2023, email: a.valchukav@gmail.com
 */

@Service
@Transactional
public class PriorityService {

    private final PriorityRepository priorityRepository;

    @Autowired
    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public Priority add(Priority priority) {
        try {
            Long id = priority.getId();
            if (id != null || id > 0) {
                throw new InvalidIDException("id must be null");
            }
        } catch (NullPointerException ignored) {
        }

        return priorityRepository.save(priority);
    }

    public Priority update(Priority priority) {
        if (priority.getId() == null || priority.getId() <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Priority> entity = priorityRepository.findById(priority.getId());
            if (entity.isPresent()) {
                return priorityRepository.save(priority);
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    public Priority get(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Priority> entity = priorityRepository.findById(id);
            if (entity.isPresent()) {
                return entity.get();
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            priorityRepository.deleteById(id);
        }
    }

    public List<Priority> getAll() {
        return priorityRepository.findAllByOrderByIdAsc();
    }

    public List<Priority> searchByTitle(PrioritySearchValues searchValues) {
        return priorityRepository.findByTitle(searchValues.getTitle());
    }
}
