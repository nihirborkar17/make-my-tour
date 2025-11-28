# Complete Integration Checklist & Test Guide

## ✅ What's Been Done (4 Critical Fixes)

### Fix #1: Backend URL Configuration
- [x] Replaced hardcoded `"your backend url"` 
- [x] Now uses environment variables: `process.env.NEXT_PUBLIC_BACKEND_URL`
- [x] Created `.env.local` with example configuration
- [x] Supports both development and production URLs

**File:** `src/api/index.js` (lines 1-5)

### Fix #2: MongoDB ID Field Mapping
- [x] Fixed flight detail page ID filtering: `flight._id` instead of `flight.id`
- [x] Fixed hotel detail page ID filtering: `hotel._id` instead of `hotel.id`
- [x] Components now correctly fetch data from MongoDB

**Files:** 
- `src/pages/book-flight/[id]/index.tsx` (line ~60)
- `src/pages/book-hotel/[id]/index.tsx` (line ~51)

### Fix #3: Hotel Booking Endpoint (CRITICAL)
- [x] Changed wrong endpoint: `/booking/flight` → `/booking/hotel`
- [x] Hotel bookings now call correct backend endpoint
- [x] Prevents data corruption and booking failures

**File:** `src/api/index.js` (line 211)

### Fix #4: Error Handling Improvement
- [x] All API functions now throw errors instead of silently failing
- [x] Better console error messages for debugging
- [x] Errors propagate to components for proper handling

**Files:** `src/api/index.js` (all error blocks)

---

## 🎯 Pre-Launch Checklist

### ✅ Environment Setup
- [ ] `.env.local` file exists in project root
- [ ] `NEXT_PUBLIC_BACKEND_URL` is set to your backend
- [ ] Spring Boot backend is running on that URL
- [ ] MongoDB is connected to Spring Boot

### ✅ Backend Verification
- [ ] Spring Boot running on configured port (default: 8080)
- [ ] CORS enabled for `http://localhost:3000` and production domain
- [ ] All required endpoints are implemented:
  - [ ] `GET /flight`
  - [ ] `GET /hotel`
  - [ ] `POST /booking/flight`
  - [ ] `POST /booking/hotel`
  - [ ] `POST /user/login`
  - [ ] `POST /user/signup`
  - [ ] `GET /user/email`
  - [ ] `POST /user/edit`
  - [ ] `POST /admin/flight`
  - [ ] `PUT /admin/flight/{id}`
  - [ ] `POST /admin/hotel`
  - [ ] `PUT /admin/hotel/{id}`

### ✅ Data Structure Verification
- [ ] MongoDB collections have sample data
- [ ] Flight documents have all required fields: `_id`, `flightName`, `from`, `to`, `departureTime`, `arrivalTime`, `price`, `availableSeats`
- [ ] Hotel documents have all required fields: `_id`, `hotelName`, `location`, `pricePerNight`, `availableRooms`, `amenities`
- [ ] Response format is JSON array (not wrapped)

### ✅ Frontend Setup
- [ ] All dependencies installed: `npm install`
- [ ] No build errors: `npm run build`
- [ ] Development server starts: `npm run dev`

---

## 🧪 Testing Procedure

### Test 1: API Connectivity
**What to do:**
1. Open terminal and run: `npm run dev`
2. Open http://localhost:3000 in browser
3. Press F12 to open DevTools
4. Go to Network tab
5. On the home page, look for API requests

**Expected Results:**
- [ ] Requests to `/flight` return 200 status
- [ ] Requests to `/hotel` return 200 status
- [ ] Response contains array of flights/hotels
- [ ] Each flight has `_id` field
- [ ] Each hotel has `_id` field

**If failing:**
- Check `.env.local` has correct `NEXT_PUBLIC_BACKEND_URL`
- Verify Spring Boot backend is running
- Check CORS configuration on backend
- Look at Network tab → Response tab for error details

### Test 2: Home Page Functionality
**What to do:**
1. Stay on home page
2. Verify flights and hotels are displayed
3. Try searching flights (select from/to cities)
4. Try searching hotels (select location)
5. Click "Book Now" on any item

**Expected Results:**
- [ ] Flights display in a list (from backend data)
- [ ] Hotels display in a list (from backend data)
- [ ] Search filters data correctly
- [ ] "Book Now" redirects to detail page with ID in URL
- [ ] URL format: `/book-flight/mongodb-object-id`

**If failing:**
- Check browser console for JavaScript errors
- Verify flight data has `flightName`, `from`, `to`, `price` fields
- Verify hotel data has `hotelName`, `location`, `pricePerNight` fields
- Ensure data returned is an array, not a single object

