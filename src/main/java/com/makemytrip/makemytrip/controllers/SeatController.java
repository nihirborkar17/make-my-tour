package com.makemytrip.makemytrip.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    // in-memory seat maps for demos: flightId -> seatId -> seat metadata map
    private static final Map<String, Map<String, Map<String, Object>>> SEAT_MAPS = new HashMap<>();

    static {
        Map<String, Map<String, Object>> m = new HashMap<>();
        // create 30 seats: 1A..5F
        String[] rows = {"1","2","3","4","5"};
        String[] cols = {"A","B","C","D","E","F"};
        for (String r: rows) for (String c: cols) {
            String seat = r + c;
            Map<String, Object> meta = new HashMap<>();
            meta.put("reservedBy", null);
            // make aisle/window in a simple way and mark some as premium
            boolean premium = (r.equals("1") || c.equals("A") || c.equals("F")) && Integer.parseInt(r) <= 2;
            meta.put("isPremium", premium);
            meta.put("basePrice", premium ? 50.0 : 10.0);
            m.put(seat, meta);
        }
        SEAT_MAPS.put("AI101", m);
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<?> getSeatMap(@PathVariable String flightId) {
        Map<String, Map<String, Object>> map = SEAT_MAPS.getOrDefault(flightId, new HashMap<>());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/{flightId}/reserve")
    public ResponseEntity<?> reserve(@PathVariable String flightId, @RequestBody Map<String,String> body) {
        String seatId = body.get("seatId");
        String userId = body.get("userId");
        SEAT_MAPS.computeIfAbsent(flightId, k -> new HashMap<>());
        Map<String, Map<String, Object>> map = SEAT_MAPS.get(flightId);
        if (seatId == null || userId == null) return ResponseEntity.badRequest().body("seatId and userId required");
        Map<String, Object> seatMeta = map.get(seatId);
        if (seatMeta == null) return ResponseEntity.badRequest().body("unknown seat");
        String current = (String) seatMeta.get("reservedBy");
        if (current != null) return ResponseEntity.badRequest().body("seat already reserved");
        seatMeta.put("reservedBy", userId);
        return ResponseEntity.ok(Map.of("seatId", seatId, "reservedBy", userId));
    }

    @GetMapping("/{flightId}/upsell")
    public ResponseEntity<?> getUpsellSuggestions(@PathVariable String flightId) {
        Map<String, Map<String, Object>> map = SEAT_MAPS.getOrDefault(flightId, new HashMap<>());
        Map<String, Object> result = new HashMap<>();
        map.forEach((seatId, meta) -> {
            boolean premium = Boolean.TRUE.equals(meta.get("isPremium"));
            if (premium && meta.get("reservedBy") == null) {
                result.put(seatId, Map.of(
                        "additionalPrice", meta.get("basePrice"),
                        "reason", "Extra legroom / window"
                ));
            }
        });
        return ResponseEntity.ok(result);
    }
}
