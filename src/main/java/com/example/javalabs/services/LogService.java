package com.example.javalabs.services;

import com.example.javalabs.exceptions.ValidationException;
import com.example.javalabs.models.LogTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private final String logDir;
    private static final String LOG_FILE_PATTERN = "app-%s.log";
    private final ConcurrentHashMap<String, LogTask> tasks = new ConcurrentHashMap<>();

    public LogService() {
        this.logDir = System.getProperty("log.dir", "logs");
    }

    public LogService(String logDir) {
        this.logDir = logDir;
    }

    @Async
    public CompletableFuture<String> createLogFileAsync(String date, String level) {
        String taskId = UUID.randomUUID().toString();
        LogTask task = new LogTask();
        task.setId(taskId);
        task.setStatus("PENDING");
        tasks.put(taskId, task);

        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(20000);
                Path result = getLogs(date, level);
                task.setLogFile(result);
                task.setStatus("COMPLETED");
                logger.info("Log file created for task {}", taskId);
            } catch (InterruptedException e) {
                task.setStatus("FAILED");
                task.setErrorMessage("Task interrupted: " + e.getMessage());
                logger.error("Task {} interrupted: {}", taskId, e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                task.setStatus("FAILED");
                task.setErrorMessage(e.getMessage());
                logger.error("Failed to create log file for task {}: {}", taskId, e.getMessage());
            }
            return taskId;
        });

        return CompletableFuture.completedFuture(taskId);
    }

    public LogTask getTaskStatus(String taskId) {
        return tasks.get(taskId);
    }

    public Path getLogFile(String taskId) {
        LogTask task = tasks.get(taskId);
        return (task != null && "COMPLETED".equals(task.getStatus())) ? task.getLogFile() : null;
    }

    public Path getLogs(String date, String level) throws IOException {
        try {
            LocalDate targetDate = (date == null) ? LocalDate.now() : LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            String logFilePath = String.format(logDir + "/" + LOG_FILE_PATTERN, targetDate.toString());
            Path path = Paths.get(logFilePath);

            if (!Files.exists(path)) {
                throw new IOException("No logs found for date: " + targetDate);
            }

            List<String> logs = Files.lines(path)
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> line.length() >= 25)
                    .filter(line -> level == null || extractLogLevel(line).equalsIgnoreCase(level))
                    .collect(Collectors.toList());

            logger.info("Retrieved logs from file: {} with level: {}", logFilePath, level != null ? level : "all");

            if (logs.isEmpty()) {
                throw new IOException("No logs found for date: " + targetDate);
            }

            Path tempFile = Files.createTempFile("logs-" + targetDate, ".log");
            Files.write(tempFile, logs);

            return tempFile;
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", date);
            throw new ValidationException("Invalid date format: " + date + ". Use yyyy-MM-dd");
        } catch (IOException e) {
            logger.error("Error reading log file for date: {}, level: {}", date, level);
            throw e;
        }
    }

    private String extractLogLevel(String line) {
        try {
            int start = 20;
            int end = line.indexOf(" ", start);
            if (end == -1) return "";
            return line.substring(start, end).trim();
        } catch (StringIndexOutOfBoundsException e) {
            logger.warn("Could not extract log level from line: {}", line);
            return "";
        }
    }
}