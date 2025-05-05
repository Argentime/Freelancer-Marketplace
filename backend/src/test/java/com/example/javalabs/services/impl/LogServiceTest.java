package com.example.javalabs.services.impl;

import com.example.javalabs.exceptions.ValidationException;
import com.example.javalabs.services.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogServiceTest {

    private LogService logService;
    private Path logDir;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        logDir = tempDir.resolve("logs");
        Files.createDirectories(logDir);
        logService = new LogService(logDir.toString());
    }

    @Test
    void getLogs_validDateAndLevel_returnsFilteredLogs() throws IOException {
        String date = "2025-04-15";
        String logFileName = "app-" + date + ".log";
        Path logFile = logDir.resolve(logFileName);
        List<String> logLines = List.of(
                "2025-04-15 10:00:00 INFO  App started",
                "2025-04-15 10:01:00 ERROR Failed operation",
                "2025-04-15 10:02:00 INFO  Operation successful"
        );
        Files.write(logFile, logLines);

        Path result = logService.getLogs(date, "INFO");

        List<String> resultLines = Files.readAllLines(result);
        assertEquals(2, resultLines.size(), "Expected 2 INFO logs");
        assertTrue(resultLines.contains("2025-04-15 10:00:00 INFO  App started"));
        assertTrue(resultLines.contains("2025-04-15 10:02:00 INFO  Operation successful"));
        assertFalse(resultLines.contains("2025-04-15 10:01:00 ERROR Failed operation"));
    }

    @Test
    void getLogs_noDate_returnsTodayLogs() throws IOException {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String logFileName = "app-" + date + ".log";
        Path logFile = logDir.resolve(logFileName);
        List<String> logLines = List.of(
                "2025-04-15 10:00:00 INFO  App started"
        );
        Files.write(logFile, logLines);

        Path result = logService.getLogs(null, null);

        List<String> resultLines = Files.readAllLines(result);
        assertEquals(1, resultLines.size(), "Expected 1 log line");
        assertEquals("2025-04-15 10:00:00 INFO  App started", resultLines.get(0));
    }

    @Test
    void getLogs_invalidDate_throwsValidationException() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> logService.getLogs("invalid-date", null));

        assertEquals("Invalid date format: invalid-date. Use yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void getLogs_noLogs_throwsIOException() {
        String date = "2025-04-15";

        IOException exception = assertThrows(IOException.class,
                () -> logService.getLogs(date, null));

        assertTrue(exception.getMessage().contains("No logs found for date: 2025-04-15"));
    }

    @Test
    void getLogs_emptyLines_filteredOut() throws IOException {
        String date = "2025-04-15";
        String logFileName = "app-" + date + ".log";
        Path logFile = logDir.resolve(logFileName);
        List<String> logLines = List.of(
                "2025-04-15 10:00:00 INFO  App started",
                "",
                "   ",
                "2025-04-15 10:01:00 ERROR Failed operation"
        );
        Files.write(logFile, logLines);

        Path result = logService.getLogs(date, null);

        List<String> resultLines = Files.readAllLines(result);
        assertEquals(2, resultLines.size(), "Expected 2 non-empty log lines");
        assertTrue(resultLines.contains("2025-04-15 10:00:00 INFO  App started"));
        assertTrue(resultLines.contains("2025-04-15 10:01:00 ERROR Failed operation"));
    }

    @Test
    void extractLogLevel_validLine_returnsLevel() {
        String line = "2025-04-15 10:00:00 INFO  App started";
        try {
            Method method = LogService.class.getDeclaredMethod("extractLogLevel", String.class);
            method.setAccessible(true);
            String level = (String) method.invoke(logService, line);
            assertEquals("INFO", level);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke extractLogLevel: " + e.getMessage());
        }
    }

    @Test
    void extractLogLevel_invalidLine_returnsEmpty() {
        String line = "Short line";
        try {
            Method method = LogService.class.getDeclaredMethod("extractLogLevel", String.class);
            method.setAccessible(true);
            String level = (String) method.invoke(logService, line);
            assertEquals("", level);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke extractLogLevel: " + e.getMessage());
        }
    }
}