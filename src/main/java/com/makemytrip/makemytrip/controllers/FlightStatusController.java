package com.makemytrip.makemytrip.controllers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.makemytrip.makemytrip.services.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightStatusController {

    private final NotificationService notificationService;

    // simple in-memory mock data keyed by flight id
    private static final Map<String, Map<String, Object>> MOCK = new HashMap<>();

    static {
        Map<String, Object> f1 = new HashMap<>();
        f1.put("flightId", "AI101");
        f1.put("status", "On Time");
        f1.put("lastUpdated", Instant.now());
        f1.put("estimatedArrival", Instant.now().plusSeconds(3600 * 2));
        MOCK.put("AI101", f1);

        Map<String, Object> f2 = new HashMap<>();
        f2.put("flightId", "BA202");
        f2.put("status", "Delayed by 1h");
        f2.put("delayReason", "ATC congestion");
        f2.put("lastUpdated", Instant.now());
        f2.put("estimatedArrival", Instant.now().plusSeconds(3600 * 3 + 1800));
        MOCK.put("BA202", f2);
    }

    @GetMapping("/{flightId}/status")
    public ResponseEntity<?> status(@PathVariable String flightId) {
        Map<String, Object> data = MOCK.getOrDefault(flightId, Map.of(
                "flightId", flightId,
                "status", "Unknown",
                "lastUpdated", Instant.now()
        ));
        return ResponseEntity.ok(data);
    }

    @PostMapping("/{flightId}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable String flightId, @RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        if (userId == null) return ResponseEntity.badRequest().body("userId required");
        notificationService.subscribe(flightId, userId);
        return ResponseEntity.ok(Map.of("flightId", flightId, "subscribed", userId));
    }

    @PostMapping("/{flightId}/update")
    public ResponseEntity<?> pushUpdate(@PathVariable String flightId, @RequestBody Map<String, Object> body) {
        // update internal mock state and publish an event
        Map<String, Object> state = MOCK.computeIfAbsent(flightId, k -> new HashMap<>());
        state.putAll(body);
        state.put("lastUpdated", Instant.now());
        notificationService.publishEvent(flightId, Map.of(
                "type", "status_update",
                "payload", body
        ));
        return ResponseEntity.ok(state);
    }

    @GetMapping("/{flightId}/events")
    public ResponseEntity<?> events(@PathVariable String flightId) {
        return ResponseEntity.ok(notificationService.getEvents(flightId));
    }
}
