# Gestion des événements universitaires

Projet AWD 2026-2027 — Équipe Warriors (DASI 6)

## Membres et microservices

| Membre | Microservice | Port | Base de données |
|--------|-------------|------|-----------------|
| Eya Ben Saber | api-gateway + evenements | 8080 / 8081 | MySQL |
| Eya Baccouche | statistiques | 8085 | PostgreSQL |
| Rayen Amri | checkin + eureka-server | 8083 / 8761 | MySQL |
| Youssef Saibi | feedback + config-server | 8084 / 8888 | PostgreSQL |
| Hana Abidi | notifications | 8086 | MongoDB |
| Amal Barhoumi | planning | 8082 | PostgreSQL |

## Architecture

```
Frontend (Angular)
      │
      ▼
API Gateway (8080)
      │
      ├── Eureka Server (8761) — service discovery
      ├── Config Server (8888) — configuration centralisée
      │
      ├── Événements      (8081) — MySQL
      ├── Feedback        (8084) — PostgreSQL
      ├── Planning        (8082) — PostgreSQL
      ├── Check-in        (8083) — MySQL
      ├── Notifications   (8086) — MongoDB
      └── Statistiques    (8085) — PostgreSQL
```

Stack : Spring Boot 4.x · Spring Cloud · OpenFeign · Eureka · Config Server · Keycloak · Swagger/OpenAPI

## Démarrage

1. Lancer **Eureka Server** (port 8761)
2. Lancer **Config Server** (port 8888)
3. Lancer les microservices dans n'importe quel ordre
4. Accéder au **Frontend** Angular

## Microservice Feedback (Youssef Saibi)

**Port :** 8084  
**Base :** PostgreSQL — `feedbackdb`  
**Branche :** `feature/feedback`

### Endpoints

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | `/events/{id}/feedback` | Déposer un avis |
| GET | `/events/{id}/feedback` | Lister les avis d'un événement |
| POST | `/speakers` | Créer un intervenant |
| POST | `/assignments` | Affecter un intervenant à un événement |
| GET | `/speakers/{id}/assignments` | Voir les affectations d'un intervenant |

**Swagger UI :** http://localhost:8084/swagger-ui/index.html

### Entités

- `Avis` — avis d'un participant sur un événement (note, commentaire)
- `Intervenant` — intervenant externe (nom, fonction, disponibilités)
- `Affectation` — lien intervenant ↔ événement avec rôle (CONFERENCIER / JURY)

### Communication inter-services

- Feign client vers **Événements** (port 8081) pour vérifier l'existence d'un événement avant affectation
- Fallback automatique si Événements est hors-ligne (réponse 503)

## Documentation

- `Documentation/Diagrams/usecase_feedback.png` — diagramme de cas d'utilisation
- `Documentation/Diagrams/sequence_feedback.png` — diagramme de séquence (3 scénarios)
