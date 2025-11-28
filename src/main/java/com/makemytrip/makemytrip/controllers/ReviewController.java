package com.makemytrip.makemytrip.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.makemytrip.makemytrip.models.Review;
import com.makemytrip.makemytrip.models.ReviewReply;
import com.makemytrip.makemytrip.repositories.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review r) {
        Review saved = reviewRepository.save(r);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@RequestParam String entityId,
            @RequestParam(required = false, defaultValue = "helpful") String sort) {
        List<Review> list = reviewRepository.findByEntityId(entityId);
        if ("helpful".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparingInt(Review::getHelpfulCount).reversed());
        } else if ("newest".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Review::getCreatedAt).reversed());
        } else if ("rating".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparingInt(Review::getRating).reversed());
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/flag")
    public ResponseEntity<?> flagReview(@PathVariable String id) {
        Optional<Review> o = reviewRepository.findById(id);
        if (o.isEmpty()) return ResponseEntity.notFound().build();
        Review r = o.get();
        r.setFlagged(true);
        reviewRepository.save(r);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/helpful")
    public ResponseEntity<?> markHelpful(@PathVariable String id) {
        Optional<Review> o = reviewRepository.findById(id);
        if (o.isEmpty()) return ResponseEntity.notFound().build();
        Review r = o.get();
        r.setHelpfulCount(r.getHelpfulCount() + 1);
        reviewRepository.save(r);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<?> replyToReview(@PathVariable String id, @RequestBody ReviewReply reply) {
        Optional<Review> o = reviewRepository.findById(id);
        if (o.isEmpty()) return ResponseEntity.notFound().build();
        Review r = o.get();
        r.getReplies().add(reply);
        reviewRepository.save(r);
        return ResponseEntity.ok().build();
    }
}
