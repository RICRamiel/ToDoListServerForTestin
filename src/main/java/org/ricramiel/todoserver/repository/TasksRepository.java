package org.ricramiel.todoserver.repository;

import org.ricramiel.todoserver.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TasksRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
}
