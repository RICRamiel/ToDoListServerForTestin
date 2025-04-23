package org.ricramiel.todoserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.ricramiel.todoserver.model.*;
import org.ricramiel.todoserver.model.mapper.TaskMapper;
import org.ricramiel.todoserver.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/task")
@Tag(name = "Task")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("/create")
    public void createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        taskService.createTask(taskMapper.map(taskCreateDTO));
    }

    @GetMapping()
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/filtered")
    public List<Task> getFilteredTasks(@RequestParam(required = false) String title, @RequestParam(required = false) String description, @RequestParam(required = false) LocalDate deadline, @RequestParam(required = false) StatusEnum status, @RequestParam(required = false) PriorityEnum priority, @RequestParam(required = false) LocalDate creationDate, @RequestParam(required = false) LocalDate editDate) {
        return taskService.getFilteredTasks(title, description, deadline, status, priority, creationDate, editDate);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

    @PutMapping("/edit/{id}")
    public void editTask(@RequestBody TaskEditDTO taskEditDTO, @PathVariable Long id) {
        taskService.updateTask(taskEditDTO, id);
    }

    @PostMapping("/check/{id}")
    public void checkTask(@PathVariable Long id) {
        taskService.checkDoneTask(id);
    }

    @GetMapping("/get/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
}
