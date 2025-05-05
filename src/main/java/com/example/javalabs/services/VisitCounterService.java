package com.example.javalabs.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VisitCounterService {

    private final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();

    public void incrementVisit(String url) {
        counters.computeIfAbsent(url, k -> new AtomicLong(0)).incrementAndGet();
    }

    public long getVisitCount(String url) {
        AtomicLong counter = counters.get(url);
        return counter != null ? counter.get() : 0;
    }

    public ConcurrentHashMap<String, AtomicLong> getAllCounters() {
        return counters;
    }
}