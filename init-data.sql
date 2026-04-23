-- Script di inizializzazione dati per Docker
-- Eseguire DOPO `docker compose up -d` (le tabelle vengono create da Hibernate al primo avvio)
--
-- Mac/Linux:
--   docker exec -i mysql_db mysql -uroot final_project < init-data.sql
--   (con password: docker exec -i mysql_db mysql -uroot -pTUAPASSWORD final_project < init-data.sql)
--
-- Windows PowerShell:
--   Get-Content init-data.sql | docker exec -i mysql_db mysql -uroot final_project
--   (con password: Get-Content init-data.sql | docker exec -i mysql_db mysql -uroot -pTUAPASSWORD final_project)
--
-- Utente admin di default creato da questo script:
--   username : admin
--   password : miche
--   email    : admin@codemarketplace.it

SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE item_quantity;
TRUNCATE TABLE carrello;
TRUNCATE TABLE ordine;
TRUNCATE TABLE prodotto_to_categoria;
TRUNCATE TABLE prodotto;
TRUNCATE TABLE categoria;
TRUNCATE TABLE utente_ruolo;
TRUNCATE TABLE utente;
SET FOREIGN_KEY_CHECKS=1;

-- Admin di default (password: password)
INSERT INTO utente (id, mail, password, username) VALUES
(1, 'admin@codemarketplace.it', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.', 'admin');

INSERT INTO utente_ruolo (id_utente, nome) VALUES
(1, 'ROLE_ADMIN');

INSERT INTO carrello (id_utente) VALUES (1);

-- Categorie
INSERT INTO categoria (id, nome) VALUES
(1, 'Snippet & Algoritmi'),
(2, 'Web Template'),
(3, 'Script Database'),
(4, 'Progetti Completi'),
(5, 'UI Kit & Design');

-- Prodotti
INSERT INTO prodotto (id, nome, prezzo, disponibile, stock, link_download) VALUES
(1,  'Gestionale Scolastico',               14.99,  1, 999999, 'https://drive.google.com/file/d/1Lm4wAZBZPwnGq0bnMCgy8pFa440BlahE/view?usp=drive_link'),
(2,  'Gestionale Hotel',                     9.99,  1, 999999, 'https://drive.google.com/file/d/1g_QgU2dXjsUJ_OuttkLMkBY8f-g-BbOa/view?usp=drive_link'),
(3,  'Gestione Calcoli Matematici',          12.99,  1, 999999, 'https://drive.google.com/file/d/1XyK6v65hyxoZ9jIhVl3c3UIN_E7yu0RS/view?usp=drive_link'),
(4,  'Gestionale di un Negozio di Prodotti', 49.99,  1, 999999, 'https://drive.google.com/file/d/1wDFFp8JNK9DQ0qU9GST1p1TTkpXszvDv/view?usp=drive_link'),
(5,  'World SQL',                           119.99,  1,    999, 'https://drive.google.com/file/d/1Yze-Md9w7QxIa9nNo8_AtgD1a3Dknuwz/view?usp=sharing'),
(6,  'Gestione Clinica SQL',                599.99,  1,    100, 'https://drive.google.com/file/d/1D9hsj49kO4HCm7IffpLM92vQOZXjccWa/view?usp=sharing'),
(7,  'Classic Models SQL',                   35.15,  1,    100, 'https://drive.google.com/file/d/1wHP5W99f4UFL84Jo-W3mBnFgsVhgHnbz/view?usp=sharing'),
(8,  'Gestione Festival SQL',               150.00,  1,    500, 'https://drive.google.com/file/d/1_ghvm--LYz6KOUsIy2F76S2ycoyLOTA7/view?usp=sharing'),
(11, 'Gestionale Pizzeria',                  89.99,  1, 999999, 'https://drive.google.com/file/d/12DUOjJvydLJ8whgvhaiehTF47JLxSz6e/view?usp=drive_link');

-- Associazioni Prodotto -> Categoria
INSERT INTO prodotto_to_categoria (id_prodotto, id_categoria) VALUES
(1, 1),(1, 4),
(2, 1),(2, 4),
(3, 1),
(4, 4),
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(11, 1),(11, 4);
