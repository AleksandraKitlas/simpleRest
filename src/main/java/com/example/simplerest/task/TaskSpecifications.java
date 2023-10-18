package com.example.simplerest.task;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class TaskSpecifications {
    public static Specification<Task> filterByTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<Task> filterByDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("description"), "%" + description + "%");
        };
    }

    public static Specification<Task> filterByStatus(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> filterByDeadline(Date deadline) {
        return (root, query, criteriaBuilder) -> {
            if (deadline == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("deadline"), deadline);
        };
    }
}
