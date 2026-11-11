# Microservice Événements — Eya Ben Saber

- **Port :** `8081`
- **Base de données :** MySQL (`evenements_db`)

---

## 1. Entités

### Evenement
- `id` : identifiant unique (Long)
- `titre` : titre de l'événement (String)
- `description` : description de l'événement (String)
- `type` : CONFERENCE, HACKATHON, SOUTENANCE, SEMINAIRE, ATELIER
- `date` : date de l'événement (LocalDateTime)
- `salleId` : référence vers le service Planning (Long)
- `capacite` : nombre max de places (Integer)
- `statut` : BROUILLON, PUBLIE, TERMINE, ANNULE

### Inscription
- `id` : identifiant unique (Long)
- `evenement` : événement associé (`@ManyToOne`)
- `participantId` : identifiant Keycloak du participant (Long)
- `statut` : CONFIRMEE, ANNULEE
- `date` : date d'inscription (LocalDateTime)

---

## 2. Endpoints (API REST)

### Événements
| Méthode | URL | Description | Codes |
|---|---|---|---|
| **POST** | `/events` | Créer un événement | 201, 400 |
| **GET** | `/events?type=&statut=` | Lister et filtrer les événements | 200 |
| **GET** | `/events/{id}` | Détail d'un événement | 200, 404 |
| **PUT** | `/events/{id}` | Modifier un événement | 200, 409 |
| **PUT** | `/events/{id}/publish` | Publier un événement | 200, 409 |
| **PUT** | `/events/{id}/cancel` | Annuler un événement | 200, 409 |

### Inscriptions
| Méthode | URL | Description | Codes |
|---|---|---|---|
| **POST** | `/events/{evenementId}/registrations` | Inscrire un participant | 201, 409 |
| **GET** | `/events/{evenementId}/registrations` | Liste des inscrits par événement | 200, 404 |
| **GET** | `/registrations/{id}` | Détail d'une inscription | 200, 404 |
| **DELETE** | `/registrations/{id}` | Annuler une inscription | 204, 404 |

---

## 3. Documentation Swagger (OpenAPI)

Avec `server.servlet.context-path=/evenements` :
- **Swagger UI :** http://localhost:8081/evenements/swagger-ui/index.html
- **Redirection Swagger :** http://localhost:8081/evenements/swagger-ui.html
- **Spécification OpenAPI (JSON) :** http://localhost:8081/evenements/v3/api-docs
