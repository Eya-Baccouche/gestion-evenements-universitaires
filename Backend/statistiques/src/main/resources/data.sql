-- ===================================================================
-- Donnees de demonstration riches pour le Microservice Statistiques
-- Projet AWD 2026-2027 · Equipe Warriors
-- ===================================================================

-- 1. Statistiques globales par evenement (Table: stat_evenements)
-- (evenement_id, titre, date_evenement, statut, inscrits, presents, taux_presence, note_moyenne, nb_avis)
INSERT INTO stat_evenements (evenement_id, titre, date_evenement, statut, inscrits, presents, taux_presence, note_moyenne, nb_avis)
VALUES 
    (1, 'Conference Intelligence Artificielle & Deep Learning', '2026-11-01', 'TERMINE', 120, 105, 87.50, 4.7, 45),
    (2, 'Hackathon AWD 2026-2027',                              '2026-11-05', 'PUBLIE',   80,  76,  95.00, 4.9, 38),
    (3, 'Workshop Cybersecurite & Ethical Hacking',             '2026-11-08', 'PUBLIE',   60,  48,  80.00, 4.3, 22),
    (4, 'Forum Entreprises & Stages PFE',                       '2026-11-10', 'PUBLIE',  250, 210,  84.00, 4.4, 80),
    (5, 'Seminaire Cloud Computing & Microservices',            '2026-11-15', 'PUBLIE',   90,  72,  80.00, 4.6, 30),
    (6, 'Ceremonie de Remise des Diplomes',                     '2026-11-20', 'TERMINE', 300, 285,  95.00, 4.9, 120)
ON CONFLICT (evenement_id) DO NOTHING;

-- 2. Instantanes temporels pour la timeline (Table: instantanes)
-- Permet de tracer l'evolution de l'affluence au cours du temps pour chaque evenement
INSERT INTO instantanes (evenement_id, date, inscrits, presents)
VALUES 
    -- Progression de l'Evenement 1 (Conference IA)
    (1, CURRENT_TIMESTAMP - INTERVAL '4 hours', 120, 15),
    (1, CURRENT_TIMESTAMP - INTERVAL '3 hours', 120, 55),
    (1, CURRENT_TIMESTAMP - INTERVAL '2 hours', 120, 85),
    (1, CURRENT_TIMESTAMP - INTERVAL '1 hour',  120, 100),
    (1, CURRENT_TIMESTAMP,                      120, 105),

    -- Progression de l'Evenement 2 (Hackathon AWD)
    (2, CURRENT_TIMESTAMP - INTERVAL '6 hours', 80, 20),
    (2, CURRENT_TIMESTAMP - INTERVAL '4 hours', 80, 50),
    (2, CURRENT_TIMESTAMP - INTERVAL '2 hours', 80, 72),
    (2, CURRENT_TIMESTAMP,                      80, 76),

    -- Progression de l'Evenement 3 (Workshop Cybersecurite)
    (3, CURRENT_TIMESTAMP - INTERVAL '3 hours', 60, 12),
    (3, CURRENT_TIMESTAMP - INTERVAL '2 hours', 60, 34),
    (3, CURRENT_TIMESTAMP - INTERVAL '1 hour',  60, 45),
    (3, CURRENT_TIMESTAMP,                      60, 48),

    -- Progression de l'Evenement 4 (Forum Entreprises PFE)
    (4, CURRENT_TIMESTAMP - INTERVAL '5 hours', 250, 40),
    (4, CURRENT_TIMESTAMP - INTERVAL '3 hours', 250, 120),
    (4, CURRENT_TIMESTAMP - INTERVAL '2 hours', 250, 180),
    (4, CURRENT_TIMESTAMP - INTERVAL '1 hour',  250, 205),
    (4, CURRENT_TIMESTAMP,                      250, 210),

    -- Progression de l'Evenement 5 (Seminaire Cloud)
    (5, CURRENT_TIMESTAMP - INTERVAL '2 hours', 90, 30),
    (5, CURRENT_TIMESTAMP - INTERVAL '1 hour',  90, 60),
    (5, CURRENT_TIMESTAMP,                      90, 72),

    -- Progression de l'Evenement 6 (Remise des Diplomes)
    (6, CURRENT_TIMESTAMP - INTERVAL '3 hours', 300, 80),
    (6, CURRENT_TIMESTAMP - INTERVAL '2 hours', 300, 190),
    (6, CURRENT_TIMESTAMP - INTERVAL '1 hour',  300, 260),
    (6, CURRENT_TIMESTAMP,                      300, 285)
ON CONFLICT DO NOTHING;
