# Eureka Server

Serveur d'enregistrement et de découverte des services (**Service Discovery**) pour la plateforme **Gestion des événements universitaires** (équipe Warriors).

## Rôle
- Maintenir l'annuaire dynamique de tous les microservices démarrés (`evenements`, `planning`, `checkin`, `feedback`, `statistiques`, `notifications`, `api-gateway`).
- Permettre à l'API Gateway et aux appels synchrones OpenFeign de résoudre les services par leur nom logique (`lb://nom-du-service`) sans coder d'adresses IP ou de ports en dur.

## Port
- **8761** (Tableau de bord : `http://localhost:8761`)
