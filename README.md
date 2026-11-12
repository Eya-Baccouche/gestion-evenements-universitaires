# 🎓 Gestion des Événements Universitaires

Plateforme web distribuée en architecture microservices pour gérer l'intégralité du cycle de vie des événements universitaires : publication, réservation de salles, inscriptions, contrôle d'accès par QR code, recueil d'avis et statistiques d'affluence.

**Cadre :** Projet Architecture Web Distribuée (AWD) 2026-2027 · 4DAS6  
**Équipe :** Warriors  
**Dossier technique (PDF) :** [`Documentation/Gestion_evenements.pdf`](Documentation/Gestion_evenements.pdf)  
**Schémas & diagrammes :** [`Documentation/Diagrams/`](Documentation/Diagrams)

---

## 1. Problématique & Périmètre

* **Problème identifié :** Dans le contexte universitaire, la gestion des événements souffre d'un éparpillement des données (fichiers dispersés), d'un risque élevé de double réservation de salles gérées manuellement, et d'une absence de contrôle fiable à l'entrée (aucun bilan réel de présence ni évaluation post-événement).
* **Solution apportée :** Une suite coordonnée de 6 microservices découplés autour d'un socle d'infrastructure Spring Cloud unifié.
* **Hors périmètre :** Paiement en ligne, streaming vidéo et gestion interne des comptes utilisateurs (déléguée au fournisseur d'identité IAM).

---

## 2. Acteurs & Cas d'Utilisation

* **Participant :** Consulte les événements publiés, s'inscrit, reçoit son badge QR unique et dépose son avis après participation.
* **Organisateur :** Crée, publie et annule des événements, réserve une salle, affecte des intervenants, scanne les badges à l'entrée et analyse les statistiques.
* **Intervenant :** Déclare ses disponibilités, consulte ses affectations (conférencier / jury) et reçoit ses convocations.
* **Administrateur :** Gère le référentiel des salles, supervise l'ensemble des événements et administre les gabarits de messages.

---

## 3. Répartition des Microservices & Choix des Bases de Données

Chaque membre de l'équipe a la responsabilité d'un microservice métier avec sa propre base de données (*Database-per-Service*) :

| Membre | Microservice | Port | Base de données | Justification du choix de la BDD |
|---|---|:---:|:---:|---|
| **Eya Ben Saber** | **Événements** | `8081` | MySQL | Modèle relationnel ; intégrité transactionnelle requise pour le contrôle de capacité lors des inscriptions. |
| **Eya Baccouche** | **Statistiques** | `8082` | PostgreSQL | Puissance des requêtes d'agrégation analytique SQL, fenêtrage et support performant des séries temporelles. |
| **Rayen Amri** | **Check-in / QR** | `8083` | MySQL | Contraintes d'unicité strictes en base pour garantir l'unicité absolue de chaque code QR et badge généré. |
| **Youssef Saibi** | **Feedback & Intervenants** | `8084` | PostgreSQL | Base relationnelle robuste pour la persistance des avis, profils intervenants et affectations de jurys. |
| **Hana Abidi** | **Notifications** | `8085` | MongoDB | Structure de documents polymorphe et variable (emails, SMS, templates paramétrables) avec écritures fréquentes sans schéma rigide. |
| **Amal Barhoumi** | **Planning des salles** | `8086` | PostgreSQL | Contraintes d'exclusion natives sur les plages horaires (`tsrange`), garantissant au niveau base l'impossibilité de conflits de réservation. |

---

## 4. Socle Commun d'Infrastructure (Travail d'équipe)

Développé collectivement par toute l'équipe pour assurer la cohésion, la sécurité et la résilience du système :

* **Spring Cloud Gateway (`8080`) :** Point d'entrée unique de la plateforme, gestion des CORS, vérification des jetons JWT et routage dynamique vers les services via Eureka (`lb://...`).
* **Netflix Eureka Server (`8761`) :** Annuaire de services assurant l'enregistrement et la découverte dynamique des instances.
* **Spring Cloud Config Server (`8888`) :** Centralisation et versionnement des profils de configuration applicative.
* **Sécurité & IAM (`8180`) :** Keycloak / Better Auth pour la gestion centralisée des comptes, des rôles et l'émission des tokens OAuth2/JWT.
* **Broker RabbitMQ (`5672`) :** Bus de messages asynchrone garantissant la tolérance aux pannes et le découplage événementiel (exchange topic `evenements`).
* **Front-end Client (`5173`) :** Application web unique développée avec React (Vite).
* **Docker & Docker Compose :** Orchestration globale et reproductible des conteneurs (bases de données, broker et services).

### Contrat de Routage API Gateway

| Préfixe URI | Microservice cible | Route Eureka |
|---|---|---|
| `/events/**` | Événements | `lb://evenements` |
| `/rooms/**`, `/reservations/**` | Planning des salles | `lb://planning` |
| `/badges/**`, `/checkins/**` | Check-in / QR | `lb://checkin` |
| `/feedback/**`, `/speakers/**`, `/assignments/**` | Feedback & Intervenants | `lb://feedback` |
| `/stats/**` | Statistiques | `lb://statistiques` |
| `/notifications/**`, `/templates/**` | Notifications | `lb://notifications` |

---

## 5. Justification des Choix d'Interactions

### A. Communications Synchrones (OpenFeign)
Utilisées lorsque l'émetteur a un besoin immédiat et bloquant de la réponse pour valider son opération :
* **Événements ➔ Planning :** Réservation obligatoire de la salle lors de la publication (si le planning est indisponible ou s'il y a conflit, l'événement reste en brouillon).
* **Check-in ➔ Événements :** Vérification en direct au moment du scan que l'événement est bien ouvert (repli sur copie locale en cas d'indisponibilité temporaire).
* **Feedback ➔ Événements :** Vérification de l'existence et du statut de l'événement lors de l'affectation d'un conférencier ou membre du jury.

### B. Communications Asynchrones (RabbitMQ — Exchange Topic `evenements`)
Privilégiées pour diffuser des faits métier sans bloquer l'utilisateur et pour garantir la continuité de service en cas de panne temporaire d'un consommateur :
* `inscription.confirmee` *(Événements)* ➔ Check-in (génération du badge QR), Notifications (envoi du badge) et Statistiques (incrément).
* `badge.genere` *(Check-in)* ➔ Notifications (envoi par mail/SMS).
* `presence.enregistree` *(Check-in)* ➔ Statistiques (mise à jour affluence) et Feedback (ouverture du droit à laisser un avis).
* `evenement.publie / annule / termine` *(Événements)* ➔ Diffusion globale (Planning, Notifications, Feedback, Statistiques).
* `avis.envoye` *(Feedback)* ➔ Statistiques (recalcul de la note moyenne).
* `intervenant.affecte` *(Feedback)* ➔ Notifications (envoi de convocation).

---

## 6. Références & Diagrammes

* **Architecture technique globale :** [`Documentation/Diagrams/architecture-technique.png`](Documentation/Diagrams/architecture-technique.png)
* **Diagramme des cas d'utilisation :** [`Documentation/Diagrams/Use_Case.png`](Documentation/Diagrams/Use_Case.png)
* **Diagramme de classes :** [`Documentation/Diagrams/class-diagram.png`](Documentation/Diagrams/class-diagram.png)
* **Dossier technique complet (PDF) :** [`Documentation/Gestion_evenements.pdf`](Documentation/Gestion_evenements.pdf)