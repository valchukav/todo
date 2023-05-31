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
@Table(name = "priority")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "color")
    private String color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Priority)) return false;
        Priority priority = (Priority) o;
        return Objects.equals(getId(), priority.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
