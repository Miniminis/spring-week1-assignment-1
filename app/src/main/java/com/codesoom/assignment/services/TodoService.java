package com.codesoom.assignment.services;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.codesoom.assignment.utils.JsonParser;

import java.io.IOException;

public class TodoService {

    private final TaskList taskList;

    public TodoService() {
        taskList = TaskList.getTaskList();
    }

    public String getTodos() throws IOException {
        return JsonParser.toJsonString(taskList.getTasks());
    }

    public String addTodo(Task newTask) {
        taskList.addTask(newTask);

        return HttpStatusCode.CREATED.getMessage();
    }
}