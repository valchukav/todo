package ru.avalc.todobackend.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.avalc.todobackend.entity.CompleteType;
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

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "priority", "category"
            }
    )
    @Query("select t from Task t where (:title is null or :title = '' or lower(t.title) like lower(concat('%', :title, '%'))) and" +
            "(:completeType is null or t.completeType = :completeType) and " +
            "(:priorityId is null or t.priority.id = :priorityId) and " +
            "(:categoryId is null or t.category.id = :categoryId)")
    Page<Task> findByParams(@Param("title") String title,
                            @Param("completeType") CompleteType completeType,
                            @Param("priorityId") Long priorityId,
                            @Param("categoryId") Long categoryId,
                            Pageable pageable);
}
