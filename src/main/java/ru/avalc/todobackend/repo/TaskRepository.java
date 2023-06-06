package ru.avalc.todobackend.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.todobackend.entity.Task;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 06.06.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "priority", "category"
            }
    )
    @Override
    List<Task> findAll();

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "priority", "category"
            }
    )
    @Override
    Optional<Task> findById(Long aLong);
}
