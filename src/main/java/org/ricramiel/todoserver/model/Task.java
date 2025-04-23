package org.ricramiel.todoserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 4, max = 255)
    private String title;

    private String description;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private PriorityEnum priority = PriorityEnum.MEDIUM;

    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.ACTIVE;

    @CreatedDate
    private LocalDate creationDate;
    @LastModifiedDate
    private LocalDate editDate;

    private Boolean completed = false;

    @PrePersist
    public void prePersist() {
        if (priority == null) {
            priority = PriorityEnum.MEDIUM;
        }
        if (status == null) {
            status = StatusEnum.ACTIVE;
        }
    }
}
