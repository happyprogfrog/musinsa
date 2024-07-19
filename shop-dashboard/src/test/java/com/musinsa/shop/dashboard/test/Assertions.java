package com.musinsa.shop.dashboard.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class Assertions {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void assertMvcErrorEquals(
            MvcResult result, ErrorCodes errorCodes
    ) throws UnsupportedEncodingException, JsonProcessingException {
        final String content = result.getResponse().getContentAsString();
        final var responseBody = objectMapper.readTree(content);
        final var errorField = responseBody.get("error");

        assertNotNull(errorField);
        assertTrue(errorField.isObject());
        assertEquals(errorCodes.code, errorField.get("code").asLong());
        assertEquals(errorCodes.message, errorField.get("localMessage").asText());
    }

    public static void assertMvcDataEquals(
            MvcResult result, Consumer<JsonNode> consumer
    ) throws UnsupportedEncodingException, JsonProcessingException {
        final String content = result.getResponse().getContentAsString();
        final var responseBody = objectMapper.readTree(content);
        final var dataField = responseBody.get("data");

        assertNotNull(dataField);
        assertTrue(dataField.isObject());

        consumer.accept(dataField);
    }

    public static void assertMvcJsonEquals(
            MvcResult result, Consumer<JsonNode> consumer
    ) throws UnsupportedEncodingException, JsonProcessingException {
        final String content = result.getResponse().getContentAsString();
        final var responseBody = objectMapper.readTree(content);
        final var dataField = responseBody.get("data");

        assertNotNull(dataField);
        assertTrue(dataField.isArray());

        consumer.accept(dataField);
    }
}
