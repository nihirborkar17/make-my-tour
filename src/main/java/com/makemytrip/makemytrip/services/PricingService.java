package com.makemytrip.makemytrip.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

    // Simple in-memory price history store: entityId -> list of (timestamp, price)
    private final Map<String, List<PricePoint>> history = new ConcurrentHashMap<>();

    public double computePrice(String entityId, double basePrice, int demandPercent) {
        // dynamic multiplier: + (demandPercent / 100) * basePrice * 0.5 as example
        double multiplier = 1.0 + (demandPercent / 100.0) * 0.5;
        double price = Math.round(basePrice * multiplier * 100.0) / 100.0;
        savePoint(entityId, price);
        return price;
    }

    private void savePoint(String entityId, double price) {
        history.computeIfAbsent(entityId, k -> new ArrayList<>()).add(new PricePoint(Instant.now(), price));
    }

    public List<PricePoint> getHistory(String entityId) {
        return history.getOrDefault(entityId, List.of());
    }

    // price freeze store: token -> frozen price + expiry
    private final Map<String, FrozenPrice> freezes = new ConcurrentHashMap<>();

    public String freezePrice(String entityId, double price, int seconds) {
        String token = "freeze-" + java.util.UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(seconds);
        freezes.put(token, new FrozenPrice(entityId, price, expiry));
        return token;
    }

    public FrozenPrice getFrozen(String token) {
        FrozenPrice f = freezes.get(token);
        if (f == null) return null;
        if (f.expiry().isBefore(Instant.now())) {
            freezes.remove(token);
            return null;
        }
        return f;
    }

    public record PricePoint(Instant t, double price) {}

    public record FrozenPrice(String entityId, double price, Instant expiry) {}
}
