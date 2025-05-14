package org.ricramiel.todoserver.service;

import org.ricramiel.todoserver.model.PriorityEnum;
import org.ricramiel.todoserver.model.StatusEnum;
import org.ricramiel.todoserver.model.Task;
import org.ricramiel.todoserver.model.TaskEditDTO;
import org.ricramiel.todoserver.repository.TasksRepository;
import org.ricramiel.todoserver.repository.specifications.TaskSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    StringBuilder sb = new StringBuilder();

    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public Task createTask(Task task) {
        sb.delete(0, sb.length());
        Pattern priorityPattern = Pattern.compile("( |^)![1234]( |$)");
        Matcher priorityMatcher = priorityPattern.matcher(task.getTitle());
        Pattern deadlinePattern = Pattern.compile("( |^)!before (\\d{2}.\\d{2}.\\d{4}|(\\d{2}-\\d{2}-\\d{4}))( |$)");
        Matcher deadlineMatcher = deadlinePattern.matcher(task.getTitle());

        Pattern overAllPattern = Pattern.compile("(\\B![1234]( |$))|(\\B!before (\\d{2}.\\d{2}.\\d{4}|\\d{2}-\\d{2}-\\d{4})( |$))");
        Matcher overAllMatcher = overAllPattern.matcher(task.getTitle());
        if (task.getPriority() == null && priorityMatcher.find()) {
            String macrosPriority = priorityMatcher.group().trim();
            switch (macrosPriority) {
                case "!1": {
                    task.setPriority(PriorityEnum.CRITICAL);
                    break;
                }
                case "!2": {
                    task.setPriority(PriorityEnum.HIGH);
                    break;
                }
                case "!3": {
                    task.setPriority(PriorityEnum.MEDIUM);
                    break;
                }
                case "!4": {
                    task.setPriority(PriorityEnum.LOW);
                    break;
                }
                default: {
                    task.setPriority(PriorityEnum.MEDIUM);
                }
            }
        }

        if (task.getPriority() == null) {
            task.setPriority(PriorityEnum.MEDIUM);
        }

        if (task.getDeadline() == null && deadlineMatcher.find()) {
            String macrosDeadline = deadlineMatcher.group(2).trim();
            String[] temp = macrosDeadline.split("[.\\-]");
            Integer[] dmy = Stream.of(temp).map(Integer::valueOf).toArray(Integer[]::new);
            LocalDate deadline = null;
            try {
                deadline = LocalDate.of(dmy[2], dmy[1], dmy[0]);
            } catch (DateTimeException e) {
                e.printStackTrace();
            }

            task.setDeadline(deadline);
        }
        while (overAllMatcher.find()) {
            overAllMatcher.appendReplacement(sb, "");
        }
        overAllMatcher.appendTail(sb);
        task.setTitle(sb.toString());
        sb.delete(0, sb.length());
        tasksRepository.save(task);
        return task;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = tasksRepository.findAll();
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(recheckDeadline(task));
        }
        tasksRepository.saveAll(result);
        return result;
    }

    public Task getTaskById(Long id) {
        return tasksRepository.findById(id).orElse(null);
    }

    public void deleteTaskById(Long id) {
        tasksRepository.deleteById(id);
    }

    public void updateTask(TaskEditDTO taskEditDTO, Long id) {
        Task task = tasksRepository.findById(id).orElse(null);
        if (task != null) {
            task.setDescription((taskEditDTO.getDescription() != null) ? taskEditDTO.getDescription() : task.getDescription());
            task.setTitle((taskEditDTO.getTitle() != null) ? taskEditDTO.getTitle() : task.getTitle());
            task.setDeadline((taskEditDTO.getDeadline() != null) ? taskEditDTO.getDeadline() : task.getDeadline());
            task.setPriority((taskEditDTO.getPriority() != null) ? taskEditDTO.getPriority() : task.getPriority());
            tasksRepository.save(recheckDeadline(task));
        }
    }

    public void checkDoneTask(Long id) {
        Task task = tasksRepository.findById(id).orElse(null);
        if (task != null) {
            task.setCompleted(!task.getCompleted());
            tasksRepository.save(recheckDeadline(task));
        }
    }

    private Task recheckDeadline(Task task) {
        try {
            if (Boolean.TRUE.equals(task.getCompleted())) {
                if (task.getDeadline().isAfter(LocalDate.now()) || task.getDeadline().isEqual(LocalDate.now())) {
                    task.setStatus(StatusEnum.COMPLETED);
                }
                if (task.getDeadline().isBefore(LocalDate.now())) {
                    task.setStatus(StatusEnum.LATE);
                }
            } else {
                if (task.getDeadline().isAfter(LocalDate.now()) || task.getDeadline().isEqual(LocalDate.now())) {
                    task.setStatus(StatusEnum.ACTIVE);
                }
                if (task.getDeadline().isBefore(LocalDate.now())) {
                    task.setStatus(StatusEnum.OVERDUE);
                }
            }
            return task;
        } catch (Exception e) {
            task.setStatus(StatusEnum.ACTIVE);
            return task;
        }
    }

    public List<Task> getFilteredTasks(String title, String description, LocalDate deadline, StatusEnum status, PriorityEnum priority, LocalDate creationDate, LocalDate editDate) {
        Specification<Task> spec = Specification.where(null);
        if (title != null) {
            spec = spec.and(TaskSpecifications.filterByTitle(title));
        }
        if (description != null) {
            spec = spec.and(TaskSpecifications.filterByDescription(description));
        }
        if (deadline != null) {
            spec = spec.and(TaskSpecifications.filterByDeadline(deadline));
        }
        if (status != null) {
            spec = spec.and(TaskSpecifications.filterByStatus(status));
        }
        if (priority != null) {
            spec = spec.and(TaskSpecifications.filterByPriority(priority));
        }
        if (creationDate != null) {
            spec = spec.and(TaskSpecifications.filterByCreationDate(creationDate));
        }
        if (editDate != null) {
            spec = spec.and(TaskSpecifications.filterByEditDate(editDate));
        }
        List<Task> tasks = tasksRepository.findAll(spec);
        for (Task task : tasks) {
            Task tempTask = tasksRepository.findById(task.getId()).orElse(null);
            if (tempTask != null) {
                tasksRepository.save(recheckDeadline(tempTask));
            }
        }
        return tasks;
    }
}
