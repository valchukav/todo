package ru.avalc.todobackend.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@Entity
@Table(name = "stat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "completed_total")
    private Long completedTotal;

    @Basic
    @Column(name = "uncompleted_total")
    private Long uncompletedTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stat)) return false;
        Stat stat = (Stat) o;
        return Objects.equals(getId(), stat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
