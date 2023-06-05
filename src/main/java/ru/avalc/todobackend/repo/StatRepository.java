package ru.avalc.todobackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.todobackend.entity.Stat;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

}
