package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    public static final String NOT_FOUND_MESSAGE = "Not Found.";
    public static final String NOT_FOUND_TASK_ID_MESSAGE = "Can't find task from your id.";

    private Long lastTaskId = 1L;

    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(httpExchange);
        System.out.println(httpRequest);

        InputStream httpRequestBody = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));

        if (httpRequest.isMatchMethod("GET") && httpRequest.isMatchPath("/tasks")) {
            new HttpResponseOK(httpExchange).send(tasksToJson());
        }

        if (httpRequest.isMatchMethod("GET") && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            Task task = getTaskFromId(taskId);
            if (task == null) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            new HttpResponseOK(httpExchange).send(taskToJson(task));
        }

        if (httpRequest.isMatchMethod("POST") && httpRequest.isMatchPath("/tasks")) {
            if (!body.isEmpty()) {
                Task task = toTask(body);
                tasks.add(task);

                new HttpResponseCreated(httpExchange).send(taskToJson(task));
            }
        }

        if (httpRequest.isUpdateMethod() && httpRequest.pathStartsWith("/tasks") && httpRequest
            .hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            Task task = getTaskFromId(taskId);
            if (task == null) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }
            Task bodyTask = getTaskFromContent(body);
            task.setTitle(bodyTask.getTitle());

            new HttpResponseOK(httpExchange).send(taskToJson(task));
        }

        if (httpRequest.isMatchMethod("DELETE") && httpRequest.pathStartsWith("/tasks")
            && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            Task task = getTaskFromId(taskId);
            if (task == null) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }
            tasks.remove(task);

            new HttpResponseNoContent(httpExchange).send(taskToJson(task));
        }

        new HttpResponseNotFound(httpExchange).send(NOT_FOUND_MESSAGE);
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private Task getTaskFromContent(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private Task toTask(String content) throws JsonProcessingException {
        Task task = getTaskFromContent(content);
        task.setId(++lastTaskId);

        return task;
    }

    private Task getTaskFromId(long taskId) {
        return tasks.stream()
            .filter(task -> task.isMatchId(taskId))
            .findFirst()
            .orElse(null);
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
