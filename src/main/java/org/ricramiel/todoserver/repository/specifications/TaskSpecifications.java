package org.ricramiel.todoserver.repository.specifications;

import org.ricramiel.todoserver.model.PriorityEnum;
import org.ricramiel.todoserver.model.StatusEnum;
import org.ricramiel.todoserver.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskSpecifications {

    private TaskSpecifications(){}

    public static Specification<Task> filterByTitle(String title) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title));
    }

    public static Specification<Task> filterByDescription(String description) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("description"), description));
    }

    public static Specification<Task> filterByDeadline(LocalDate deadline) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deadline"), deadline));
    }

    public static Specification<Task> filterByStatus(StatusEnum status) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Task> filterByPriority(PriorityEnum priority) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority));
    }

    public static Specification<Task> filterByCreationDate(LocalDate creationDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("creationDate"), creationDate));
    }

    public static Specification<Task> filterByEditDate(LocalDate editDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("editDate"), editDate));
    }
}
