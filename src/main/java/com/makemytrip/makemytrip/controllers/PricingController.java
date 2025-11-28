package com.makemytrip.makemytrip.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.makemytrip.makemytrip.services.PricingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @GetMapping("/{entityId}")
    public ResponseEntity<?> getPrice(@PathVariable String entityId,
            @RequestParam(defaultValue = "100.0") double base,
            @RequestParam(defaultValue = "0") int demandPercent) {
        double price = pricingService.computePrice(entityId, base, demandPercent);
        return ResponseEntity.ok(Map.of("entityId", entityId, "price", price));
    }

    @GetMapping("/{entityId}/history")
    public ResponseEntity<?> history(@PathVariable String entityId) {
        return ResponseEntity.ok(pricingService.getHistory(entityId));
    }

    @PostMapping("/freeze")
    public ResponseEntity<?> freeze(@RequestBody Map<String,Object> body) {
        String entityId = (String) body.get("entityId");
        double price = Double.parseDouble(String.valueOf(body.getOrDefault("price", "0")));
        int seconds = Integer.parseInt(String.valueOf(body.getOrDefault("seconds", "3600")));
        String token = pricingService.freezePrice(entityId, price, seconds);
        return ResponseEntity.ok(Map.of("entityId", entityId, "frozenPrice", price, "token", token, "validForSeconds", seconds));
    }
}
