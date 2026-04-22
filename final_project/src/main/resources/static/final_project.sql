-- MySQL dump 10.13  Distrib 8.0.45, for macos15 (arm64)
--
-- Host: localhost    Database: final_project
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '9b022f3c-3cdb-11f1-88cc-881df782a2a7:1-225';

--
-- Table structure for table `carrello`
--

DROP TABLE IF EXISTS `carrello`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrello` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_utente` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl287dga2nb4ahi1j34on39ruk` (`id_utente`),
  CONSTRAINT `FKimyxl9cko6g83slko5cldpbh` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrello`
--

LOCK TABLES `carrello` WRITE;
/*!40000 ALTER TABLE `carrello` DISABLE KEYS */;
INSERT INTO `carrello` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8);
/*!40000 ALTER TABLE `carrello` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Snippet & Algoritmi'),(2,'Web Template'),(3,'Script Database'),(4,'Progetti Completi'),(5,'UI Kit & Design');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_quantity`
--

DROP TABLE IF EXISTS `item_quantity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_quantity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `qtn` int NOT NULL,
  `id_carrello` bigint DEFAULT NULL,
  `id_ordine` bigint DEFAULT NULL,
  `id_prodotto` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8nu7h8792mtrbrg27rliswibd` (`id_carrello`),
  KEY `FKiao22urd2ersaodgcu4ucla00` (`id_ordine`),
  KEY `FK3o4h1cbyaiega6r09t4xbpuxg` (`id_prodotto`),
  CONSTRAINT `FK3o4h1cbyaiega6r09t4xbpuxg` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id`),
  CONSTRAINT `FK8nu7h8792mtrbrg27rliswibd` FOREIGN KEY (`id_carrello`) REFERENCES `carrello` (`id`),
  CONSTRAINT `FKiao22urd2ersaodgcu4ucla00` FOREIGN KEY (`id_ordine`) REFERENCES `ordine` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_quantity`
--

LOCK TABLES `item_quantity` WRITE;
/*!40000 ALTER TABLE `item_quantity` DISABLE KEYS */;
INSERT INTO `item_quantity` VALUES (1,1,2,NULL,4),(4,1,NULL,1,1),(44,1,1,NULL,2),(48,1,NULL,8,3),(50,1,NULL,9,3),(52,1,NULL,10,3),(54,1,NULL,11,3),(57,1,NULL,12,2),(58,1,NULL,12,3),(63,1,NULL,13,2),(64,1,NULL,13,3);
/*!40000 ALTER TABLE `item_quantity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `consegna` date DEFAULT NULL,
  `indirizzo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pagato` bit(1) NOT NULL,
  `id_utente` bigint DEFAULT NULL,
  `data_pagamento` date DEFAULT NULL,
  `data_ordine` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgsxxfj3dm1kfppteavqrvkwcr` (`id_utente`),
  CONSTRAINT `FKgsxxfj3dm1kfppteavrvkwcr` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
INSERT INTO `ordine` VALUES (1,'2026-04-09','Via Roma 12, Milano',_binary '',2,'2026-04-09','2026-04-09'),(2,'2026-04-14','Via Roma 12, Milano',_binary '',2,'2026-04-14','2026-04-14'),(3,'2026-04-11','Corso Vittorio 8, Torino',_binary '',3,'2026-04-11','2026-04-11'),(4,'2026-04-22','Corso Vittorio 8, Torino',_binary '\0',3,NULL,'2026-04-20'),(5,'2026-04-07','Viale Europa 3, Roma',_binary '',4,'2026-04-07','2026-04-07'),(6,'2026-04-20','Via Giorgio Vasari 4, Napoli',_binary '\0',5,NULL,'2026-04-18'),(7,'2026-04-20','Via Santa Palomba 9, Roma',_binary '\0',5,NULL,'2026-04-17'),(8,'2026-04-21','donatomorra90@gmail.com',_binary '\0',5,NULL,'2026-04-21'),(9,'2026-04-21','donatomorra90@gmail.com',_binary '\0',5,NULL,'2026-04-21'),(10,'2026-04-21','donatomorra90@gmail.com',_binary '\0',5,NULL,'2026-04-21'),(11,'2026-04-21','donatomorra90@gmail.com',_binary '\0',5,NULL,'2026-04-21'),(12,'2026-04-22','michela@example.com',_binary '\0',7,NULL,'2026-04-22'),(13,'2026-04-22','micheladellagatta06@gmail.com',_binary '\0',7,NULL,'2026-04-22');
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotto`
--

DROP TABLE IF EXISTS `prodotto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `prezzo` double NOT NULL,
  `disponibile` bit(1) NOT NULL,
  `stock` int NOT NULL,
  `link_download` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto`
--

LOCK TABLES `prodotto` WRITE;
/*!40000 ALTER TABLE `prodotto` DISABLE KEYS */;
INSERT INTO `prodotto` VALUES (1,'Gestionale Scolastico',14.99,_binary '',999999,'https://drive.google.com/file/d/1Lm4wAZBZPwnGq0bnMCgy8pFa440BlahE/view?usp=drive_link'),(2,'Gestionale Hotel',9.99,_binary '',999999,'https://drive.google.com/file/d/1g_QgU2dXjsUJ_OuttkLMkBY8f-g-BbOa/view?usp=drive_link'),(3,'Gestione Calcoli Matematici',12.99,_binary '',999999,'https://drive.google.com/file/d/1XyK6v65hyxoZ9jIhVl3c3UIN_E7yu0RS/view?usp=drive_link'),(4,'Gestionale di un Negozio di Prodotti',49.99,_binary '',999999,'https://drive.google.com/file/d/1wDFFp8JNK9DQ0qU9GST1p1TTkpXszvDv/view?usp=drive_link'),(5,'World SQL',119.99,_binary '',999,'https://drive.google.com/file/d/1Yze-Md9w7QxIa9nNo8_AtgD1a3Dknuwz/view?usp=sharing'),(6,'Gestione Clinica SQL',599.99,_binary '',100,'https://drive.google.com/file/d/1D9hsj49kO4HCm7IffpLM92vQOZXjccWa/view?usp=sharing'),(7,'Classic Models SQL',35.15,_binary '',100,'https://drive.google.com/file/d/1wHP5W99f4UFL84Jo-W3mBnFgsVhgHnbz/view?usp=sharing'),(8,'Gestione Festival SQL',150,_binary '',500,'https://drive.google.com/file/d/1_ghvm--LYz6KOUsIy2F76S2ycoyLOTA7/view?usp=sharing'),(11,'Gestionale Pizzeria',59.99,_binary '',999999,'https://drive.google.com/file/d/12DUOjJvydLJ8whgvhaiehTF47JLxSz6e/view?usp=drive_link'),(12,'Gestionale NASA',10.05,_binary '\0',999,'https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExa2lyN3JoZTZzbzA1cm5lbDVyd2hueDZpMTY5NGt5Y3FtZGx5MWpyMSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/qZgHBlenHa1zKqy6Zn/giphy.gif');
/*!40000 ALTER TABLE `prodotto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotto_to_categoria`
--

DROP TABLE IF EXISTS `prodotto_to_categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotto_to_categoria` (
  `id_prodotto` bigint NOT NULL,
  `id_categoria` bigint NOT NULL,
  KEY `FK8rddyu5sgh2h9rew02rara5jr` (`id_categoria`),
  KEY `FK4jv72cip3f0cq3ij3otu8lole` (`id_prodotto`),
  CONSTRAINT `FK4jv72cip3f0cq3ij3otu8lole` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id`),
  CONSTRAINT `FK8rddyu5sgh2h9rew02rara5jr` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto_to_categoria`
--

LOCK TABLES `prodotto_to_categoria` WRITE;
/*!40000 ALTER TABLE `prodotto_to_categoria` DISABLE KEYS */;
INSERT INTO `prodotto_to_categoria` VALUES (11,4),(4,4),(2,4),(1,1),(3,1),(6,3),(7,3),(8,3),(5,3),(12,4);
/*!40000 ALTER TABLE `prodotto_to_categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mail` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `roles` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKp72xb2utk1ksr1ymf57y37w04` (`mail`),
  UNIQUE KEY `UK2vq82crxh3p7upassu0k1kmte` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (1,'admin@codemarketplace.it','$2a$10$ax5LoKNcgorZrPzL/yHyZ.Lda/Ssg2Dn0d/uC/c.8/dpR26heuJhy','ROLE_ADMIN','admin'),(2,'mario@mail.it','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.','ROLE_USER','mario'),(3,'giulia@mail.it','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.','ROLE_USER','giulia'),(4,'luca@mail.it','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uVs7.IEC.','ROLE_USER','luca'),(5,'donatomorra90@gmail.com','$2a$10$.CyHBmFoGgCuZZjsTSR.2.LoSfsyKd8CJFVJoWMxD6saCVrNRc8dO','ROLE_ADMIN','donato'),(6,'mick@mick.mick','$2a$10$wsUkT/z2mLu2u.Uh6RcoCey.rxRtU3eR6rV9lAWrrZhKKzUt1MoC2','ROLE_USER','mick_'),(7,'micheladellagatta1@gmail.com','$2a$10$ax5LoKNcgorZrPzL/yHyZ.Lda/Ssg2Dn0d/uC/c.8/dpR26heuJhy','ROLE_ADMIN','miche'),(8,'miche@example.com','$2a$10$ax5LoKNcgorZrPzL/yHyZ.Lda/Ssg2Dn0d/uC/c.8/dpR26heuJhy',NULL,'michela');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente_ruolo`
--

DROP TABLE IF EXISTS `utente_ruolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente_ruolo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `id_utente` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjjcwv1trgn2xeg7h4fa0sp7yw` (`id_utente`),
  CONSTRAINT `FKjjcwv1trgn2xeg7h4fa0sp7yw` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente_ruolo`
--

LOCK TABLES `utente_ruolo` WRITE;
/*!40000 ALTER TABLE `utente_ruolo` DISABLE KEYS */;
INSERT INTO `utente_ruolo` VALUES (1,'ROLE_USER',6),(2,'ROLE_USER',7),(3,'ROLE_USER',8),(4,'ROLE_ADMIN',1),(5,'ROLE_ADMIN',7);
/*!40000 ALTER TABLE `utente_ruolo` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-22 14:46:33
