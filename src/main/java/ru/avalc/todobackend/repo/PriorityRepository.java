package ru.avalc.todobackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.avalc.todobackend.entity.Priority;

import java.util.List;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findAllByOrderByIdAsc();

    @Query("select p from Priority p where (:title is null or :title = '' or lower(p.title) like lower(concat('%', :title, '%'))) order by p.title asc")
    List<Priority> findByTitle(@Param("title") String title);
}
