package com.makemytrip.makemytrip.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReply {
    private String id;
    private String userId;
    private String text;
    private Instant createdAt = Instant.now();
}
