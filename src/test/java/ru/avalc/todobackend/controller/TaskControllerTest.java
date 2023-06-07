package ru.avalc.todobackend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.avalc.todobackend.controller.util.JsonUtil;
import ru.avalc.todobackend.entity.CompleteType;
import ru.avalc.todobackend.entity.Task;
import ru.avalc.todobackend.search.TaskSearchValues;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.avalc.todobackend.controller.util.TestUtil.readFromJsonMvcResult;
import static ru.avalc.todobackend.controller.util.TestUtil.readListFromJsonMvcResult;

/**
 * @author Alexei Valchuk, 06.06.2023, email: a.valchukav@gmail.com
 */

public class TaskControllerTest extends AbstractControllerTest {

    private static final Task TASK_1 = new Task(328L, "Позвонить родителям", CompleteType.COMPLETED, new Date(), null, null);
    private static final Task TASK_2 = new Task(331L, "Посмотреть мультики", CompleteType.UNCOMPLETED, new Date(), null, null);
    private static final Task NOT_EXISTED_TASK = new Task(587867L, "TASK 123", CompleteType.COMPLETED, new Date(), null, null);
    private static final Task TASK_TO_UPDATE = new Task(333L, "NEW", CompleteType.COMPLETED, new Date(), null, null);
    private static final Task NEW_TASK = new Task(null, "NEW TASK 123", CompleteType.COMPLETED, new Date(), null, null);

    private static final String[] FIELDS_TO_IGNORE = new String[]{"date", "priority", "category"};

    private static final int INITIAL_TASKS_COUNT = 18;
    private static final int INITIAL_COMPLETED_TASKS_COUNT = 7;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/task/id/" + TASK_1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Task.class))
                        .usingRecursiveComparison()
                        .ignoringFields(FIELDS_TO_IGNORE)
                        .isEqualTo(TASK_1));
    }

    @Test
    void getWithNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/task/id/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getWithNotExistedId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/task/id/10394232"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/task/delete/" + TASK_2.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/task/id/" + TASK_2.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteWithNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/task/delete/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NEW_TASK)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Task.class))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringFields(FIELDS_TO_IGNORE)
                        .isEqualTo(NEW_TASK));
    }

    @Test
    void addWithDefinedId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_EXISTED_TASK)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(TASK_TO_UPDATE)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Task.class))
                        .usingRecursiveComparison()
                        .ignoringFields(FIELDS_TO_IGNORE)
                        .isEqualTo(TASK_TO_UPDATE));
    }

    @Test
    void updateNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_EXISTED_TASK)))
                .andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @MethodSource("ru.avalc.todobackend.controller.TaskControllerTest#argumentsForGetAllWithTitleFilter")
    void getAllWithFilter(String title, CompleteType completeType, Long priorityId, Long categoryId, int expectedListSize, boolean isEmpty) throws Exception {
        TaskSearchValues taskSearchValues = new TaskSearchValues();
        taskSearchValues.setTitle(title);
        taskSearchValues.setCompleteType(completeType);
        taskSearchValues.setCategoryId(categoryId);
        taskSearchValues.setPriorityId(priorityId);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/task/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(taskSearchValues)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Task> tasks = readListFromJsonMvcResult(result, Task.class);

        assertEquals(tasks.isEmpty(), isEmpty);
        assertTrue(isBetween(tasks, expectedListSize));
    }

    private static Stream<Arguments> argumentsForGetAllWithTitleFilter() {
        return Stream.of(
                Arguments.of("", null, null, null, INITIAL_TASKS_COUNT, false),
                Arguments.of("", CompleteType.COMPLETED, null, null, INITIAL_COMPLETED_TASKS_COUNT, false),
                Arguments.of("java", null, null, null, 1, false),
                Arguments.of(null, null, 60L, null, 0, true),
                Arguments.of(null, null, 58L, null, 4, false),
                Arguments.of(null, null, null, 171L, 1, false),
                Arguments.of("java", CompleteType.COMPLETED, null, null, 0, true),
                Arguments.of("smth", null, null, null, 0, true)
        );
    }

    private static boolean isBetween(List<Task> list, int expectedCount) {
        return list.size() <= expectedCount + 1 && list.size() >= expectedCount - 1;
    }
}
