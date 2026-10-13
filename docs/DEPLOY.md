<!-- Copyright (c) 2026 Anshul Negi | https://github.com/NegiCoder -->

# Hotel Booking API — Deploy Guide

**Live:** https://hotel-booking-api.wittywave-65888691.centralindia.azurecontainerapps.io/api/v1/swagger-ui/index.html

**Documentation (Notion):** https://app.notion.com/p/382385babde6810d98e3c84dc27990f8

**Stack:** Spring Boot on **Azure Container Apps** + **H2 in-memory** (demo profile — **~$0 idle**, no MySQL bill)

> Cold start ~30–60s. Demo data re-seeds automatically on each wake.

---

## Live URLs

| What | URL |
|------|-----|
| Swagger UI | https://hotel-booking-api.wittywave-65888691.centralindia.azurecontainerapps.io/api/v1/swagger-ui/index.html |
| Search hotels | `GET /api/v1/hotels/search?city=Delhi&startDate=2026-06-20&endDate=2026-06-22&roomCount=1` |
| Login | `POST /api/v1/auth/login` |

---

## Azure resources

| Resource | Name | Resource group |
|----------|------|----------------|
| Container App | `hotel-booking-api` | `rg-hotel-booking-dev-demo` |
| Container Apps env | `training-institute-env` (shared) | `rg-training-institute-dev` |
| Database | H2 in-memory (`demo` profile) | inside Container App |
| ACR image | `anshultrainingacr.azurecr.io/hotel-booking-api:latest` | `rg-training-institute-dev` |

---

## Redeploy (Azure CLI)

```bash
# 1. Build & push (Docker must be running)
az acr login --name anshultrainingacr
cd /path/to/hotel-booking-backend
docker buildx build --platform linux/amd64 \
  -t anshultrainingacr.azurecr.io/hotel-booking-api:latest --push .

# 2. Update Container App
az containerapp update \
  -g rg-hotel-booking-dev-demo \
  -n hotel-booking-api \
  --image anshultrainingacr.azurecr.io/hotel-booking-api:latest
```

---

## Render deploy (optional alternative)

See env vars below if you prefer Render instead of Azure Container Apps.

## Demo accounts (auto-seeded)

| Role | Email | Password |
|------|-------|----------|
| Hotel manager | `manager@demo.com` | `Demo@123` |
| Guest | `guest@demo.com` | `Demo@123` |

**Seeded cities:** Delhi, Goa, Bangalore (3 hotels, 6 room types, 1 year inventory)

---

## Azure MySQL (already provisioned)

| Setting | Value |
|---------|-------|
| Resource group | `rg-hotel-booking-dev` |
| Server | `hotelbookingmysql` |
| Host | `hotelbookingmysql.mysql.database.azure.com` |
| Database | `airbnb` |
| Admin user | `hoteladmin` |
| Password | Set during provisioning — check Azure Portal → MySQL server → Reset password if needed |

---

## Render deploy (one-time)

1. Push this repo to GitHub (`NegiCoder/Hotel-Property-Booking-System-Backend`)
2. Go to [render.com](https://render.com) → **New +** → **Blueprint** (or **Web Service**)
3. Connect the private repo and use `render.yaml`, **or** configure manually:

| Setting | Value |
|---------|-------|
| Runtime | Docker |
| Region | Singapore |
| Branch | `main` |
| Plan | Free |

4. **Environment variables** (Render dashboard → Environment):

```
SPRING_PROFILES_ACTIVE=prod
APP_DEMO_SEED=true
SPRING_DATASOURCE_URL=jdbc:mysql://hotelbookingmysql.mysql.database.azure.com:3306/airbnb?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=hoteladmin
DB_PASSWORD=<your-azure-mysql-password>
JWT_SECRET_KEY=<random-64-char-string>
STRIPE_SECRET_KEY=sk_test_demo
STRIPE_WEBHOOK_SECRET=whsec_demo
```

5. Deploy. First boot takes ~3–5 min (Docker build). Free tier cold start ~40–60s.

---

## Recruiter walkthrough (Swagger)

1. Open **Swagger UI** link from README
2. `GET /hotels/search` → city `Delhi`, dates next weekend, `roomCount=1` → **Execute**
3. `POST /auth/login` → `guest@demo.com` / `Demo@123` → copy `accessToken`
4. Click **Authorize** (top right) → `Bearer <token>`
5. `POST /bookings/init` to create a reservation
6. For admin: login as `manager@demo.com` → try `/admin/hotels` endpoints

---

## Local dev

```bash
cp .env.example .env   # fill in values
# MySQL locally or point SPRING_DATASOURCE_URL to Azure
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Swagger: http://localhost:8080/api/v1/swagger-ui/index.html

---

## Cost note

Azure MySQL `Standard_B1ms` is a **paid** SKU (~$12–15/mo). For Students subscription, monitor usage. Tear down when not demoing:

```bash
az group delete --name rg-hotel-booking-dev --yes --no-wait
```