### Test 3: Flight Detail Page
**What to do:**
1. From home page, click "Book Now" on a flight
2. URL should be `/book-flight/{id}`
3. Wait for page to load
4. Verify flight details appear

**Expected Results:**
- [ ] Page loads without errors
- [ ] Flight name, from, to are displayed
- [ ] Price information shows correctly
- [ ] "Book Flight" button is visible
- [ ] No "No flight data available" message

**If failing:**
- Check network tab for `/flight` request
- Verify response includes flight with matching `_id`
- Check browser console for filter errors
- Confirm `_id` field exists in response

### Test 4: Hotel Detail Page
**What to do:**
1. From home page, click "Book Now" on a hotel
2. URL should be `/book-hotel/{id}`
3. Wait for page to load
4. Verify hotel details appear

**Expected Results:**
- [ ] Page loads without errors
- [ ] Hotel name and location displayed
- [ ] Price per night shows correctly
- [ ] "Book Hotel" button is visible
- [ ] No "No hotel data available" message

**If failing:**
- Same troubleshooting as flight detail page
- Verify hotel data structure in MongoDB

### Test 5: Booking Flow (Flight)
**What to do:**
1. Go to flight detail page
2. Click "Book Flight" button
3. Verify booking dialog appears
4. Click "Confirm Booking" (if logged in)
5. Wait for response

**Expected Results:**
- [ ] Booking dialog shows flight details
- [ ] If logged in, booking completes
- [ ] Redirects to profile page
- [ ] Booking appears in user's bookings list

**If failing:**
- Check if user is logged in (Redux state)
- Verify `/booking/flight` endpoint exists
- Check Spring Boot logs for request details
- Verify request includes: `userId`, `flightId`, `seats`, `price`

### Test 6: Booking Flow (Hotel)
**What to do:**
1. Go to hotel detail page
2. Click "Book Hotel" button
3. Verify booking dialog appears
4. Click "Confirm Booking" (if logged in)
5. Wait for response

**Expected Results:**
- [ ] Booking dialog shows hotel details
- [ ] If logged in, booking completes
- [ ] Redirects to profile page
- [ ] Booking appears in user's bookings list

**If failing:**
- Verify `/booking/hotel` endpoint exists (not `/booking/flight`)
- Check Spring Boot logs
- Verify request includes: `userId`, `hotelId`, `rooms`, `price`

### Test 7: Admin Panel
**What to do:**
1. Navigate to http://localhost:3000/admin
2. Check Flights tab
3. Check Hotels tab
4. Try to add a flight
5. Try to add a hotel

**Expected Results:**
- [ ] Flight list populates from backend
- [ ] Hotel list populates from backend
- [ ] Add flight form works and submits to `/admin/flight`
- [ ] Add hotel form works and submits to `/admin/hotel`
- [ ] New items appear in respective lists

**If failing:**
- Check network requests in DevTools
- Verify `/admin/flight` endpoint accepts POST
- Verify `/admin/hotel` endpoint accepts POST
- Ensure request body format matches backend expectations

---

## 🔧 Troubleshooting Guide

### Problem: "Cannot GET /flight" or 404 errors

**Diagnosis:**
1. Open DevTools → Network tab
2. Look for request to `/flight`
3. Check response status (should be 200)

**Solutions:**
```
✓ Check .env.local has NEXT_PUBLIC_BACKEND_URL
✓ Verify Spring Boot running on that port
✓ Check endpoint exists on backend
✓ Verify CORS allows requests from localhost:3000
✓ Check Spring Boot logs for errors
```

**Test directly with curl:**
```bash
curl http://localhost:8080/flight
# Should return: [{...flight data...}]
```

### Problem: Flights show but "Book Now" redirects to blank page

**Diagnosis:**
1. Click "Book Now" on a flight
2. Check URL has ID: `/book-flight/123abc...`
3. DevTools → Network tab shows `/flight` request
4. Check response has that specific flight

**Solutions:**
```
✓ Verify flight has _id field in response
✓ Check if filtering works: flight._id === urlId
✓ Look at console for any errors
✓ Verify flight data structure has all fields
```

### Problem: Booking fails with error

**Diagnosis:**
1. Open DevTools → Network tab
2. Look for request to `/booking/flight` or `/booking/hotel`
3. Check response status and body

**Solutions:**
```
✓ Is user logged in? (check Redux state)
✓ Does backend endpoint exist?
✓ Check request parameters: userId, flightId, seats, price
✓ Verify Spring Boot logs for validation errors
✓ Check MongoDB for user document
```

### Problem: "No data" message on search results

**Diagnosis:**
1. Check if API returns data (Network tab)
2. Verify filter logic in code
3. Check DevTools Console for errors

