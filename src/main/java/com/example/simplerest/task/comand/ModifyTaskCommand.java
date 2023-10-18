package com.example.simplerest.task.comand;

import com.example.simplerest.task.Status;

import java.util.Date;
import java.util.Set;

public record ModifyTaskCommand(String title,
                                String description,
                                Status status,
                                Date deadline,
                                Set<Long> appUsersIds) {
}