package ru.avalc.todobackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * @author Alexei Valchuk, 31.05.2023, email: a.valchukav@gmail.com
 */

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {

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
    @Column(name = "completed_count")
    @PositiveOrZero(message = "Invalid completed count: the value must be greater than or equal to zero")
    private Long completedCount;

    @Basic
    @Column(name = "uncompleted_count")
    @PositiveOrZero(message = "Invalid uncompleted count: the value must be greater than or equal to zero")
    private Long uncompletedCount;

    public Category(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
