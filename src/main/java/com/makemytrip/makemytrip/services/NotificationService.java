package com.makemytrip.makemytrip.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // flightId -> list of subscriber userIds
    private final Map<String, List<String>> subscribers = new ConcurrentHashMap<>();

    // flightId -> events
    private final Map<String, List<Map<String, Object>>> events = new ConcurrentHashMap<>();

    public void subscribe(String flightId, String userId) {
        subscribers.computeIfAbsent(flightId, k -> new ArrayList<>()).add(userId);
    }

    public void publishEvent(String flightId, Map<String, Object> event) {
        event.putIfAbsent("id", UUID.randomUUID().toString());
        event.putIfAbsent("timestamp", Instant.now());
        events.computeIfAbsent(flightId, k -> new ArrayList<>()).add(event);
    }

    public List<Map<String, Object>> getEvents(String flightId) {
        return events.getOrDefault(flightId, List.of());
    }

    public List<String> getSubscribers(String flightId) {
        return subscribers.getOrDefault(flightId, List.of());
    }
}
