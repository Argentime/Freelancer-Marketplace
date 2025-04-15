package com.example.javalabs.services.impl;

import com.example.javalabs.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PATTERN = "app-%s.log";

    public Path getLogs(String date, String level) throws IOException {
        try {
            LocalDate targetDate = (date == null) ? LocalDate.now() : LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            String logFilePath = String.format(LOG_DIR + "/" + LOG_FILE_PATTERN, targetDate.toString());

            List<String> logs = Files.lines(Paths.get(logFilePath))
                    .filter(line -> !line.trim().isEmpty()) // Исключаем пустые строки
                    .filter(line -> line.length() >= 25) // Убеждаемся, что строка содержит дату и уровень
                    .filter(line -> level == null || extractLogLevel(line).equalsIgnoreCase(level)) // Фильтр по уровню
                    .collect(Collectors.toList());

            logger.info("Retrieved logs from file: {} with level: {}", logFilePath, level != null ? level : "all");

            if (logs.isEmpty()) {
                throw new IOException("No logs found for date: " + targetDate);
            }

             Path tempFile = Files.createTempFile("logs-" + targetDate, ".log");
            Files.write(tempFile, logs);

            return tempFile;
        } catch (IllegalArgumentException e) {
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