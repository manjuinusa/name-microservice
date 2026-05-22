# Java Microservices Demo — First Name / Last Name

Two Spring Boot microservices:

| Service        | Port | Role                                                        |
|----------------|------|-------------------------------------------------------------|
| `web-service`  | 8080 | Serves the HTML form, proxies submissions to `name-service` |
| `name-service` | 8081 | REST API that processes `firstName` + `lastName`            |

```
Browser  ──►  web-service (8080)  ──►  name-service (8081)
              [static index.html]      [POST /api/names]
```

## Prerequisites
- Java 17+
- Maven 3.9+ (only if running without Docker)
- Docker + Docker Compose (recommended)

## Run locally with Docker (one command)
```bash
docker compose up --build
```
Open http://localhost:8080 — enter first/last name and submit.

## Run locally without Docker
Two terminals:
```bash
# Terminal 1
cd name-service && mvn spring-boot:run

# Terminal 2
cd web-service && mvn spring-boot:run
```

## REST API (name-service)
```bash
curl -X POST http://localhost:8081/api/names \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ada","lastName":"Lovelace"}'
```
Response:
```json
{
  "id": "…",
  "firstName": "Ada",
  "lastName": "Lovelace",
  "fullName": "Ada Lovelace",
  "greeting": "Hello, Ada Lovelace!",
  "createdAt": "2026-…"
}
```

## Free hosting — Render.com (recommended)

Render's free tier supports Docker-based Java services. The included
`render.yaml` is a Blueprint that provisions both microservices.

**👉 Step-by-step deploy guide (zero local toolchain needed): see [DEPLOY.md](DEPLOY.md)**

Short version:
1. Push this folder to a GitHub repo.
2. On https://dashboard.render.com/blueprints click **New Blueprint Instance** → pick the repo.
3. After both services are Live, copy `name-service`'s URL into `web-service`'s `NAME_SERVICE_URL` env var.

> Free Render services sleep after ~15 min idle and cold-start in ~30s.

### Other free options
- **Railway** (railway.app) — $5/month free credit, deploy each Dockerfile as a service.
- **Fly.io** (fly.io) — `fly launch` in each service folder; free allowance covers small apps.
- **Koyeb** (koyeb.com) — free tier, Docker deploys from GitHub.

## Project structure
```
.
├── name-service/                 # Backend microservice
│   ├── src/main/java/.../NameController.java
│   ├── src/main/resources/application.properties
│   ├── Dockerfile
│   └── pom.xml
├── web-service/                  # Frontend + proxy microservice
│   ├── src/main/java/.../ProxyController.java
│   ├── src/main/resources/static/index.html
│   ├── src/main/resources/application.properties
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml            # Local orchestration
├── render.yaml                   # Free-hosting Blueprint
└── README.md
```
