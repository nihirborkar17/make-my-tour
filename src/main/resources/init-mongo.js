// Users Collection
db.users.insertMany([
    {
        "_id": "user1",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "password": "$2a$10$zGG1K5LLv9riN7tUu5OGXufOLZ0YzxFXXhlkS4R.K9YvY9ED8nKtm", // password: password123
        "role": "USER",
        "phoneNumber": "1234567890",
        "bookings": []
    },
    {
        "_id": "user2",
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane.smith@example.com",
        "password": "$2a$10$zGG1K5LLv9riN7tUu5OGXufOLZ0YzxFXXhlkS4R.K9YvY9ED8nKtm", // password: password123
        "role": "USER",
        "phoneNumber": "9876543210",
        "bookings": []
    },
    {
        "_id": "admin1",
        "firstName": "Admin",
        "lastName": "User",
        "email": "admin@makemytrip.com",
        "password": "$2a$10$zGG1K5LLv9riN7tUu5OGXufOLZ0YzxFXXhlkS4R.K9YvY9ED8nKtm", // password: password123
        "role": "ADMIN",
        "phoneNumber": "5555555555",
        "bookings": []
    }
]);

// Flights Collection
db.flights.insertMany([
    {
        "_id": "flight1",
        "flightName": "Air India AI-101",
        "from": "Mumbai",
        "to": "Delhi",
        "departureTime": "2025-11-07T10:00:00",
        "arrivalTime": "2025-11-07T12:00:00",
        "price": 5999.99,
        "availableSeats": 120
    },
    {
        "_id": "flight2",
        "flightName": "IndiGo 6E-201",
        "from": "Bangalore",
        "to": "Mumbai",
        "departureTime": "2025-11-07T14:00:00",
        "arrivalTime": "2025-11-07T16:00:00",
        "price": 4599.99,
        "availableSeats": 150
    },
    {
        "_id": "flight3",
        "flightName": "SpiceJet SG-301",
        "from": "Delhi",
        "to": "Bangalore",
        "departureTime": "2025-11-07T18:00:00",
        "arrivalTime": "2025-11-07T20:30:00",
        "price": 6299.99,
        "availableSeats": 100
    }
]);

// Hotels Collection
db.hotels.insertMany([
    {
        "_id": "hotel1",
        "hotelName": "Taj Palace",
        "location": "Mumbai",
        "pricePerNight": 12999.99,
        "availableRooms": 50,
        "checkInDate": new Date("2025-11-07T14:00:00Z"),
        "amenities": "WiFi, Pool, Spa, Restaurant, Gym"
    },
    {
        "_id": "hotel2",
        "hotelName": "The Oberoi",
        "location": "Delhi",
        "pricePerNight": 15999.99,
        "availableRooms": 40,
        "checkInDate": new Date("2025-11-07T14:00:00Z"),
        "amenities": "WiFi, Pool, Spa, Restaurant, Gym, Bar"
    },
    {
        "_id": "hotel3",
        "hotelName": "Leela Palace",
        "location": "Bangalore",
        "pricePerNight": 13999.99,
        "availableRooms": 45,
        "checkInDate": new Date("2025-11-07T14:00:00Z"),
        "amenities": "WiFi, Pool, Spa, Restaurant, Gym, Business Center"
    }
]);

// Sample Bookings
db.bookings.insertMany([
    {
        "_id": "booking1",
        "userId": "user1",
        "bookingType": "FLIGHT",
        "referenceId": "flight1",
        "quantity": 2,
        "totalPrice": 11999.98,
        "bookingTime": "2025-11-06T08:00:00",
        "journeyDate": "2025-11-07",
        "status": "CONFIRMED"
    },
    {
        "_id": "booking2",
        "userId": "user2",
        "bookingType": "HOTEL",
        "referenceId": "hotel1",
        "quantity": 1,
        "totalPrice": 12999.99,
        "bookingTime": "2025-11-06T09:00:00",
        "journeyDate": "2025-11-07",
        "status": "CONFIRMED"
    }
]);