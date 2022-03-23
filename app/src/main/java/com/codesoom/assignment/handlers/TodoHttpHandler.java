package com.codesoom.assignment.handlers;

import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.utils.JsonParser;
import com.codesoom.assignment.utils.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private Logger logger;
    private List<Task> tasks;

    public TodoHttpHandler() {
        logger = new Logger();
        tasks = new ArrayList<>();

        dummyDatas();
    }

    private void dummyDatas() {
        tasks.add(new Task(1L, "codesoom lecture complete 1"));
        tasks.add(new Task(2L, "codesoom lecture complete 2"));
        tasks.add(new Task(3L, "codesoom lecture complete 3"));
        tasks.add(new Task(4L, "codesoom lecture complete 4"));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String reqMethod = httpExchange.getRequestMethod();
        URI reqUri = httpExchange.getRequestURI();
        String reqPath = reqUri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String reqBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        logger.logRequest(reqMethod, reqUri, reqPath, reqBody);

        String resBody = "";

        if (reqMethod.equals(HttpMethod.GET.getMethod()) && reqPath.equals("/tasks")) {
            resBody = JsonParser.toJsonString(tasks);
        }

        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resBody.getBytes(StandardCharsets.UTF_8).length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(resBody.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        logger.logResponse(resBody);
    }

}
