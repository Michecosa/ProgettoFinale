-- ==========================================================
-- 1. SVUOTAMENTO DELLE TABELLE
-- ==========================================================

-- Disabilita i vincoli per evitare errori di Foreign Key durante il reset
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM item_quantity;
DELETE FROM prodotto_to_categoria;
DELETE FROM ordine;
DELETE FROM carrello;
DELETE FROM prodotto;
DELETE FROM categoria;
DELETE FROM utente;

-- Opzionale: Reset dei contatori auto-increment (se il database non lo fa con DELETE)
-- ALTER TABLE utente AUTO_INCREMENT = 1;
-- ALTER TABLE prodotto AUTO_INCREMENT = 1;

-- ==========================================================
-- 2. INSERIMENTO DATI CON ID ESPLICITI
-- ==========================================================

-- Tabella utente
INSERT INTO utente (id, username, mail, password, roles) VALUES
(1, 'admin', 'admin@store.it', 'admin123', 'ROLE_ADMIN'),
(2, 'mario_rossi', 'mario.rossi@gmail.com', 'mario789', 'ROLE_USER'),
(3, 'luca_bianchi', 'l.bianchi@outlook.it', 'luca456', 'ROLE_USER');

-- Tabella categoria
INSERT INTO categoria (id, nome) VALUES
(1, 'Elettronica'),
(2, 'Casa e Cucina'),
(3, 'Libri'),
(4, 'Sport');

-- Tabella prodotto
INSERT INTO prodotto (id, nome, prezzo) VALUES
(1, 'Smartphone Alpha', 699.99),
(2, 'Laptop Pro 15', 1250.50),
(3, 'Macchina del Caffè', 89.00),
(4, 'Manuale Java Spring', 45.00),
(5, 'Borraccia Termica', 15.90);

-- Tabella di Join prodotto_to_categoria (Non ha un ID proprio, usa FK)
INSERT INTO prodotto_to_categoria (id_prodotto, id_categoria) VALUES
(1, 1), -- Smartphone -> Elettronica
(2, 1), -- Laptop -> Elettronica
(3, 2), -- Macchina Caffè -> Casa
(4, 3), -- Libro -> Libri
(5, 4), -- Borraccia -> Sport
(5, 2); -- Borraccia -> Casa

-- Tabella carrello
INSERT INTO carrello (id, id_utente) VALUES
(1, 1), -- Carrello per Admin
(2, 2), -- Carrello per Mario
(3, 3); -- Carrello per Luca

-- Tabella ordine
INSERT INTO ordine (id, id_utente, pagato, consegna, indirizzo) VALUES
(1, 2, true, '2026-05-10', 'Via Roma 10, Milano'),
(2, 3, false, '2026-05-15', 'Corso Vittorio Emanuele 5, Torino');

-- Tabella item_quantity
-- Inserimenti legati ai carrelli (id_ordine è NULL)
INSERT INTO item_quantity (id, qtn, id_prodotto, id_carrello, id_ordine) VALUES
(1, 1, 1, 2, NULL), -- 1x Smartphone nel carrello di Mario
(2, 2, 5, 2, NULL), -- 2x Borracce nel carrello di Mario
(3, 1, 2, 3, NULL); -- 1x Laptop nel carrello di Luca

-- Inserimenti legati agli ordini (id_carrello è NULL)
INSERT INTO item_quantity (id, qtn, id_prodotto, id_carrello, id_ordine) VALUES
(4, 1, 3, NULL, 1), -- 1x Macchina Caffè nell'ordine 1 (di Mario)
(5, 1, 4, NULL, 2); -- 1x Libro nell'ordine 2 (di Luca)

-- ==========================================================
-- 3. RIPRISTINO VINCOLI
-- ==========================================================
SET FOREIGN_KEY_CHECKS = 1;