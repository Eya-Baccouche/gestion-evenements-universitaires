# Gestion des Événements Universitaires

> **Module :** Architecture Web Distribuée (AWD) — ESPRIT 4DAS6 (2026-2027)
> **Équipe :** Warriors (Eya Ben Saber · Eya Baccouche · Rayen Amri · Youssef Saibi · Hana Abidi · Amal Barhoumi)

---

## 1. Présentation du Projet

Plateforme web distribuée permettant de gérer l'intégralité du cycle de vie des événements universitaires (conférences, hackathons, salons partenaires et compétitions) : réservation des espaces, démarchage de sponsors, formation d'équipes (Smart Matching), logistique matérielle et programme de fidélité.

---

## 2. Découpage en 7 Microservices

L'architecture repose sur le pattern *Database-per-Service* avec une persistance polyglotte (MySQL, PostgreSQL, MongoDB) :

| Microservice                        | Responsable                | Stack Backend              |   Port   |   Base de Données   | Rôle Clé                                                                 |
| ----------------------------------- | -------------------------- | -------------------------- | :------: | :------------------: | -------------------------------------------------------------------------- |
| **Événements** *(Pivot)*  | Eya Ben Saber              | Spring Boot                | `8081` |   **MySQL**   | Cycle de vie des événements et contrôle des jauges d'inscription.       |
| **Compétitions & Équipes**  | Eya Baccouche              | Spring Boot                | `8082` | **PostgreSQL** | Hackathons, équipes, Smart Matching solo et notation des projets.         |
| **Sponsors & Partenariats**   | Rayen Amri                 | Spring Boot                | `8083` |   **MySQL**   | Packs partenaires, conventions et démarchage bilatéral.                  |
| **Feedback & Intervenants**   | Youssef Saibi              | Spring Boot                | `8084` | **PostgreSQL** | Disponibilités des speakers/jurys, affectations et recueil des avis.      |
| **Fidélité & Récompenses** | Hana Abidi                 | Spring Boot                | `8085` |  **MongoDB**  | Gamification, historique des points d'engagement NoSQL et badges.          |
| **Planning des salles**       | Amal Barhoumi              | Spring Boot                | `8086` | **PostgreSQL** | Réservations d'espaces et détection des conflits horaires (`tsrange`). |
| **Logistique & Équipements** | **Équipe (Commun)** | **Python (FastAPI)** | `8087` | **PostgreSQL** | Gestion du stock matériel partagé (kits IoT, micros) et des stands.      |

---

## 3. Socle Commun d'Infrastructure

* **API Gateway (`8080`) :** Spring Cloud Gateway (point d'entrée unique, routage dynamique et CORS).
* **Service Registry (`8761`) :** Netflix Eureka Server (annuaire dynamique des 7 microservices).
* **Config Server (`8888`) :** Spring Cloud Config Server (centralisation des configurations applicatives).
* **Sécurité & IAM (`8180`) :** Keycloak (authentification OAuth2 / OpenID Connect, tokens JWT et rôles RBAC).
* **Message Broker (`5672`) :** RabbitMQ (Exchange Topic `evenements.topic` pour les flux asynchrones).
* **Front-end (`5173`) :** Application web unique développée avec React (Vite).
* **Conteneurisation :** Docker & Docker Compose pour orchestrer l'ensemble des conteneurs.

---

## 4. Diagrammes du Projet

Tous les diagrammes de conception sont directement accessibles ci-dessous :

* [Architecture Technique](Documentation/Diagrams/architecture-technique.png) — *Vue globale des composants, de la Gateway, d'Eureka, de Keycloak et de RabbitMQ*
* [Diagramme de Classes Global](Documentation/Diagrams/class-diagram.png) — *Modèle conceptuel métier des entités et relations*
* [Diagramme de Cas d&#39;Utilisation](Documentation/Diagrams/Use_Case.png) — *Les 5 acteurs et leurs interactions avec la plateforme*
* [Communications Synchrones](Documentation/Diagrams/communications-synchrones.png) — *Détail des 5 appels REST inter-services (OpenFeign)*
* ⚡ [Communications Asynchrones](Documentation/Diagrams/communications-asynchrones.png) — *Détail des 6 événements publiés et consommés (RabbitMQ)*
