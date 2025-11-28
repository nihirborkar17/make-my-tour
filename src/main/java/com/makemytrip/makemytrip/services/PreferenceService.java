package com.makemytrip.makemytrip.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    // userId -> preference key -> value
    private final Map<String, Map<String, Object>> prefs = new ConcurrentHashMap<>();

    public void savePreference(String userId, String key, Object value) {
        prefs.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(key, value);
    }

    public Map<String, Object> getPreferences(String userId) {
        return prefs.getOrDefault(userId, Map.of());
    }
}
