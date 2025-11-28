# Spring Boot Backend Integration Guide

## Overview
This Next.js frontend is now configured to integrate with your Spring Boot backend REST APIs for flights and hotels management.

## Setup Instructions

### 1. Configure Backend URL
The frontend is configured to use environment variables for the backend URL:

**Development Environment:**
- Update `.env.local` file in the root directory
- Set `NEXT_PUBLIC_BACKEND_URL` to your Spring Boot backend URL
  ```
  NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
  ```

**Production Environment:**
- Create `.env.production.local` file
  ```
  NEXT_PUBLIC_BACKEND_URL=https://your-backend-api.com
  ```

### 2. API Endpoints Expected

Your Spring Boot backend should have the following endpoints:

#### User Management
- `POST /user/login` - Login user
- `POST /user/signup` - Register new user
- `GET /user/email?email={email}` - Get user by email
- `POST /user/edit?id={id}` - Edit user profile

#### Flight Management
- `GET /flight` - Get all flights
- `POST /admin/flight` - Add new flight
- `PUT /admin/flight/{id}` - Update flight

#### Hotel Management
- `GET /hotel` - Get all hotels
- `POST /admin/hotel` - Add new hotel
- `PUT /admin/hotel/{id}` - Update hotel

#### Booking
- `POST /booking/flight` - Book a flight
  - Query params: `userId`, `flightId`, `seats`, `price`
- `POST /booking/hotel` - Book a hotel
  - Query params: `userId`, `hotelId`, `rooms`, `price`

### 3. Data Structure Expected

#### Flight Object
```json
{
  "_id": "MongoDB ObjectId",
  "flightName": "string",
  "from": "string",
  "to": "string",
  "departureTime": "ISO 8601 datetime",
  "arrivalTime": "ISO 8601 datetime",
  "price": "number",
  "availableSeats": "number"
}
```

#### Hotel Object
```json
{
  "_id": "MongoDB ObjectId",
  "hotelName": "string",
  "location": "string",
  "pricePerNight": "number",
  "availableRooms": "number",
  "amenities": "string"
}
```

#### User Object
```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string",
  "bookings": []
}
```

### 4. CORS Configuration

Your Spring Boot backend needs to allow requests from your frontend domain. Add CORS configuration:

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000", "http://localhost:3001") // Add your frontend URL
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

### 5. Running the Application

```bash
# Install dependencies (if not already done)
npm install

# Start development server
npm run dev

# The app will be available at http://localhost:3000
```

### 6. Testing the Integration

1. **Home Page (/)** - Fetches all flights and hotels from backend
   - Search and filter flights/hotels
   - Book now redirects to detail pages

2. **Book Flight Page (/book-flight/[id])** - Shows flight details with booking
   - Displays flight information from MongoDB
   - Handles flight booking

3. **Book Hotel Page (/book-hotel/[id])** - Shows hotel details with booking
   - Displays hotel information from MongoDB
   - Handles hotel booking

4. **Admin Panel (/admin)** - Manage flights and hotels
   - Add new flights/hotels
   - Edit existing flights/hotels
   - View and search users

## Fixed Issues

✅ **Backend URL Configuration** - Now uses environment variables instead of hardcoded string

✅ **MongoDB ID Mapping** - Components now correctly use `_id` instead of `id` for filtering

✅ **Hotel Booking Endpoint** - Fixed incorrect endpoint path (`/booking/hotel` instead of `/booking/flight`)

✅ **Error Handling** - All API functions now properly throw and handle errors

✅ **Data Flow** - Properly typed and structured for TypeScript components

## Common Issues & Troubleshooting

### Issue: "Cannot GET /api..." or API calls fail
**Solution:** 
- Verify `NEXT_PUBLIC_BACKEND_URL` is correctly set in `.env.local`
- Ensure Spring Boot backend is running
- Check CORS configuration on backend

### Issue: Flights/Hotels not showing on home page
**Solution:**
- Verify database has data
- Check network tab in browser DevTools
- Review error logs in both frontend and backend

### Issue: Booking fails
**Solution:**
- Ensure user is logged in (stored in Redux state)
- Verify user ID is being sent correctly
- Check backend booking endpoint response format

## File Structure

```
src/
├── api/index.js          # API functions (✅ FIXED)
├── pages/
│   ├── index.tsx         # Home page
│   ├── admin/index.tsx   # Admin panel
│   ├── profile/index.tsx # User profile
│   ├── book-flight/[id]/ # Flight booking (✅ FIXED)
│   └── book-hotel/[id]/  # Hotel booking (✅ FIXED)
├── components/
│   ├── Flights/          # Flight components
│   ├── Hotel/            # Hotel components
│   └── ui/               # UI components
└── store/                # Redux store
```

## Next Steps

1. Update `.env.local` with your backend URL
2. Ensure Spring Boot backend is running
3. Test API connections using browser DevTools Network tab
4. Verify data is displaying correctly on all pages
5. Test booking flow end-to-end

Good luck! 🚀
