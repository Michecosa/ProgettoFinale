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
-- Utente admin di default:
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

-- Utenti
INSERT INTO utente (id, mail, password, username) VALUES
(1,  'admin@codemarketplace.it',       '$2a$10$ax5LoKNcgorZrPzL/yHyZ.Lda/Ssg2Dn0d/uC/c.8/dpR26heuJhy', 'admin'),
(2,  'mario@mail.it',                  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.', 'mario'),
(3,  'giulia@mail.it',                 '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.', 'giulia'),
(4,  'luca@mail.it',                   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.', 'luca'),
(5,  'donatomorra90@gmail.com',         '$2a$10$.CyHBmFoGgCuZZjsTSR.2.LoSfsyKd8CJFVJoWMxD6saCVrNRc8dO', 'donato'),
(6,  'mick@mick.mick',                 '$2a$10$wsUkT/z2mLu2u.Uh6RcoCey.rxRtU3eR6rV9lAWrrZhKKzUt1MoC2', 'mick_'),
(7,  'micheladellagatta1@gmail.com',    '$2a$10$ax5LoKNcgorZrPzL/yHyZ.Lda/Ssg2Dn0d/uC/c.8/dpR26heuJhy', 'miche'),
(8,  'miche@example.com',              '$2a$10$ax5LoKNcgorZrPzL/yHyZ.Lda/Ssg2Dn0d/uC/c.8/dpR26heuJhy', 'michela'),
(9,  'mich@example.com',               '$2a$10$NnVS9uAidZ2/FEFw3cei2uImGStVxWbgP989lcHpA9qYfZl.XIe1S',  'mich');

-- Ruoli
INSERT INTO utente_ruolo (id_utente, nome) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER'),
(5, 'ROLE_ADMIN'),
(6, 'ROLE_USER'),
(7, 'ROLE_ADMIN'),
(8, 'ROLE_USER'),
(9, 'ROLE_USER');

-- Carrelli (uno per utente)
INSERT INTO carrello (id_utente) VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9);

-- Categorie
INSERT INTO categoria (id, nome) VALUES
(1, 'Snippet & Algoritmi'),
(2, 'Web Template'),
(3, 'Script Database'),
(4, 'Progetti Completi'),
(5, 'UI Kit & Design');

-- Prodotti
INSERT INTO prodotto (id, nome, prezzo, disponibile, stock, link_download) VALUES
(1,  'Gestionale Scolastico',               14.99, 1, 999999, 'https://drive.google.com/file/d/1Lm4wAZBZPwnGq0bnMCgy8pFa440BlahE/view?usp=drive_link'),
(2,  'Gestionale Hotel',                     9.99, 1, 999999, 'https://drive.google.com/file/d/1g_QgU2dXjsUJ_OuttkLMkBY8f-g-BbOa/view?usp=drive_link'),
(3,  'Gestione Calcoli Matematici',          12.99, 1, 999999, 'https://drive.google.com/file/d/1XyK6v65hyxoZ9jIhVl3c3UIN_E7yu0RS/view?usp=drive_link'),
(4,  'Gestionale di un Negozio di Prodotti', 49.99, 1, 999999, 'https://drive.google.com/file/d/1wDFFp8JNK9DQ0qU9GST1p1TTkpXszvDv/view?usp=drive_link'),
(5,  'World SQL',                           119.99, 1,    999, 'https://drive.google.com/file/d/1Yze-Md9w7QxIa9nNo8_AtgD1a3Dknuwz/view?usp=sharing'),
(6,  'Gestione Clinica SQL',                599.99, 1,    100, 'https://drive.google.com/file/d/1D9hsj49kO4HCm7IffpLM92vQOZXjccWa/view?usp=sharing'),
(7,  'Classic Models SQL',                   35.15, 1,    100, 'https://drive.google.com/file/d/1wHP5W99f4UFL84Jo-W3mBnFgsVhgHnbz/view?usp=sharing'),
(8,  'Gestione Festival SQL',               150.00, 1,    500, 'https://drive.google.com/file/d/1_ghvm--LYz6KOUsIy2F76S2ycoyLOTA7/view?usp=sharing'),
(11, 'Gestionale Pizzeria',                  89.99, 1, 999999, 'https://drive.google.com/file/d/12DUOjJvydLJ8whgvhaiehTF47JLxSz6e/view?usp=drive_link'),
(12, 'Gestionale NASA',                      10.05, 0,      1, 'https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExa2lyN3JoZTZzbzA1cm5lbDVyd2hueDZpMTY5NGt5Y3FtZGx5MWpyMSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/qZgHBlenHa1zKqy6Zn/giphy.gif'),
(18, 'Gestionale di prova',                   0.02, 0,      1, 'https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExa2lyN3JoZTZzbzA1cm5lbDVyd2hueDZpMTY5NGt5Y3FtZGx5MWpyMSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/qZgHBlenHa1zKqy6Zn/giphy.gif');

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
(11, 1),(11, 4),
(12, 4),
(18, 2),(18, 4),(18, 5);
