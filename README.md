<!-- Copyright (c) 2026 Anshul Negi | https://github.com/NegiCoder -->

# Hotel & Property Booking System — Backend API

[![Live API](https://img.shields.io/badge/Live%20API-Swagger%20UI-2ea44f?style=for-the-badge)](https://hotel-booking-api.wittywave-65888691.centralindia.azurecontainerapps.io/api/v1/swagger-ui/index.html)
[![Documentation](https://img.shields.io/badge/Docs-Notion-black?style=for-the-badge&logo=notion)](https://app.notion.com/p/382385babde6810d98e3c84dc27990f8)
[![GitHub stars](https://img.shields.io/github/stars/NegiCoder/Hotel-Property-Booking-System-Backend?style=social)](https://github.com/NegiCoder/Hotel-Property-Booking-System-Backend/stargazers)
[![Last commit](https://img.shields.io/github/last-commit/NegiCoder/Hotel-Property-Booking-System-Backend)](https://github.com/NegiCoder/Hotel-Property-Booking-System-Backend/commits/main)
[![Java](https://img.shields.io/badge/Java-23-orange?style=flat-square)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen?style=flat-square)](https://spring.io/projects/spring-boot)
[![API Docs](https://img.shields.io/badge/API-Swagger%20UI-85EA2D?style=flat-square&logo=swagger&logoColor=black)](https://swagger.io/)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Anshul_Negi-0A66C2?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/anshul-negi-55aa14345/)

**Production-style hotel booking backend** — property discovery, room inventory, reservations, dynamic pricing, JWT role-based access, and Stripe payment webhooks. Built for recruiters to explore entirely through **Swagger UI** (no frontend required).

> **Keywords:** Spring Boot hotel API · booking system backend · JWT REST API · Swagger demo · Azure Container Apps · dynamic pricing Java

---

## What it does

| Capability | Description |
|------------|-------------|
| **Auth** | JWT login for Guest and Hotel Manager roles |
| **Hotels & rooms** | CRUD, activation, inventory by date |
| **Search** | City + date range with min-price aggregation |
| **Bookings** | Init, guest details, payment hooks, cancel |
| **Pricing** | Surge, holiday, urgency, and occupancy strategies |
| **Docs** | OpenAPI / Swagger — interactive API exploration |

Seeded demo data: **3 hotels** (Delhi, Goa, Bangalore), **6 room types**, **60 days** of inventory per room.

---

## Try it live

| | Link |
|---|------|
| **Swagger UI** | https://hotel-booking-api.wittywave-65888691.centralindia.azurecontainerapps.io/api/v1/swagger-ui/index.html |
| **Full docs (Notion)** | https://app.notion.com/p/382385babde6810d98e3c84dc27990f8 |

Demo logins are shown at the top of Swagger and pre-filled in **Auth → login**.

| Role | Email | Password |
|------|-------|----------|
| **Guest** | `guest@demo.com` | `Demo@123` |
| **Hotel Manager** | `manager@demo.com` | `Demo@123` |

### Quick Swagger walkthrough

1. **2. Hotels → search** — Delhi + dates → **Try it out** → **Execute**
2. **1. Auth → login** — guest credentials → copy `accessToken`
3. **Authorize** (top right) → paste token
4. **3. Bookings → init** — sample body
5. Admin: login as `manager@demo.com`, try **4. Admin — Hotels → getAllHotels**

> Cold start ~30–60s on Azure. H2 demo DB re-seeds on each wake.

---

## Install & run locally

**Requirements:** JDK 23+, Maven 3.9+

```bash
git clone https://github.com/NegiCoder/Hotel-Property-Booking-System-Backend.git
cd Hotel-Property-Booking-System-Backend
cp .env.example .env    # set DB + JWT secrets
mvn spring-boot:run
```

Swagger: http://localhost:8080/api/v1/swagger-ui/index.html

For production-style deploy with persistent MySQL, use the `prod` profile — see `application-prod.properties` and **[docs/DEPLOY.md](docs/DEPLOY.md)**.

---

## Tech stack

Java 23 · Spring Boot 4 · Spring Security · JPA · H2 (demo) / MySQL (prod) · Maven · Stripe webhooks · springdoc-openapi

---

## Architecture

| Layer | Hosting |
|-------|---------|
| API | Azure Container Apps (`rg-hotel-booking-dev-demo`) |
| Database | H2 in-memory (demo profile) |
| Registry | `anshultrainingacr.azurecr.io/hotel-booking-api` |

---

## Contributing

1. Fork the repository and create a feature branch.
2. Keep changes focused — one feature or fix per PR.
3. Run `mvn test` before opening a pull request.
4. Update Swagger-related docs if you change API contracts.
5. Open an issue first for large architectural changes.

Bug reports and documentation improvements are especially welcome.

---

## Author

Built by **[Anshul Negi](https://github.com/NegiCoder)** · [LinkedIn](https://www.linkedin.com/in/anshul-negi-55aa14345/) — Java backend portfolio project.

## License

Educational / portfolio use. See repository for details.

---

## Support

If this project helped you learn Spring Boot APIs, JWT auth, or booking-domain modeling, please consider giving it a **⭐** on GitHub.

[![Star on GitHub](https://img.shields.io/github/stars/NegiCoder/Hotel-Property-Booking-System-Backend?style=social&label=Star%20this%20repo)](https://github.com/NegiCoder/Hotel-Property-Booking-System-Backend/stargazers)
