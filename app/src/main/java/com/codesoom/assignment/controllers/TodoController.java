package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TodoService;

import java.io.IOException;

public class TodoController {

    private final TodoService todoService;

    public TodoController() {
        this.todoService = new TodoService();
    }

    public String getTodos() throws IOException {
        return todoService.getTodos();
    }

    public String postTodo(Task newTask) {
        return todoService.addTodo(newTask);
    }
}