package com.example.simplerest.task;

import com.example.simplerest.user.AppUser;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "task")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Date deadline;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_task",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "app_user_id")}
    )
    private Set<AppUser> assignedAppUsers = new HashSet<>();

    public Task() {
    }

    public Task(String title, String description, Status status, Date deadline, Set<AppUser> assignedAppUsers) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.assignedAppUsers = assignedAppUsers;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Set<AppUser> getAssignedAppUsers() {
        return assignedAppUsers;
    }

    public void setAssignedAppUsers(Set<AppUser> assignedAppUsers) {
        this.assignedAppUsers = assignedAppUsers;
    }
}
