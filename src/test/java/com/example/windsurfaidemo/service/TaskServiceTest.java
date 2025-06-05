package com.example.windsurfaidemo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.windsurfaidemo.model.Task;
import com.example.windsurfaidemo.repository.TaskRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskService taskService;

  private Task task1;
  private Task task2;

  @BeforeEach
  void setUp() {
    task1 = new Task(1L, "Task 1", "Description 1", false);
    task2 = new Task(2L, "Task 2", "Description 2", true);
  }

  @Test
  void getAllTasks_ShouldReturnAllTasks() {
    // モックの設定
    when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

    // テスト実行
    List<Task> tasks = taskService.getAllTasks();

    // 検証
    assertEquals(2, tasks.size());
    assertEquals("Task 1", tasks.get(0).getName());
    assertEquals("Task 2", tasks.get(1).getName());
    verify(taskRepository, times(1)).findAll();
  }

  @Test
  void getTaskById_WithValidId_ShouldReturnTask() {
    // モックの設定
    when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

    // テスト実行
    Optional<Task> foundTask = taskService.getTaskById(1L);

    // 検証
    assertTrue(foundTask.isPresent());
    assertEquals("Task 1", foundTask.get().getName());
    verify(taskRepository, times(1)).findById(1L);
  }

  @Test
  void getTaskById_WithInvalidId_ShouldReturnEmpty() {
    // モックの設定
    when(taskRepository.findById(999L)).thenReturn(Optional.empty());

    // テスト実行
    Optional<Task> foundTask = taskService.getTaskById(999L);

    // 検証
    assertFalse(foundTask.isPresent());
    verify(taskRepository, times(1)).findById(999L);
  }

  @Test
  void createTask_ShouldReturnSavedTask() {
    // モックの設定
    Task newTask = new Task("New Task", "New Description");
    Task savedTask = new Task(3L, "New Task", "New Description", false);
    when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

    // テスト実行
    Task createdTask = taskService.createTask(newTask);

    // 検証
    assertNotNull(createdTask.getId());
    assertEquals("New Task", createdTask.getName());
    assertEquals("New Description", createdTask.getDescription());
    assertFalse(createdTask.isCompleted());
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void updateTask_WithExistingId_ShouldUpdateTask() {
    // モックの設定
    Task existingTask = new Task(1L, "Existing Task", "Existing Description", false);
    Task updatedDetails = new Task(1L, "Updated Task", "Updated Description", true);
    when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
    when(taskRepository.save(any(Task.class))).thenReturn(updatedDetails);

    // テスト実行
    Task updatedTask = taskService.updateTask(1L, updatedDetails);

    // 検証
    assertEquals("Updated Task", updatedTask.getName());
    assertEquals("Updated Description", updatedTask.getDescription());
    assertTrue(updatedTask.isCompleted());
    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void updateTask_WithNonExistingId_ShouldCreateNewTask() {
    // モックの設定
    Task newTask = new Task(999L, "New Task", "New Description", false);
    when(taskRepository.findById(999L)).thenReturn(Optional.empty());
    when(taskRepository.save(any(Task.class))).thenReturn(newTask);

    // テスト実行
    Task createdTask = taskService.updateTask(999L, newTask);

    // 検証
    assertEquals("New Task", createdTask.getName());
    verify(taskRepository, times(1)).findById(999L);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void deleteTask_ShouldCallDeleteById() {
    // テスト実行
    taskService.deleteTask(1L);

    // 検証
    verify(taskRepository, times(1)).deleteById(1L);
  }

  @Test
  void toggleTaskCompletion_ShouldToggleCompletedStatus() {
    // モックの設定
    Task task = new Task(1L, "Task 1", "Description 1", false);
    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // テスト実行 (false -> true)
    Task toggledTask = taskService.toggleTaskCompletion(1L);
    assertTrue(toggledTask.isCompleted());

    // テスト実行 (true -> false)
    toggledTask = taskService.toggleTaskCompletion(1L);
    assertFalse(toggledTask.isCompleted());

    // 検証
    verify(taskRepository, times(2)).findById(1L);
    verify(taskRepository, times(2)).save(any(Task.class));
  }

  @Test
  void toggleTaskCompletion_WithNonExistingId_ShouldThrowException() {
    // モックの設定
    when(taskRepository.findById(999L)).thenReturn(Optional.empty());

    // テスト実行と検証
    assertThrows(
        RuntimeException.class,
        () -> taskService.toggleTaskCompletion(999L),
        "Expected toggleTaskCompletion to throw, but it didn't");
    verify(taskRepository, times(1)).findById(999L);
    verify(taskRepository, never()).save(any(Task.class));
  }
}
