package ru.avalc.todobackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.todobackend.entity.Category;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
