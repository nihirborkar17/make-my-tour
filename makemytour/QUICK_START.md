# Quick Start - Spring Boot Integration

## 🚀 Get Started in 2 Minutes

### Step 1: Configure Backend URL
```bash
# Open .env.local and update it
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
```

Replace `http://localhost:8080` with your actual Spring Boot backend URL.

### Step 2: Start the Frontend
```bash
npm install  # If needed
npm run dev
```

Open http://localhost:3000 in your browser.

### Step 3: Verify Connection
1. Go to the home page
2. Open **DevTools** (F12) → **Network** tab
3. Try searching for flights
4. Look for requests to `/flight` endpoint
5. Verify you see your MongoDB data

## ✅ What Was Fixed

| Issue | Fix | Impact |
|-------|-----|--------|
| Hardcoded backend URL | Now uses environment variables | ✅ Easy configuration |
| Wrong ID field mapping | Changed `id` → `_id` | ✅ Data displays correctly |
| Hotel booking wrong endpoint | Fixed `/booking/flight` → `/booking/hotel` | ✅ Hotel bookings work |
| Silent error failures | Added error throwing | ✅ Better debugging |

## 📋 API Endpoints Your Backend Needs

```
GET    /flight                    (list all flights)
POST   /admin/flight              (add flight)
PUT    /admin/flight/{id}         (edit flight)

GET    /hotel                     (list all hotels)
POST   /admin/hotel               (add hotel)
PUT    /admin/hotel/{id}          (edit hotel)

POST   /booking/flight            (book flight)
POST   /booking/hotel             (book hotel)

POST   /user/login                (user login)
POST   /user/signup               (user signup)
GET    /user/email?email={email}  (get user)
POST   /user/edit?id={id}         (edit user)
```

## 🔍 Test the Connection

**Option 1: Browser DevTools**
1. Open http://localhost:3000
2. Press F12 → Network tab
3. Click "Book Now" on any item
4. Look for API requests in the Network tab
5. Verify responses contain your MongoDB data

**Option 2: Postman (Direct Backend Test)**
```
GET http://localhost:8080/flight

Expected Response:
[
  {
    "_id": "...",
    "flightName": "...",
    "from": "...",
    "to": "...",
    "price": 5000,
    ...
  }
]
```

## ⚠️ If It's Not Working

### No Data Showing
```
✓ Backend is running?
✓ .env.local has correct BACKEND_URL?
✓ MongoDB has data?
✓ DevTools shows 200 status on API calls?
```

### API Errors
```
✓ Check Console tab for error messages
✓ Check Spring Boot logs
✓ Verify CORS is configured on backend
✓ Check endpoint paths match exactly
```

### Booking Not Working
```
✓ User is logged in (check Redux store)?
✓ POST /booking/hotel endpoint exists?
✓ Backend returns booking with _id?
✓ Response data structure matches?
```

## 📁 Modified Files

```
✅ src/api/index.js
✅ src/pages/book-flight/[id]/index.tsx
✅ src/pages/book-hotel/[id]/index.tsx
✅ .env.local (new)
✅ BACKEND_INTEGRATION.md (new)
✅ INTEGRATION_CHANGES.md (new)
```

## 🔗 Full Documentation

See `BACKEND_INTEGRATION.md` for:
- Detailed API endpoint documentation
- Expected data structures
- CORS configuration
- Troubleshooting guide
- Testing instructions

## 💡 Pro Tips

1. **Use environment variables for different environments:**
   ```
   .env.local          (development - localhost)
   .env.production     (production - your server)
   ```

2. **Add request/response logging:**
   Open DevTools → Application → Local Storage
   Redux state shows user and bookings

3. **Monitor API calls:**
   DevTools → Network tab → filter by "XHR"
   Shows all API requests in real-time

4. **Check Redux state:**
   Install Redux DevTools Chrome extension
   See all state changes in real-time

---

**Need help?** Check the detailed guides:
- `BACKEND_INTEGRATION.md` - Complete integration guide
- `INTEGRATION_CHANGES.md` - Detailed change list
