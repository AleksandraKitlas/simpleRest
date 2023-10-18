package com.example.simplerest.task;

import com.example.simplerest.task.comand.AddUserToTaskCommand;
import com.example.simplerest.task.comand.ModifyTaskCommand;
import com.example.simplerest.task.comand.UpdateTaskStatusCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody ModifyTaskCommand modifyTaskCommand) {
        return taskService.addTask(modifyTaskCommand);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
            Pageable pageable
    ) {
        return taskService.getAllTasks(title, description, status, deadline, pageable);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Task> deleteTaskById(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }

    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody ModifyTaskCommand modifyTaskCommand) {
        return taskService.updateTask(id, modifyTaskCommand);
    }

    @PutMapping("/update-status")
    public ResponseEntity<Task> updateTaskStatus(@RequestBody UpdateTaskStatusCommand updateTaskStatusCommand) {
        return taskService.updateTaskStatus(updateTaskStatusCommand);
    }

    @PutMapping("/add-user-to-task")
    public ResponseEntity<Task> addUserToTask(@RequestBody AddUserToTaskCommand addUserToTaskCommand) {
        return taskService.addUserToTask(addUserToTaskCommand);
    }
}
