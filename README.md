# Panini Album · Backend

API REST en Spring Boot 4 + Java 21 + PostgreSQL para la app **Figuritas del Mundial 2026**.

## Stack
- Spring Boot 4.0.6 (webmvc, data-jpa, security, validation)
- Java 21 · Maven
- PostgreSQL 16
- JWT (jjwt 0.12.6)
- Lombok

## Requisitos locales
- Java 21
- Maven (o usa `./mvnw`)
- PostgreSQL 16 con BD `panini_album`

## Configuración

1. Copia `.env.example` a `.env`:
   ```bash
   cp .env.example .env
   ```
2. Rellena `DB_URL`, `DB_USER`, `DB_PASSWORD` y `JWT_SECRET`.

Spring carga el `.env` automáticamente vía `spring.config.import=optional:file:./.env[.properties]`.

## Arrancar local

```bash
./mvnw spring-boot:run
# http://localhost:8081
```

## Docker

```bash
docker build -t panini-backend .
docker run --rm -p 8080:8080 --env-file .env panini-backend
```

## Deploy a Railway

1. **New Project → Deploy from GitHub repo** y selecciona este repo.
2. Railway detecta el `Dockerfile` automáticamente.
3. En **Variables** añade:
   - `DB_URL` (formato `jdbc:postgresql://HOST:PORT/DATABASE`)
   - `DB_USER`, `DB_PASSWORD`
   - `JWT_SECRET` (string aleatorio largo)
   - `CORS_ALLOWED_ORIGINS` (incluye el dominio del frontend)
4. Railway expone automáticamente `PORT`; el servidor lo escucha con `${PORT:8081}`.

## Endpoints principales

| Recurso | Path |
|---|---|
| Auth | `/api/auth/{register,login}` |
| Catálogo | `/api/catalog/{nations,stickers}` |
| Álbum | `/api/album/me`, `/api/album/items` |
| Stats | `/api/stats/{me,country/:code}` |
| Amigos | `/api/friends/*` |
| Intercambios | `/api/trades/*` |
