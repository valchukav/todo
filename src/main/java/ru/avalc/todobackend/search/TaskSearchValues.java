package ru.avalc.todobackend.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.avalc.todobackend.entity.CompleteType;

/**
 * @author Alexei Valchuk, 06.06.2023, email: a.valchukav@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskSearchValues {

    private String title;
    private CompleteType completeType;
    private Long priorityId;
    private Long categoryId;
}
