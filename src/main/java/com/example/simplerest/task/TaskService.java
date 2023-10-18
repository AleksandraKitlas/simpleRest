package com.example.simplerest.task;

import com.example.simplerest.task.comand.AddUserToTaskCommand;
import com.example.simplerest.task.comand.ModifyTaskCommand;
import com.example.simplerest.task.comand.UpdateTaskStatusCommand;
import com.example.simplerest.user.AppUser;
import com.example.simplerest.user.AppUserService;
import org.apache.commons.lang3.Validate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserService appUserService;

    TaskService(TaskRepository taskRepository, AppUserService appUserService) {
        this.taskRepository = taskRepository;
        this.appUserService = appUserService;
    }

    public ResponseEntity<Task> addTask(ModifyTaskCommand modifyTaskCommand) {
        Task task = new Task(
                modifyTaskCommand.title(),
                modifyTaskCommand.description(),
                modifyTaskCommand.status(),
                modifyTaskCommand.deadline(),
                convertUserIdsToUsers(modifyTaskCommand.appUsersIds())
        );
        Task taskAfterSave = taskRepository.save(task);
        return new ResponseEntity<>(taskAfterSave, HttpStatus.OK);
    }

    private Set<AppUser> convertUserIdsToUsers(Set<Long> userIds) {
        return (userIds == null || userIds.isEmpty()) ? Collections.emptySet() : appUserService.findAppUsersByIds(userIds);
    }

    public ResponseEntity<Page<Task>> getAllTasks(String title, String description, Status status, Date deadline, Pageable pageable) {
        try {
            Specification<Task> spec = Specification.where((Specification<Task>) null)
                    .and(TaskSpecifications.filterByTitle(title))
                    .and(TaskSpecifications.filterByDescription(description))
                    .and(TaskSpecifications.filterByStatus(status))
                    .and(TaskSpecifications.filterByDeadline(deadline));

            Page<Task> tasks = taskRepository.findAll(spec, pageable);

            return tasks.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Task> getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Task> deleteTaskById(Long id) {
        try {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Task> updateTask(Long id, ModifyTaskCommand modifyTaskCommand) {
        Optional<Task> oldTaskData = taskRepository.findById(id);
        if (oldTaskData.isPresent()) {
            Task updatedTask = oldTaskData.get();

            if (modifyTaskCommand.title() != null) {
                updatedTask.setTitle(modifyTaskCommand.title());
            }

            if (modifyTaskCommand.description() != null) {
                updatedTask.setDescription(modifyTaskCommand.description());
            }

            if (modifyTaskCommand.status() != null) {
                updatedTask.setStatus(modifyTaskCommand.status());
            }

            if (modifyTaskCommand.deadline() != null) {
                updatedTask.setDeadline(modifyTaskCommand.deadline());
            }

            if (modifyTaskCommand.appUsersIds() != null) {
                updatedTask.setAssignedAppUsers(convertUserIdsToUsers(modifyTaskCommand.appUsersIds()));
            }

            taskRepository.save(updatedTask);

            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<Task> updateTaskStatus(UpdateTaskStatusCommand updateTaskStatusCommand) {
        Validate.notNull(updateTaskStatusCommand.id());
        Validate.notNull(updateTaskStatusCommand.newStatus());
        Optional<Task> taskToUpdate = taskRepository.findById(updateTaskStatusCommand.id());

        if (taskToUpdate.isPresent()) {
            Task updatedTask = taskToUpdate.get();
            updatedTask.setStatus(updateTaskStatusCommand.newStatus());

            Task taskAfterUpdate = taskRepository.save(updatedTask);

            return new ResponseEntity<>(taskAfterUpdate, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Task> addUserToTask(AddUserToTaskCommand addUserToTaskCommand) {
        Validate.notNull(addUserToTaskCommand.taskId());
        Validate.notNull(addUserToTaskCommand.userId());
        Optional<Task> taskToUpdate = taskRepository.findById(addUserToTaskCommand.taskId());

        if (taskToUpdate.isPresent()) {
            Task updatedTask = taskToUpdate.get();

            Set<AppUser> assignedUsers = updatedTask.getAssignedAppUsers();

            if (assignedUsers == null) {
                assignedUsers = new HashSet<>();
            }
            Optional<AppUser> userToAdd = appUserService.findAppUserById(addUserToTaskCommand.userId());
            if (userToAdd.isPresent()) {
                assignedUsers.add(userToAdd.get());
                updatedTask.setAssignedAppUsers(assignedUsers);
                Task taskAfterUpdate = taskRepository.save(updatedTask);

                return new ResponseEntity<>(taskAfterUpdate, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
