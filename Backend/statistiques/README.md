# Microservice Statistiques 📊

> **Projet :** Gestion des événements universitaires  
> **Auteur :** Eya Baccouche  
> **Port :** 8085  
> **Context-Path :** `/statistiques`  
> **Base de données :** PostgreSQL (`statistiques_db`, port 5433)

---

## 1. Description
Ce microservice agrège inscriptions, présences et avis pour produire des indicateurs statistiques et alimenter le tableau de bord des organisateurs.

---

## 2. Configuration (`application.properties`)
```properties
server.port=8085
server.servlet.context-path=/statistiques

# PostgreSQL (Port 5433)
spring.datasource.url=jdbc:postgresql://localhost:5433/statistiques_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# Hibernate & chargement automatique de data.sql
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```

---

## 3. Endpoints de l'API REST (100% Conforme au Sujet AWD)
Base URL : `http://localhost:8085/statistiques/stats`

| Méthode | Route | Description | Code Succès |
|---|---|---|---|
| `GET` | `/statistiques/stats/events/{id}` | Consulter les statistiques d'un événement | `200 OK` (ou `404`) |
| `GET` | `/statistiques/stats/events?from=&to=` | Lister les statistiques de tous les événements | `200 OK` |
| `GET` | `/statistiques/stats/overview` | Consulter la vue d'ensemble des indicateurs globaux | `200 OK` |
| `GET` | `/statistiques/stats/events/{id}/timeline` | Consulter l'historique temporel (affluence) d'un événement | `200 OK` |

Documentation Swagger UI : **`http://localhost:8085/statistiques/swagger-ui/index.html`**
