package ru.avalc.todobackend.controller.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Page;

/**
 * @author Alexei Valchuk, 07.06.2023, email: a.valchukav@gmail.com
 */

public class PageModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public PageModule() {
        addDeserializer(Page.class, new PageDeserializer());
    }
}
