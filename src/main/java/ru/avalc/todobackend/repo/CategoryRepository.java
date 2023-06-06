package ru.avalc.todobackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.avalc.todobackend.entity.Category;

import java.util.List;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByTitleAsc();

    @Query("select c from Category c where (:title is null or :title = '' or lower(c.title) like lower(concat('%', :title, '%'))) order by c.title asc")
    List<Category> findByTitle(@Param("title") String title);
}
