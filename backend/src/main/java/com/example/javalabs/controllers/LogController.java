package com.example.javalabs.controllers;

import com.example.javalabs.models.LogTask;
import com.example.javalabs.services.LogService;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<String> createLogFile(@RequestParam(required = false) String date,
                                                @RequestParam(required = false) String level) {
        CompletableFuture<String> taskIdFuture = logService.createLogFileAsync(date, level);
        return ResponseEntity.ok(taskIdFuture.join());
    }

    @GetMapping("/status/{taskId}")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId) {
        LogTask task = logService.getTaskStatus(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/file/{taskId}")
    public ResponseEntity<?> getLogFile(@PathVariable String taskId) {
        LogTask task = logService.getTaskStatus(taskId);
        if (task == null) {
            return ResponseEntity.status(404).body("Task not found");
        }
        if (!"COMPLETED".equals(task.getStatus())) {
            return ResponseEntity.status(202).body("File is not ready yet");
        }
        Path filePath = logService.getLogFile(taskId);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving file: " + e.getMessage());
        }
    }
}