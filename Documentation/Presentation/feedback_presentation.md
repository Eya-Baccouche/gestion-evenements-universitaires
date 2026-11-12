# Microservice Feedback & Intervenants
**Youssef Saibi — Équipe Warriors — AWD 2026-2027**

---

## 1. Présentation du microservice

Le microservice **Feedback** gère :
- Les **avis** déposés par les participants sur les événements
- Les **intervenants** externes (conférenciers, jurys)
- Les **affectations** des intervenants aux événements

---

## 2. Stack technique

| Composant | Technologie |
|-----------|-------------|
| Framework | Spring Boot 4.1.2 |
| API | REST (Spring Web MVC) |
| Base de données | PostgreSQL 17 |
| ORM | Spring Data JPA / Hibernate |
| Communication | OpenFeign (vers Événements) |
| Documentation API | Swagger UI (springdoc-openapi) |
| Sécurité | Spring Security (Keycloak prévu) |
| Service discovery | Eureka Client |
| Config | Spring Cloud Config Client |

---

## 3. Modèle de données

```
Avis
├── id
├── evenementId
├── participantId
├── commentaire
├── note (1-5)
└── date

Intervenant
├── id
├── nom
├── fonction
└── disponibilites

Affectation
├── id
├── intervenant → Intervenant
├── evenementId
└── role (CONFERENCIER / JURY)
```

---

## 4. API REST

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/events/{id}/feedback` | Déposer un avis |
| GET | `/events/{id}/feedback` | Lister les avis |
| POST | `/speakers` | Créer un intervenant |
| POST | `/assignments` | Affecter à un événement |
| GET | `/speakers/{id}/assignments` | Voir les affectations |

**Swagger :** http://localhost:8084/swagger-ui/index.html

---

## 5. Communication inter-services

- **OpenFeign** → appelle le microservice Événements (port 8081)
- Vérifie l'existence de l'événement avant toute affectation
- **Fallback 503** automatique si Événements est hors-ligne

---

## 6. Démo en direct

1. Ouvrir Swagger : http://localhost:8084/swagger-ui/index.html
2. POST `/speakers` → créer un intervenant
3. POST `/assignments` → affecter à l'événement 1
4. POST `/events/1/feedback` → déposer un avis
5. GET `/events/1/feedback` → voir les avis
6. Ouvrir **pgAdmin 4** → feedbackdb → table `avis` → toutes les données visibles

---

## 7. Diagrammes UML

- Diagramme de cas d'utilisation : `Documentation/Diagrams/usecase_feedback.png`
- Diagramme de séquence : `Documentation/Diagrams/sequence_feedback.png`
