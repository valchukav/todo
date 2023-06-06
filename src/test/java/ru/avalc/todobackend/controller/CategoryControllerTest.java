package ru.avalc.todobackend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.avalc.todobackend.controller.util.JsonUtil;
import ru.avalc.todobackend.entity.Category;
import ru.avalc.todobackend.search.CategorySearchValues;

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

public class CategoryControllerTest extends AbstractControllerTest {

    private static final Category CATEGORY_1 = new Category(167L, "Семья", 1L, 1L);
    private static final Category CATEGORY_2 = new Category(168L, "Работа");
    private static final Category NOT_EXISTED_CATEGORY = new Category(587867L, "Высокий 123");
    private static final Category CATEGORY_TO_UPDATE = new Category(170L, "Отдых");
    private static final Category NEW_CATEGORY = new Category(null, "NEW 1");

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/id/" + CATEGORY_1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Category.class)).usingRecursiveComparison().isEqualTo(CATEGORY_1));
    }

    @Test
    void getWithNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/id/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getWithNotExistedId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/id/10392"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/category/delete/" + CATEGORY_2.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/category/id/" + CATEGORY_2.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteWithNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/category/delete/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NEW_CATEGORY)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Category.class))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(NEW_CATEGORY));
    }

    @Test
    void addWithDefinedId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_EXISTED_CATEGORY)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(CATEGORY_TO_UPDATE)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Category.class))
                        .usingRecursiveComparison()
                        .isEqualTo(CATEGORY_TO_UPDATE));
    }

    @Test
    void updateNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_EXISTED_CATEGORY)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllSortedByTitleAsc() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/category/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Category> categories = readListFromJsonMvcResult(result, Category.class);

        assertFalse(categories.isEmpty());
        assertTrue(categories.get(0).getTitle().equalsIgnoreCase("Здоровье"));
    }

    @ParameterizedTest
    @MethodSource("ru.avalc.todobackend.controller.CategoryControllerTest#argumentsForGetAllWithTitleFilter")
    void getAllWithTitleFilter(String title, String expectedFirstTitle, boolean isEmpty) throws Exception {
        CategorySearchValues categorySearchValues = new CategorySearchValues();
        categorySearchValues.setTitle(title);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/category/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(categorySearchValues)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Category> categories = readListFromJsonMvcResult(result, Category.class);

        assertEquals(categories.isEmpty(), isEmpty);
        try {
            assertTrue(categories.get(0).getTitle().equalsIgnoreCase(expectedFirstTitle));
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static Stream<Arguments> argumentsForGetAllWithTitleFilter() {
        return Stream.of(
                Arguments.of("", "Здоровье", false),
                Arguments.of(null, "Здоровье", false),
                Arguments.of("овая", "Новая категория", false),
                Arguments.of("smth", null, true)
        );
    }
}
