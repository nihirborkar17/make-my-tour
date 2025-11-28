package com.makemytrip.makemytrip.services;

import com.makemytrip.makemytrip.models.Booking;
import com.makemytrip.makemytrip.models.Users;
import com.makemytrip.makemytrip.models.Flight;
import com.makemytrip.makemytrip.models.Hotel;
import com.makemytrip.makemytrip.repositories.UserRepository;
import com.makemytrip.makemytrip.repositories.FlightRepository;
import com.makemytrip.makemytrip.repositories.HotelRepository;
import com.makemytrip.makemytrip.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CancellationPolicyService cancellationPolicyService;

    // ✅ BOOK FLIGHT
    public Booking bookFlight(String userId, String flightId, int seats, double price) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Flight> flightOpt = flightRepository.findById(flightId);

        if (userOpt.isEmpty() || flightOpt.isEmpty()) {
            throw new RuntimeException("User or Flight not found");
        }

        Users user = userOpt.get();
        Flight flight = flightOpt.get();

        if (flight.getAvailableSeats() < seats) {
            throw new RuntimeException("Not enough seats available");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - seats);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setBookingType("FLIGHT");
        booking.setReferenceId(flightId);
        booking.setQuantity(seats);
        booking.setTotalPrice(price);
        booking.setBookingTime(LocalDateTime.now().toString());
        booking.setJourneyDate(flight.getDepartureTime());
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        // Add booking to user's embedded bookings list
        user.getBookings().add(booking);
        userRepository.save(user);

        return booking;
    }

    // ✅ BOOK HOTEL
    public Booking bookHotel(String userId, String hotelId, int rooms, double price) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);

        if (userOpt.isEmpty() || hotelOpt.isEmpty()) {
            throw new RuntimeException("User or Hotel not found");
        }

        Users user = userOpt.get();
        Hotel hotel = hotelOpt.get();

        if (hotel.getAvailableRooms() < rooms) {
            throw new RuntimeException("Not enough rooms available");
        }

        hotel.setAvailableRooms(hotel.getAvailableRooms() - rooms);
        hotelRepository.save(hotel);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setBookingType("HOTEL");
        booking.setReferenceId(hotelId);
        booking.setQuantity(rooms);
        booking.setTotalPrice(price);
        booking.setBookingTime(LocalDateTime.now().toString());
        booking.setJourneyDate(hotel.getCheckInDate().toString());
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        // Add booking to user's embedded bookings list
        user.getBookings().add(booking);
        userRepository.save(user);

        return booking;
    }
    // ------------------- CANCEL BOOKING ------------------- //
    public Booking cancelBooking(String userId, String bookingId, String reason) {
        // Load booking from repository
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }

        Booking booking = bookingOptional.get();

        // Validate ownership
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("User does not own this booking");
        }

        // Check if already cancelled
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // Compute refund percentage (example: 50% if <24h, else 80%)
        double refundPercentage = calculateRefundPercentage(booking.getJourneyDate());
        double refundAmount = booking.getTotalPrice() * refundPercentage;

        booking.setStatus("CANCELLED");
        booking.setRefundAmount(refundAmount);
        booking.setCancellationReason(reason);
        booking.setRefundStatus("INITIATED");
        booking.setCancellationTime(LocalDateTime.now());

        // Handle seat/room restoration
        if ("FLIGHT".equalsIgnoreCase(booking.getBookingType())) {
            flightRepository.findById(booking.getReferenceId()).ifPresent(flight -> {
                flight.setAvailableSeats(flight.getAvailableSeats() + booking.getQuantity());
                flightRepository.save(flight);
            });
        } else if ("HOTEL".equalsIgnoreCase(booking.getBookingType())) {
            hotelRepository.findById(booking.getReferenceId()).ifPresent(hotel -> {
                hotel.setAvailableRooms(hotel.getAvailableRooms() + booking.getQuantity());
                hotelRepository.save(hotel);
            });
        }

        // Save booking changes
        bookingRepository.save(booking);

        // Also update user's embedded bookings list (remove the cancelled booking)
        userRepository.findById(userId).ifPresent(u -> {
            u.getBookings().removeIf(b -> bookingId.equals(b.getId()));
            userRepository.save(u);
        });

        return booking;
    }

    // ------------------- REFUND POLICY LOGIC ------------------- //
    private double calculateRefundPercentage(String bookingDate) {
        LocalDate date = LocalDate.parse(bookingDate);
        long daysDiff = ChronoUnit.DAYS.between(date, LocalDate.now());

        if (daysDiff < 1) {
            return 0.5; // 50% refund if cancelled within 24 hours
        } else if (daysDiff < 3) {
            return 0.8; // 80% if cancelled within 3 days
        } else {
            return 1.0; // 100% if before 3+ days
        }
    }
    // ------------------- GET USER BOOKINGS ------------------- //
    public List<Booking> getUserBookings(String userId) {
        // Validate user exists
        Optional<Users> usersOptional = userRepository.findById(userId);
        if (usersOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // Fetch all bookings from bookingRepository (ensures up-to-date data)
        return bookingRepository.findByUserId(userId);
    }

    // ------------------- GET SINGLE BOOKING ------------------- //
    public Booking getBookingById(String bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        return bookingOptional.get();
    }

}