**Solutions:**
```
✓ Verify MongoDB has sample data
✓ Check API response format
✓ Ensure field names match (from, to, location)
✓ Verify case sensitivity in filters
```

---

## 📊 Network Request Examples

### Successful Flight Request
```
URL: http://localhost:8080/flight
Method: GET
Status: 200 OK

Response:
[
  {
    "_id": "507f191e810c19729de860ea",
    "flightName": "Air India AI-101",
    "from": "Delhi",
    "to": "Mumbai",
    "departureTime": "2024-01-15T10:00:00Z",
    "arrivalTime": "2024-01-15T12:00:00Z",
    "price": 5000,
    "availableSeats": 150
  }
]
```

### Successful Hotel Booking Request
```
URL: http://localhost:8080/booking/hotel?userId=user123&hotelId=507f191e810c19729de860ea&rooms=1&price=8000
Method: POST
Status: 200 OK or 201 Created

Response:
{
  "_id": "507f191e810c19729de860eb",
  "userId": "user123",
  "hotelId": "507f191e810c19729de860ea",
  "rooms": 1,
  "price": 8000,
  "bookingDate": "2024-01-10T15:30:00Z"
}
```

---

## 🔍 DevTools Inspection Guide

### 1. Check Environment Variable
```javascript
// Type in browser console:
console.log(process.env.NEXT_PUBLIC_BACKEND_URL)

// Should output: http://localhost:8080 (or your URL)
```

### 2. Check Redux State
```javascript
// Install Redux DevTools extension
// Opens in DevTools as new tab "Redux"
// Shows all state and dispatches
```

### 3. Monitor Network Requests
```
DevTools → Network tab
Filter: XHR (shows only API requests)
Watch for: /flight, /hotel, /booking/*, /user/*
```

### 4. Check Console Errors
```
DevTools → Console tab
Clear the console (Cmd+K on Mac, Ctrl+K on Windows)
Perform action
Look for red error messages
```

---

## 📝 Validation Checklist

Before declaring success, verify all of these:

```
BACKEND CONNECTIVITY
□ GET /flight returns 200 with flight array
□ GET /hotel returns 200 with hotel array
□ Each flight has _id, flightName, from, to, price
□ Each hotel has _id, hotelName, location, price

FRONTEND DATA DISPLAY
□ Home page shows flights list
□ Home page shows hotels list
□ Flights have correct field values displayed
□ Hotels have correct field values displayed

USER INTERACTIONS
□ Can search flights by from/to
□ Can search hotels by location
□ "Book Now" works on all items
□ Book detail pages load with correct data

BOOKINGS
□ Can complete flight booking
□ Can complete hotel booking
□ Bookings appear in profile
□ POST /booking/flight uses correct endpoint
□ POST /booking/hotel uses correct endpoint (NOT /flight)

ADMIN FEATURES
□ Admin panel loads
□ Can see flight list
□ Can see hotel list
□ Can add flight
□ Can add hotel
□ Can edit flight
□ Can edit hotel

ERROR HANDLING
□ No silent failures
□ Console shows meaningful error messages
□ Network errors are handled gracefully
□ Validation errors are displayed to user

DOCUMENTATION
□ .env.local configured
□ QUICK_START.md reviewed
□ BACKEND_INTEGRATION.md reviewed
□ No outstanding issues
```

---

## 🎉 Success Criteria

Your integration is **COMPLETE** when:

1. ✅ All 4 critical fixes applied
2. ✅ `.env.local` configured with backend URL
3. ✅ Spring Boot backend running with CORS enabled
4. ✅ Home page fetches and displays flights/hotels from backend
5. ✅ Can search flights and hotels
6. ✅ Can navigate to detail pages
7. ✅ Can complete bookings (flight and hotel)
8. ✅ Bookings saved to MongoDB via backend
9. ✅ No errors in browser console
10. ✅ All tests pass

---

## 📞 Getting Help

If something doesn't work:

1. **Check the documentation:**
   - `QUICK_START.md` - 2-minute guide
   - `BACKEND_INTEGRATION.md` - Complete reference
   - `INTEGRATION_CHANGES.md` - Detailed changes
   - `VISUAL_SUMMARY.md` - Architecture diagrams

2. **Debug with DevTools:**
   - Network tab → Check API requests
   - Console tab → Look for errors
   - Redux tab → Check state

3. **Verify backend:**
   - Test endpoints with Postman
   - Check Spring Boot logs
   - Verify MongoDB has data

4. **Common issues:**
   - See "Troubleshooting Guide" section above

---

**You've got this! 🚀**

Start with the Quick Start guide, run through the tests, and your frontend will be fully integrated with your Spring Boot backend.
