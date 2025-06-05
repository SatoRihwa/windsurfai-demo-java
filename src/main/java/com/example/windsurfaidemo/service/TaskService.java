package com.example.windsurfaidemo.service;

import com.example.windsurfaidemo.model.Task;
import com.example.windsurfaidemo.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Optional<Task> getTaskById(Long id) {
    return taskRepository.findById(id);
  }

  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  public Task updateTask(Long id, Task taskDetails) {
    return taskRepository
        .findById(id)
        .map(
            task -> {
              task.setName(taskDetails.getName());
              task.setDescription(taskDetails.getDescription());
              task.setCompleted(taskDetails.isCompleted());
              return taskRepository.save(task);
            })
        .orElseGet(
            () -> {
              taskDetails.setId(id);
              return taskRepository.save(taskDetails);
            });
  }

  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }

  public Task toggleTaskCompletion(Long id) {
    Task task =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    task.setCompleted(!task.isCompleted());
    return taskRepository.save(task);
  }
}
