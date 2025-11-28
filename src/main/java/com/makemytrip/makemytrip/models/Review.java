package com.makemytrip.makemytrip.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    private String id;

    // id of the flight or hotel this review is for
    private String entityId;
    // type: "flight" or "hotel"
    private String entityType;

    private String userId;
    private int rating; // 1-5
    private String text;
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    @Builder.Default
    private List<ReviewReply> replies = new ArrayList<>();

    @Builder.Default
    private int helpfulCount = 0;

    @Builder.Default
    private boolean flagged = false;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
