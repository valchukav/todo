package ru.avalc.todobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.todobackend.controller.exception.InvalidIDException;
import ru.avalc.todobackend.entity.Task;
import ru.avalc.todobackend.repo.TaskRepository;
import ru.avalc.todobackend.search.TaskSearchValues;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 07.06.2023, email: a.valchukav@gmail.com
 */

@Service
@Transactional
public class TaskService {

    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static String DEFAULT_SEARCH_COLUMN = "id";

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task add(Task task) {
        try {
            Long id = task.getId();
            if (id != null || id > 0) {
                throw new InvalidIDException("id must be null");
            }
        } catch (NullPointerException ignored) {
        }

        return taskRepository.save(task);
    }

    public Task update(Task task) {
        if (task.getId() == null || task.getId() <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Task> entity = taskRepository.findById(task.getId());
            if (entity.isPresent()) {
                return taskRepository.save(task);
            } else {
                throw new InvalidIDException("Priority with such id does not exist");
            }
        }
    }

    public Task get(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidIDException("id must be defined");
        } else {
            Optional<Task> entity = taskRepository.findById(id);
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
            taskRepository.deleteById(id);
        }
    }

    public Page<Task> search(TaskSearchValues searchValues) {
        Sort.Direction direction = searchValues.getSortDirection() == null
                || searchValues.getSortDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(
                direction,
                searchValues.getSortColumn() != null ? searchValues.getSortColumn() : DEFAULT_SEARCH_COLUMN
        );

        PageRequest pageRequest = PageRequest.of(
                searchValues.getPageNumber() != null ? searchValues.getPageNumber() : 0,
                searchValues.getPageSize() != null ? searchValues.getPageSize() : DEFAULT_PAGE_SIZE,
                sort
        );

        return taskRepository.findByParams(
                searchValues.getTitle() != null ? searchValues.getTitle() : null,
                searchValues.getCompleteType() != null ? searchValues.getCompleteType() : null,
                searchValues.getPriorityId() != null ? searchValues.getPriorityId() : null,
                searchValues.getCategoryId() != null ? searchValues.getCategoryId() : null,
                pageRequest);
    }
}
