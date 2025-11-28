import axios from "axios";

const BACKEND_URL =
  process.env.NEXT_PUBLIC_BACKEND_URL?.replace(/\/$/, "") ||
  "http://localhost:8080";

const api = axios.create({
  baseURL: BACKEND_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

const withStableId = (entity) =>
  entity && typeof entity === "object"
    ? { ...entity, id: entity.id ?? entity._id ?? entity.Id }
    : entity;

const normalizeList = (payload) =>
  Array.isArray(payload) ? payload.map((entry) => withStableId(entry)) : [];

const handleRequest = async (promise, { label, normalize } = {}) => {
  try {
    const res = await promise;
    const data = res.data;
    if (normalize === "list") {
      return normalizeList(data);
    }
    if (normalize === "doc") {
      return withStableId(data);
    }
    return data;
  } catch (error) {
    const reason =
      error?.response?.data?.message ||
      error?.response?.data ||
      error?.message ||
      "Unknown error";
    console.error(`[api] ${label ?? "request"} failed:`, reason);
    throw error;
  }
};

export const login = async (email, password) =>
  handleRequest(
    api.post(
      "/user/login",
      {},
      {
        params: { email, password },
      }
    ),
    { label: "login", normalize: "doc" }
  );

export const signup = async (
  firstName,
  lastName,
  email,
  phoneNumber,
  password
) =>
  handleRequest(
    api.post("/user/signup", {
      firstName,
      lastName,
      email,
      phoneNumber,
      password,
    }),
    { label: "signup", normalize: "doc" }
  );

export const getuserbyemail = async (email) =>
  handleRequest(
    api.get("/user/email", {
      params: { email },
    }),
    { label: "get user by email", normalize: "doc" }
  );

export const editprofile = async (
  id,
  firstName,
  lastName,
  email,
  phoneNumber
) =>
  handleRequest(
    api.post(
      "/user/edit",
      { firstName, lastName, email, phoneNumber },
      { params: { id } }
    ),
    { label: "edit profile", normalize: "doc" }
  );

export const getflight = async () =>
  handleRequest(api.get("/flight"), {
    label: "fetch flights",
    normalize: "list",
  });

export const addflight = async (
  flightName,
  from,
  to,
  departureTime,
  arrivalTime,
  price,
  availableSeats
) =>
  handleRequest(
    api.post("/admin/flight", {
      flightName,
      from,
      to,
      departureTime,
      arrivalTime,
      price,
      availableSeats,
    }),
    { label: "add flight", normalize: "doc" }
  );

export const editflight = async (
  id,
  flightName,
  from,
  to,
  departureTime,
  arrivalTime,
  price,
  availableSeats
) =>
  handleRequest(
    api.put(`/admin/flight/${id}`, {
      flightName,
      from,
      to,
      departureTime,
      arrivalTime,
      price,
      availableSeats,
    }),
    { label: "edit flight", normalize: "doc" }
  );

export const gethotel = async () =>
  handleRequest(api.get("/hotel"), {
    label: "fetch hotels",
    normalize: "list",
  });

export const addhotel = async (
  hotelName,
  location,
  pricePerNight,
  availableRooms,
  amenities
) =>
  handleRequest(
    api.post("/admin/hotel", {
      hotelName,
      location,
      pricePerNight,
      availableRooms,
      amenities,
    }),
    { label: "add hotel", normalize: "doc" }
  );

export const edithotel = async (
  id,
  hotelName,
  location,
  pricePerNight,
  availableRooms,
  amenities
) =>
  handleRequest(
    api.put(`/admin/hotel/${id}`, {
      hotelName,
      location,
      pricePerNight,
      availableRooms,
      amenities,
    }),
    { label: "edit hotel", normalize: "doc" }
  );

export const handleflightbooking = async (userId, flightId, seats, price) =>
  handleRequest(
    api.post(
      "/booking/flight",
      {},
      {
        params: { userId, flightId, seats, price },
      }
    ),
    { label: "book flight", normalize: "doc" }
  );

export const handlehotelbooking = async (userId, hotelId, rooms, price) =>
  handleRequest(
    api.post(
      "/booking/hotel",
      {},
      {
        params: { userId, hotelId, rooms, price },
      }
    ),
    { label: "book hotel", normalize: "doc" }
  );

// --- Single-item fetchers
export const getFlightById = async (id) =>
  handleRequest(api.get(`/flight/${id}`), {
    label: "fetch flight by id",
    normalize: "doc",
  });

export const getHotelById = async (id) =>
  handleRequest(api.get(`/hotel/${id}`), {
    label: "fetch hotel by id",
    normalize: "doc",
  });

// --- Availability endpoints
// GET /flight/availability?from=...&to=...&date=YYYY-MM-DD&seats=...
export const getFlightAvailability = async ({ from, to, date, seats }) =>
  handleRequest(
    api.get("/flight/availability", {
      params: { from, to, date, seats },
    }),
    { label: "flight availability", normalize: "list" }
  );

// GET /hotel/availability?location=...&checkin=YYYY-MM-DD&checkout=YYYY-MM-DD&rooms=...
export const getHotelAvailability = async ({ location, checkin, checkout, rooms }) =>
  handleRequest(
    api.get("/hotel/availability", {
      params: { location, checkin, checkout, rooms },
    }),
    { label: "hotel availability", normalize: "list" }
  );
