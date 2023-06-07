package ru.avalc.todobackend.controller.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.avalc.todobackend.entity.Task;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Alexei Valchuk, 05.06.2023, email: a.valchukav@gmail.com
 */

public class TestUtil {

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static Page<Task> readPageFromJsonMvcResult(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        return JacksonObjectMapper.getMapper().readValue(getContent(result), new TypeReference<>() {
        });
    }
}
