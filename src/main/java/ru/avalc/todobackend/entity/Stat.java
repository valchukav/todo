package ru.avalc.todobackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
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
    @PositiveOrZero(message = "Invalid completed total count: the value must be greater than or equal to zero")
    private Long completedTotal;

    @Basic
    @Column(name = "uncompleted_total")
    @PositiveOrZero(message = "Invalid uncompleted total count: the value must be greater than or equal to zero")
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
