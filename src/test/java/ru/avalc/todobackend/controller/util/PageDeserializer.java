package ru.avalc.todobackend.controller.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei Valchuk, 07.06.2023, email: a.valchukav@gmail.com
 */

//source: https://stackoverflow.com/questions/52490399/spring-boot-page-deserialization-pageimpl-no-constructor
public class PageDeserializer extends JsonDeserializer<Page<?>> implements ContextualDeserializer {

    private static final String CONTENT = "content";
    private static final String NUMBER = "number";
    private static final String SIZE = "size";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private static final String SORT = "sort";
    private static final String SORT_PROPERTY = "property";
    private static final String SORT_DIRECTION = "direction";
    private JavaType valueType;

    @Override
    public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final CollectionType valuesListType = ctxt.getTypeFactory().constructCollectionType(List.class, valueType);

        List<?> list = new ArrayList<>();
        int pageNumber = 0;
        int pageSize = 0;
        long total = 0;
        String sortProperty = "";
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (p.isExpectedStartObjectToken()) {
            p.nextToken();
            if (p.hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
                String propName = p.getCurrentName();
                do {
                    p.nextToken();
                    switch (propName) {
                        case CONTENT:
                            list = ctxt.readValue(p, valuesListType);
                            break;
                        case NUMBER:
                            pageNumber = ctxt.readValue(p, Integer.class);
                            break;
                        case SIZE:
                            pageSize = ctxt.readValue(p, Integer.class);
                            break;
                        case TOTAL_ELEMENTS:
                            total = ctxt.readValue(p, Long.class);
                            break;
                        case SORT:
                            p.nextToken();
                            p.nextToken();
                            if (p.hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
                                String insPropName = p.getCurrentName();
                                do {
                                    p.nextToken();
                                    switch (insPropName) {
                                        case SORT_PROPERTY:
                                            sortProperty = ctxt.readValue(p, String.class);
                                            break;
                                        case SORT_DIRECTION:
                                            if (ctxt.readValue(p, String.class).equalsIgnoreCase("asc")) {
                                                sortDirection = Sort.Direction.ASC;
                                            } else {
                                                sortDirection = Sort.Direction.DESC;
                                            }
                                            break;
                                    }
                                } while (((insPropName = p.nextFieldName())) != null);
                            }
                        default:
                            p.skipChildren();
                            break;
                    }
                } while (((propName = p.nextFieldName())) != null);
            } else {
                ctxt.handleUnexpectedToken(handledType(), p);
            }
        } else {
            ctxt.handleUnexpectedToken(handledType(), p);
        }

        return new PageImpl<>(list, PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortProperty)), total);
    }

    /**
     * This is the main point here.
     * The PageDeserializer is created for each specific deserialization with concrete generic parameter type of Page.
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        //This is the Page actually
        final JavaType wrapperType = ctxt.getContextualType();
        final PageDeserializer deserializer = new PageDeserializer();
        //This is the parameter of Page
        deserializer.valueType = wrapperType.containedType(0);
        return deserializer;
    }
}
