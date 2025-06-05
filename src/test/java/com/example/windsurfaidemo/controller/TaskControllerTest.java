package com.example.windsurfaidemo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.windsurfaidemo.model.Task;
import com.example.windsurfaidemo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TaskService taskService;

  private Task task1;
  private Task task2;

  @BeforeEach
  void setUp() {
    task1 = new Task(1L, "Task 1", "Description 1", false);
    task2 = new Task(2L, "Task 2", "Description 2", true);
  }

  @Test
  void getAllTasks_ShouldReturnAllTasks() throws Exception {
    // モックの設定
    List<Task> tasks = Arrays.asList(task1, task2);
    when(taskService.getAllTasks()).thenReturn(tasks);

    // テスト実行と検証
    mockMvc
        .perform(get("/api/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Task 1"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].name").value("Task 2"));
  }

  @Test
  void getTaskById_WithValidId_ShouldReturnTask() throws Exception {
    // モックの設定
    when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));

    // テスト実行と検証
    mockMvc
        .perform(get("/api/tasks/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Task 1"))
        .andExpect(jsonPath("$.description").value("Description 1"))
        .andExpect(jsonPath("$.completed").value(false));
  }

  @Test
  void getTaskById_WithInvalidId_ShouldReturnNotFound() throws Exception {
    // モックの設定
    when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

    // テスト実行と検証
    mockMvc.perform(get("/api/tasks/999")).andExpect(status().isNotFound());
  }

  @Test
  void createTask_WithValidData_ShouldReturnCreatedTask() throws Exception {
    // モックの設定
    Task newTask = new Task("New Task", "New Description");
    Task savedTask = new Task(3L, "New Task", "New Description", false);
    when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

    // テスト実行と検証
    mockMvc
        .perform(
            post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("New Task"))
        .andExpect(jsonPath("$.description").value("New Description"))
        .andExpect(jsonPath("$.completed").value(false));
  }

  @Test
  void updateTask_WithValidData_ShouldReturnUpdatedTask() throws Exception {
    // モックの設定
    Task updatedTask = new Task(1L, "Updated Task", "Updated Description", true);
    when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(updatedTask);

    // テスト実行と検証
    mockMvc
        .perform(
            put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Updated Task"))
        .andExpect(jsonPath("$.description").value("Updated Description"))
        .andExpect(jsonPath("$.completed").value(true));
  }

  @Test
  void deleteTask_WithValidId_ShouldReturnNoContent() throws Exception {
    // テスト実行と検証
    mockMvc.perform(delete("/api/tasks/1")).andExpect(status().isNoContent());
  }

  @Test
  void toggleTaskCompletion_ShouldToggleCompletedStatus() throws Exception {
    // モックの設定
    Task toggledTask = new Task(1L, "Task 1", "Description 1", true);
    when(taskService.toggleTaskCompletion(1L)).thenReturn(toggledTask);

    // テスト実行と検証
    mockMvc
        .perform(patch("/api/tasks/1/toggle"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.completed").value(true));
  }
}
