package com.example.javalabs.services;

import com.example.javalabs.exceptions.ValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE_PATTERN = "app-%s.log";

    public String getLogs(String date, String level) throws IOException {
        try {
            // Определяем дату: если не указана, используем текущую
            LocalDate targetDate = (date == null) ?
                    LocalDate.now() :
                    LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            String logFilePath = String.format(LOG_DIR + "/" + LOG_FILE_PATTERN, targetDate.toString());

            // Читаем логи из файла
            List<String> logs = Files.lines(Paths.get(logFilePath))
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> line.length() >= 25)
                    .filter(line -> level == null || extractLogLevel(line).equalsIgnoreCase(level))
                    .collect(Collectors.toList());

            LOGGER.info("Retrieved logs from file: {} with level: {}", logFilePath,
                        level != null ? level : "all");

            return logs.isEmpty() ? "No logs found for date: " + targetDate : String.join("\n", logs);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid date format: {}", date);
            throw new ValidationException("Invalid date format: " + date + ". Use yyyy-MM-dd");
        } catch (IOException e) {
            LOGGER.error("Error reading log file for date: {}, level: {}", date, level);
            throw e;
        }
    }

    // Извлекаем уровень логирования из строки (например, "INFO", "ERROR")
    private String extractLogLevel(String line) {
        try {
            // Уровень находится между датой (yyyy-MM-dd HH:mm:ss) и пробелом (длина даты 19, затем пробелы)
            int start = 20; // После "yyyy-MM-dd HH:mm:ss "
            int end = line.indexOf(" ", start);
            return line.substring(start, end).trim();
        } catch (StringIndexOutOfBoundsException e) {
            LOGGER.warn("Could not extract log level from line: {}", line);
            return "";
        }
    }
}