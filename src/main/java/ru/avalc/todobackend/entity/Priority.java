package ru.avalc.todobackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Column(name = "title", unique = true)
    @NotNull(message = "Missed param: title")
    @NotBlank(message = "Missed param: title")
    @Size(min = 3, max = 45, message = "invalid title: must be of 3 - 45 characters")
    private String title;

    @Basic
    @Column(name = "color")
    @NotNull(message = "Missed param: color")
    @NotBlank(message = "Missed param: color")
    @Size(min = 3, max = 45, message = "invalid color: must be of 3 - 45 characters")
    private String color;

    public Priority(String title, String color) {
        this.title = title;
        this.color = color;
    }

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
