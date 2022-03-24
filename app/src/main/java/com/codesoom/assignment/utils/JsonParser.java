package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonString(Object obj) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);

        return outputStream.toString();
    }

    public static Task toTask(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, Task.class);
    }

}
