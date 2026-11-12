# API Gateway

Point d'entrée unique de la plateforme **Gestion des événements universitaires** (équipe Warriors).

## Rôle
- Recevoir toutes les requêtes du Front-end React (`http://localhost:5173`).
- Valider les jetons JWT auprès de **Keycloak** (`http://localhost:8180`).
- Router dynamiquement chaque requête vers le microservice concerné via **Eureka Server** (`lb://...`).

## Port
- **8080**

## Table de routage prévue

| Préfixe URI | Microservice cible | Route Eureka |
|---|---|---|
| `/events/**` | Événements | `lb://evenements` |
| `/rooms/**`, `/reservations/**` | Planning des salles | `lb://planning` |
| `/badges/**`, `/checkins/**` | Check-in / QR | `lb://checkin` |
| `/feedback/**`, `/speakers/**` | Feedback et intervenants | `lb://feedback` |
| `/stats/**` | Statistiques | `lb://statistiques` |
| `/notifications/**` | Notifications | `lb://notifications` |
