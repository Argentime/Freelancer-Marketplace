package com.example.javalabs.services;

import com.example.javalabs.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private static final String LOG_FILE_PATH = "logs/app.log";

    public String getLogsByDate(String date) throws IOException {
        try {
            List<String> logs;
            if (date == null) {
                logs = Files.readAllLines(Paths.get(LOG_FILE_PATH));
                logger.info("Retrieved all logs from file: {}", LOG_FILE_PATH);
            } else {
                LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
                logs = Files.lines(Paths.get(LOG_FILE_PATH))
                        .filter(line -> line.length() >= 10 && line.substring(0, 10).equals(targetDate.toString()))
                        .collect(Collectors.toList());
                logger.info("Retrieved logs for date: {}", date);
            }
            return logs.isEmpty() ? "No logs found" : String.join("\n", logs);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid date format: {}", date);
            throw new ValidationException("Invalid date format: " + date + ". Use yyyy-MM-dd");
        } catch (IOException e) {
            logger.error("Error reading log file: {}", e.getMessage());
            throw e;
        }
    }
}