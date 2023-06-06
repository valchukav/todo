package ru.avalc.todobackend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.avalc.todobackend.controller.util.JsonUtil;
import ru.avalc.todobackend.entity.Priority;
import ru.avalc.todobackend.search.PrioritySearchValues;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.avalc.todobackend.controller.util.TestUtil.readFromJsonMvcResult;
import static ru.avalc.todobackend.controller.util.TestUtil.readListFromJsonMvcResult;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

public class PriorityControllerTest extends AbstractControllerTest {

    private static final Priority PRIORITY_1 = new Priority(56L, "Низкий", "#caffdd");
    private static final Priority PRIORITY_2 = new Priority(57L, "Средний", "#883bdc");
    private static final Priority NOT_EXISTED_PRIORITY = new Priority(587867L, "Высокий 123", "#f05f5f");
    private static final Priority PRIORITY_TO_UPDATE = new Priority(58L, "1234", "#1234");
    private static final Priority NEW_PRIORITY = new Priority(null, "NEW 1", "NEW 1");

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/priority/id/" + PRIORITY_1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Priority.class)).usingRecursiveComparison().isEqualTo(PRIORITY_1));
    }

    @Test
    void getWithNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/priority/id/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getWithNotExistedId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/priority/id/10392"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/priority/delete/" + PRIORITY_2.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/priority/id/" + PRIORITY_2.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteWithNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/priority/delete/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/priority/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NEW_PRIORITY)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Priority.class))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(NEW_PRIORITY));
    }

    @Test
    void addWithDefinedId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/priority/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_EXISTED_PRIORITY)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/priority/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(PRIORITY_TO_UPDATE)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Priority.class))
                        .usingRecursiveComparison()
                        .isEqualTo(PRIORITY_TO_UPDATE));
    }

    @Test
    void updateNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/priority/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_EXISTED_PRIORITY)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllSortedByIdAsc() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/priority/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Priority> priorities = readListFromJsonMvcResult(result, Priority.class);

        assertFalse(priorities.isEmpty());
        assertTrue(priorities.get(0).getTitle().equalsIgnoreCase("Низкий"));
    }

    @ParameterizedTest
    @MethodSource("ru.avalc.todobackend.controller.PriorityControllerTest#argumentsForGetAllWithTitleFilter")
    void getAllWithTitleFilter(String title, String expectedFirstTitle, boolean isEmpty) throws Exception {
        PrioritySearchValues prioritySearchValues = new PrioritySearchValues();
        prioritySearchValues.setTitle(title);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/priority/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(prioritySearchValues)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Priority> priorities = readListFromJsonMvcResult(result, Priority.class);

        assertEquals(priorities.isEmpty(), isEmpty);
        try {
            assertTrue(priorities.get(0).getTitle().equalsIgnoreCase(expectedFirstTitle));
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static Stream<Arguments> argumentsForGetAllWithTitleFilter() {
        return Stream.of(
                Arguments.of("", "вЗапредельно высокий", false),
                Arguments.of(null, "вЗапредельно высокий", false),
                Arguments.of("запред", "вЗапредельно высокий", false),
                Arguments.of("smth", null, true)
        );
    }
}
