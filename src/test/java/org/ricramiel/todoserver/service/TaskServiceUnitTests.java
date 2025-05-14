package org.ricramiel.todoserver.service;


import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.ricramiel.todoserver.model.PriorityEnum;
import org.ricramiel.todoserver.model.Task;
import org.ricramiel.todoserver.model.mapper.TaskMapper;
import org.ricramiel.todoserver.repository.TasksRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest()
@RunWith(MockitoJUnitRunner.class)
public class TaskServiceUnitTests {
    @Mock
    TaskMapper taskMapper;
    @Mock
    TasksRepository tasksRepository;
    @InjectMocks
    TaskService taskService;

    @Test
    public void when_add_task_with_only_priority_macros() {
        Task task = new Task();
        task.setTitle("!1 !String");
        task.setPriority(null);

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());
        Task created = taskService.createTask(task);

        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getPriority()).isEqualTo(PriorityEnum.CRITICAL);
    }

    @Test
    public void when_add_task_with_only_priority_macros_and_priority_isnt_null() {
        Task task = new Task();
        task.setPriority(PriorityEnum.CRITICAL);
        task.setTitle("!2 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);
        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getPriority()).isEqualTo(PriorityEnum.CRITICAL);
    }

    @Test
    public void when_add_task_with_only_deadline_macros() {
        Task task = new Task();
        task.setTitle("!before 20.12.2055 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);
        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isEqualTo("2055-12-20");
    }

    @Test
    public void when_add_task_with_only_deadline_tabs_macros() {
        Task task = new Task();
        task.setTitle("!before 20-12-2055 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);
        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isEqualTo("2055-12-20");
    }


    @Test
    public void when_add_task_with_only_deadline_macros_and_deadline_isnt_null() {
        Task task = new Task();
        task.setTitle("!before 20.12.2055 !String");
        task.setDeadline(LocalDate.of(2020, 12, 20));


        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);
        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getPriority()).isEqualTo(PriorityEnum.MEDIUM);
        assertThat(created.getDeadline()).isEqualTo("2020-12-20");
    }

    @Test
    public void when_add_task_with_priority_and_deadline_macros() {
        Task task = new Task();
        task.setPriority(null);
        task.setTitle("!before 20.12.2055 !3 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);
        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isEqualTo("2055-12-20");
        assertThat(created.getPriority()).isEqualTo(PriorityEnum.MEDIUM);
    }

    @Test
    public void when_add_task_with_priority_and_deadline_macros_and_both_isnt_null() {
        Task task = new Task();
        task.setTitle("!before 20.12.2055 !4 !String");
        task.setDeadline(LocalDate.of(2020, 12, 20));
        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);
        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isEqualTo("2020-12-20");
        assertThat(created.getPriority()).isEqualTo(PriorityEnum.MEDIUM);
    }

    @Test
    public void when_add_task_with_priority_and_deadline_macros_and_priority_isnt_null() {
        Task taskPrior = new Task();
        taskPrior.setTitle("!before 20.12.2055 !4 !String");
        taskPrior.setDeadline(LocalDate.of(2020, 12, 20));
        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task createdPrior = taskService.createTask(taskPrior);
        assertThat(createdPrior.getTitle()).isEqualTo("!String");
        assertThat(createdPrior.getDeadline()).isEqualTo("2020-12-20");
        assertThat(createdPrior.getPriority()).isEqualTo(PriorityEnum.MEDIUM);
    }

    @Test
    public void when_add_task_with_priority_and_deadline_macros_and_deadline_isnt_null() {
        Task taskDead = new Task();
        taskDead.setTitle("!before 20.12.2055 !4 !String");
        taskDead.setPriority(null);
        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task createdDead = taskService.createTask(taskDead);
        assertThat(createdDead.getTitle()).isEqualTo("!String");
        assertThat(createdDead.getDeadline()).isEqualTo("2055-12-20");
        assertThat(createdDead.getPriority()).isEqualTo(PriorityEnum.LOW);

    }

    @Test
    public void when_add_task_check_default_macros_input() {
        Task task = new Task();
        task.setTitle("!before 20.12.2055 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);

        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isEqualTo("2055-12-20");
        assertThat(created.getPriority()).isEqualTo(PriorityEnum.MEDIUM);
    }

    @Test
    public void when_add_task_check_deadline_macros_input() {
        Task task = new Task();
        task.setTitle("!before 20.12.10000 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());
        Task created = taskService.createTask(task);

        assertThat(created.getTitle()).isEqualTo("!before 20.12.10000 !String");
        assertThat(created.getDeadline()).isNull();
    }

    @Test
    public void when_add_task_check_deadline_lowest_macros_input() {
        Task task = new Task();
        task.setTitle("!before 01.01.0001 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());
        Task created = taskService.createTask(task);

        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isEqualTo("0001-01-01");
    }

    @Test
    public void when_add_task_check_deadline_macros_input_zeros() {
        Task task = new Task();
        task.setTitle("!before 00-00-0000 !String");

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());
        Task created = taskService.createTask(task);

        assertThat(created.getTitle()).isEqualTo("!String");
        assertThat(created.getDeadline()).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    public void parametrized_add_task_check_priority_macros_input(int priority) {
        Task task = new Task();
        task.setPriority(null);
        task.setTitle(String.format("!before 20.12.2055 !%d !String", priority));

        when(tasksRepository.save(any(Task.class))).thenReturn(new Task());

        Task created = taskService.createTask(task);

        if (priority == 0 || priority == 5) {
            assertThat(created.getTitle()).isEqualTo(String.format("!%d !String", priority));
            assertThat(created.getPriority()).isEqualTo(PriorityEnum.MEDIUM);
        } else {
            assertThat(created.getTitle()).isEqualTo("!String");
            assertThat(created.getPriority()).isEqualTo(PriorityEnum.values()[priority - 1]);
        }
        assertThat(created.getDeadline()).isEqualTo("2055-12-20");
    }
}
