package com.example.simplerest.task.comand;

import com.example.simplerest.task.Status;

public record UpdateTaskStatusCommand(Long id, Status newStatus) {
}
