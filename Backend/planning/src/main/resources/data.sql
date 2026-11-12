-- ============================================================
-- Données de test — Microservice Planning des Salles
-- ============================================================

-- ---- Bâtiments ----
INSERT INTO batiment (nom, adresse, nb_etages) VALUES
('Bâtiment A - Sciences', 'Campus Principal, Rue des Sciences', 4),
('Bâtiment B - Technologie', 'Campus Principal, Avenue de la Tech', 3),
('Bâtiment C - Lettres', 'Campus Nord, Boulevard des Lettres', 2);

-- ---- Salles ----
INSERT INTO salle (nom, type_salle, capacite, etage, batiment_id, equipements, statut, description) VALUES
('Amphi A',      'AMPHI',        300, 0, 1, 'projecteur, sonorisation, wifi, climatisation', 'DISPONIBLE',    'Grand amphithéâtre pour conférences'),
('Salle 101',    'SALLE_COURS',  60,  1, 1, 'projecteur, tableau',                             'DISPONIBLE',    'Salle de cours standard rez-de-chaussée + 1'),
('Salle 102',    'SALLE_COURS',  40,  1, 1, 'tableau',                                         'DISPONIBLE',    'Petite salle de cours'),
('Labo Info 1',  'LABORATOIRE',  30,  2, 1, 'pc, wifi, projecteur',                            'DISPONIBLE',    'Laboratoire informatique 30 postes'),
('Salle TP Phys', 'SALLE_TP',    25,  3, 1, 'tableau, matériel TP',                            'EN_MAINTENANCE','En maintenance jusqu''à fin de mois'),
('Amphi B',      'AMPHI',        200, 0, 2, 'projecteur, sonorisation, wifi',                 'DISPONIBLE',    'Amphithéâtre technologie'),
('Salle 201',    'SALLE_COURS',  50,  2, 2, 'projecteur, tableau, wifi',                      'DISPONIBLE',    'Salle équipée Bâtiment B'),
('Salle Réunion B1','SALLE_REUNION',15,0,2,'écran, visio, wifi',                             'DISPONIBLE',    'Salle de réunion avec visioconférence'),
('Salle 105',    'SALLE_COURS',  35,  1, 3, 'tableau',                                         'FERME',         'Fermée pour rénovation'),
('Labo Chimie',  'LABORATOIRE',  20,  2, 3, 'hotte, matériel chimie',                          'DISPONIBLE',    'Laboratoire de chimie');

-- ---- Réservations ----
INSERT INTO reservation_salle (salle_id, titre, description, date_reservation, heure_debut, heure_fin, statut, demande_par, role_demandeur, evenement_id, nombre_personnes)
VALUES
(1, 'Conférence IA', 'Conférence intro intelligence artificielle', CURRENT_DATE + 2, '09:00', '12:00', 'CONFIRMEE',  'organisateur1@univ.fr', 'ORGANISATEUR', 101, 280),
(2, 'Cours Algo',     'TD Algorithmique avancée',                   CURRENT_DATE + 1, '08:30', '10:00', 'CONFIRMEE',  'organisateur2@univ.fr', 'ORGANISATEUR', 102, 55),
(6, 'Soutenance PFE', 'Soutenances projets de fin d''études',        CURRENT_DATE + 3, '14:00', '18:00', 'EN_ATTENTE',  'organisateur3@univ.fr', 'ORGANISATEUR', 103, 180),
(7, 'Réunion jury',   'Réunion du jury d''admission',                CURRENT_DATE + 1, '10:00', '12:30', 'CONFIRMEE',  'admin1@univ.fr',        'ADMIN',        NULL, 45),
(8, 'Brainstorm',     'Atelier brainstorming département',          CURRENT_DATE + 2, '13:00', '15:00', 'EN_ATTENTE',  'organisateur1@univ.fr', 'ORGANISATEUR', NULL, 12);
