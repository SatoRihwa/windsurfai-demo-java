package com.example.windsurfaidemo.controller;

import com.example.windsurfaidemo.model.Task;
import com.example.windsurfaidemo.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * タスクのCRUD操作を行うコントローラーです。
 *
 * <ul>
 *   <li>GET /api/tasks: 全てのタスクを取得します
 *   <li>GET /api/tasks/{id}: IDで指定されたタスクを取得します
 *   <li>POST /api/tasks: 新しいタスクを作成します
 *   <li>PUT /api/tasks/{id}: 指定されたIDのタスクを更新します
 *   <li>DELETE /api/tasks/{id}: 指定されたIDのタスクを削除します
 *   <li>PATCH /api/tasks/{id}/toggle: 指定されたIDのタスクの完了状態を切り替えます
 * </ul>
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

  private final TaskService taskService;

  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  // 全てのタスクを取得します
  @GetMapping
  public List<Task> getAllTasks() {
    return taskService.getAllTasks();
  }

  // IDで指定されたタスクを取得します
  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
    return taskService
        .getTaskById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // 新しいタスクを作成します
  @PostMapping
  public Task createTask(@RequestBody Task task) {
    return taskService.createTask(task);
  }

  // 指定されたIDのタスクを更新します
  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
    Task updatedTask = taskService.updateTask(id, taskDetails);
    return ResponseEntity.ok(updatedTask);
  }

  // 指定されたIDのタスクを削除します
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  // 指定されたIDのタスクの完了状態を切り替えます
  @PatchMapping("/{id}/toggle")
  public ResponseEntity<Task> toggleTaskCompletion(@PathVariable Long id) {
    Task toggledTask = taskService.toggleTaskCompletion(id);
    return ResponseEntity.ok(toggledTask);
  }
}
