package com.makemytrip.makemytrip.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String bookingType; // FLIGHT or HOTEL
    private String referenceId; // flightId or hotelId
    private int quantity;
    private double totalPrice;
    private String bookingTime;
    private String journeyDate;

    // Cancellation & Refund fields
    private String status = "CONFIRMED";  // CONFIRMED, CANCELED
    private String cancellationReason;
    private LocalDateTime cancellationTime;
    private Double refundAmount;
    private String refundStatus; // PENDING, PROCESSED, FAILED

    // Getters and setters
    // (or use Lombok @Data if you have it configured)
}
