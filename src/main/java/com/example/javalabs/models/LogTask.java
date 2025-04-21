package com.example.javalabs.models;

import lombok.Data;

import java.nio.file.Path;

@Data
public class LogTask {
    private String id;
    private String status; // PENDING, COMPLETED, FAILED
    private Path logFile;
    private String errorMessage;
}