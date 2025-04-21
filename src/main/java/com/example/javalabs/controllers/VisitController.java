package com.example.javalabs.controllers;

import com.example.javalabs.services.VisitCounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitCounterService visitCounterService;

    public VisitController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Long>> getVisitCounts(@RequestParam(required = false) String url) {
        if (url != null) {
            return ResponseEntity.ok(Map.of(url, visitCounterService.getVisitCount(url)));
        }
        Map<String, Long> counts = visitCounterService.getAllCounters().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
        return ResponseEntity.ok(counts);
    }
}