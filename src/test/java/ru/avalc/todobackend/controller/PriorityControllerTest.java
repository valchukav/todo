package ru.avalc.todobackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.avalc.todobackend.controller.util.JsonUtil;
import ru.avalc.todobackend.entity.Priority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.avalc.todobackend.controller.util.TestUtil.readFromJsonMvcResult;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

@Sql(scripts = "classpath:bd/populate_db.sql")
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
}
