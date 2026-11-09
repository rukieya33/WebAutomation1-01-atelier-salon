# React appointment booking concepts

Five React versions of the vanilla appointment-booking concepts live in one Vite project. Each route has a distinct brand and theme while sharing accessible search, category/staff filters, a booking dialog, and optional Spring Boot API integration.

## Run locally

Requirements: Node.js 20.19+ or 22.12+ (Vite 8 requirement).

```powershell
npm install
npm run dev
```

Open one of these URLs:

- `http://localhost:5173/atelier` — Maison & Mane salon
- `http://localhost:5173/clinic` — Goodwell clinic
- `http://localhost:5173/nocturne` — Still / House spa
- `http://localhost:5173/metro` — Sidequest local services
- `http://localhost:5173/bloom` — Bloom Room beauty studio

The selector in the header switches between concepts. The app works with preview data when the API is offline. To connect the sibling Spring Boot backend, start it on port 8080; it serves `/api/services`, `/api/staff`, `/api/availability`, and `/api/bookings`. To use a different API base, add `?api=http://localhost:8080/api` to the page URL.

## Production build

```powershell
npm run build
npm run preview
```
