package ru.avalc.todobackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.avalc.todobackend.entity.Stat;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.avalc.todobackend.controller.util.TestUtil.readFromJsonMvcResult;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

public class StatRestTest extends AbstractRestTest {

    @Test
    void testGet() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/stat/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Stat stat = readFromJsonMvcResult(result, Stat.class);

        assertNotNull(stat);
        assertTrue(stat.getCompletedTotal() >= 0);
        assertTrue(stat.getUncompletedTotal() >= 0);
    }
}
