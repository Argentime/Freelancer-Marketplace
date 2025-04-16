package com.example.javalabs.services.impl;

import com.example.javalabs.exceptions.ValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);
    private final String logDir;
    private static final String LOG_FILE_PATTERN = "app-%s.log";

    public LogService() {
        this.logDir = "logs";
    }

    public LogService(String logDir) {
        this.logDir = logDir;
    }

    public Path getLogs(String date, String level) throws IOException {
        try {
            LocalDate targetDate = (date == null) ?
                    LocalDate.now() :
                    LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
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

            LOGGER.info("Retrieved logs from file: {} with level: {}",
                        logFilePath, level != null ? level : "all");

            if (logs.isEmpty()) {
                throw new IOException("No logs found for date: " + targetDate);
            }

            Path tempFile = Files.createTempFile("logs-" + targetDate, ".log");
            Files.write(tempFile, logs);

            return tempFile;
        } catch (DateTimeParseException e) {
            LOGGER.error("Invalid date format: {}", date);
            throw new ValidationException("Invalid date format: " + date + ". Use yyyy-MM-dd");
        } catch (IOException e) {
            LOGGER.error("Error reading log file for date: {}, level: {}", date, level);
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
            LOGGER.warn("Could not extract log level from line: {}", line);
            return "";
        }
    }
}