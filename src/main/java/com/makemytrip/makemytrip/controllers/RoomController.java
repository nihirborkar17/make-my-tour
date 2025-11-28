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

import com.makemytrip.makemytrip.services.PreferenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class RoomController {

    private final PreferenceService preferenceService;

    // Demo in-memory rooms per hotel
    private static final Map<String, Map<String, Map<String, Object>>> HOTELS = new HashMap<>();

    static {
        Map<String, Map<String, Object>> h1 = new HashMap<>();
        h1.put("R101", Map.of("type", "Deluxe", "bed", "King", "price", 120.0, "preview3d", "https://example.com/3d/R101.glb"));
        h1.put("R102", Map.of("type", "Standard", "bed", "Queen", "price", 80.0, "preview3d", "https://example.com/3d/R102.glb"));
        HOTELS.put("HILTON-MUMBAI", h1);
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<?> rooms(@PathVariable String hotelId) {
        return ResponseEntity.ok(HOTELS.getOrDefault(hotelId, Map.of()));
    }

    @GetMapping("/{hotelId}/rooms/{roomId}/preview")
    public ResponseEntity<?> preview(@PathVariable String hotelId, @PathVariable String roomId) {
        Map<String, Object> room = HOTELS.getOrDefault(hotelId, Map.of()).get(roomId);
        if (room == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("preview3d", room.get("preview3d")));
    }

    @PostMapping("/{hotelId}/rooms/{roomId}/reserve")
    public ResponseEntity<?> reserve(@PathVariable String hotelId, @PathVariable String roomId, @RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        if (userId == null) return ResponseEntity.badRequest().body("userId required");
        // demo: no persistence, just echo
        return ResponseEntity.ok(Map.of("hotelId", hotelId, "roomId", roomId, "reservedBy", userId));
    }

    @PostMapping("/preferences/room")
    public ResponseEntity<?> saveRoomPref(@RequestBody Map<String,Object> body) {
        String userId = (String) body.get("userId");
        String hotelId = (String) body.get("hotelId");
        String pref = (String) body.get("pref");
        if (userId == null || hotelId == null || pref == null) return ResponseEntity.badRequest().body("userId, hotelId and pref required");
        preferenceService.savePreference(userId, "room:" + hotelId, pref);
        return ResponseEntity.ok(Map.of("saved", true));
    }
}
