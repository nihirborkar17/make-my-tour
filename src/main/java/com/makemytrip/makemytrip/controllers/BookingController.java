package com.makemytrip.makemytrip.controllers;

import com.makemytrip.makemytrip.models.Booking;
import com.makemytrip.makemytrip.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    // ------------------- BOOK FLIGHT -------------------
    @PostMapping("/flight")
    public ResponseEntity<Booking> bookFlight(
            @RequestParam String userId,
            @RequestParam String flightId,
            @RequestParam int seats,
            @RequestParam double price) {

        Booking booking = bookingService.bookFlight(userId, flightId, seats, price);
        return ResponseEntity.ok(booking);
    }

    // ------------------- BOOK HOTEL -------------------
    @PostMapping("/hotel")
    public ResponseEntity<Booking> bookHotel(
            @RequestParam String userId,
            @RequestParam String hotelId,
            @RequestParam int rooms,
            @RequestParam double price) {

        Booking booking = bookingService.bookHotel(userId, hotelId, rooms, price);
        return ResponseEntity.ok(booking);
    }

    // -------- Cancel Booking --------
    @PostMapping("/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @RequestParam String userId,
            @RequestParam String bookingId,
            @RequestParam String reason
    ) {
        Booking booking = bookingService.cancelBooking(userId, bookingId, reason);
        return ResponseEntity.ok(booking);
    }

    // ------------------- GET USER BOOKINGS -------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable String userId) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    // ------------------- GET SINGLE BOOKING -------------------
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable String bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }
}
