package org.ricramiel.todoserver.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ricramiel.todoserver.model.Task;
import org.ricramiel.todoserver.model.TaskCreateDTO;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskCreateDTO map(Task task);

    @Mapping(target = "title")
    Task map(TaskCreateDTO taskCreateDTO);
}
