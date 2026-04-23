# CodeShop — Full-Stack E-Commerce for Digital Products

![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Security-JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![PayPal](https://img.shields.io/badge/Payments-PayPal-003087?style=for-the-badge&logo=paypal&logoColor=white)

**CodeShop** rappresenta un ecosistema full-stack concepito per rivoluzionare la vendita e la distribuzione di asset digitali. La piattaforma si distingue per un'architettura robusta che coniuga le prestazioni enterprise di **Spring Boot 3.x** e **Java 21** con un'interfaccia utente moderna basata su estetica **Glassomorphism** e navigazione fluida in modalità **SPA**. Grazie all'integrazione nativa con **PayPal REST API** e a un sistema di autenticazione stateless basato su **JWT**, il progetto garantisce transazioni sicure e un controllo granulare degli accessi. L'intero ciclo di vendita è completamente automatizzato attraverso l'implementazione del pattern **Observer**, che assicura la consegna istantanea dei prodotti tramite notifiche email e aree riservate, mentre la completa containerizzazione con **Docker** rende l'intera infrastruttura scalabile e pronta per il deployment immediato in ambienti di produzione.

---

## 📖 Indice
- [Caratteristiche Principali](#caratteristiche-principali)
- [Tech Stack](#tech-stack)
- [Design Patterns](#design-patterns)
- [Architettura del Progetto](#architettura-del-progetto)
- [Installazione e Setup](#installazione-e-setup)
- [🐳 Docker & Containerizzazione](#docker-containerizzazione)
- [Configurazione](#configurazione)
- [Team di Sviluppo](#team-di-sviluppo)

---

<a name="caratteristiche-principali"></a>
## ✨ Caratteristiche Principali

### 🔒 Autenticazione & Sicurezza
- **JWT (JSON Web Token)**: Autenticazione stateless con token a scadenza (24h).
- **RBAC**: Gestione ruoli differenziata tra `USER` e `ADMIN`.
- **Password Security**: Criptazione delle credenziali e notifiche email in caso di modifica.

### 🛒 Esperienza d'Acquisto
- **Catalogo Dinamico**: Navigazione prodotti filtrata per categorie (Snippet, Progetti, Database).
- **Carrello Avanzato**: Gestione persistente del carrello con ricalcolo totali lato server.
- **Integrazione PayPal**: Flusso di pagamento sicuro tramite PayPal REST SDK con verifica transazione lato server.

### 📦 Delivery Digitale
- **Download Automatico**: Al termine del pagamento, l'utente riceve istantaneamente un'email con il link di download.
- **Area Riservata**: Storico ordini completo con pulsante "Scarica" per ogni prodotto acquistato (disponibile per 14 giorni).
- **Notifiche Transazionali**: Sistema di notifiche via email per registrazione, ordini e sicurezza account.

### 🛠️ Backoffice Amministrativo
- **Gestione Catalogo (CRUD)**: Interfaccia dedicata per gli amministratori per aggiungere, modificare o rimuovere prodotti in tempo reale.
- **Controllo Accessi Avanzato**: Visualizzazione completa degli ordini e dei file per la gestione operativa.
- **Policy di Download**: Implementazione di restrizioni temporali (es. 14 giorni) per il download dei file da parte degli utenti, garantendo la sicurezza degli asset.

### 🎨 UI/UX
- **Modern Design**: Interfaccia responsiva con effetti Glassomorphism e animazioni fluide.
- **SPA (Single Page Application)**: Navigazione dinamica senza ricaricamento della pagina.

---

<a name="tech-stack"></a>
## 🛠️ Tech Stack

| Componente | Tecnologie |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.x, Spring Security |
| **Persistence** | Spring Data JPA, Hibernate, MySQL |
| **Security** | JSON Web Token (JWT) |
| **Payments** | PayPal REST API SDK |
| **Email** | Spring Mail (SMTP), HTML Templates |
| **Frontend** | HTML5, CSS3 (Vanilla), JavaScript (ES6+) |
| **Container** | Docker, Docker Compose |
| **Build Tool** | Maven |

---

<a name="design-patterns"></a>
## 📐 Design Patterns
Il progetto implementa diversi pattern architetturali per garantire scalabilità e manutenibilità:

- **Observer Pattern**: Utilizzato per il sistema di notifiche. Ogni azione rilevante (Registrazione, Ordine) notifica i vari `Observer` per l'invio di email o log di sistema.
- **MVC (Model-View-Controller)**: Separazione netta tra logica di business, persistenza e presentazione.
- **Repository Pattern**: Per l'astrazione dell'accesso ai dati tramite Spring Data JPA.

---

<a name="architettura-del-progetto"></a>
## 📂 Architettura del Progetto

```text
ProgettoFinale/
├── final_project/
│   ├── src/main/java/com/example/final_project/
│   │   ├── Config/          # Configurazioni (PayPal)
│   │   ├── Controller/      # Endpoint REST API
│   │   ├── Model/           # Entità JPA (User, Product, Order, etc.)
│   │   ├── Repository/      # Interfacce Spring Data JPA
│   │   ├── Service/         # Business Logic e integrazioni esterne
│   │   ├── Security/        # Logica JWT e Filtri di sicurezza
│   │   └── Observer/        # Implementazione del pattern Observer
│   └── src/main/resources/
│       ├── static/          # Frontend Assets (HTML, CSS, JS)
│       └── application.yaml # Configurazione applicativa
```

---

<a name="installazione-e-setup"></a>
## 🚀 Installazione e Setup

### Prerequisiti
- **JDK 21** o superiore
- **MySQL 8.0+**
- **Maven 3.8+**
- **Docker & Docker Compose** (opzionale per esecuzione in container)

### Step 1: Clonare il repository
```bash
git clone https://github.com/Michecosa/ProgettoFinale.git
cd ProgettoFinale/final_project
```

### Step 2: Configurazione Database
Crea un database MySQL chiamato `final_project`:
```sql
CREATE DATABASE final_project;
```

### Step 3: Configurazione Ambiente
Copia il file `.env.example` in un nuovo file chiamato `.env` e inserisci le tue credenziali (Database, Email SMTP, PayPal). 
È importante sapere che il progetto utilizza due file `.env` per diversi scopi:

*   **`.env` nella cartella principale (`ProgettoFinale/`)**: Viene utilizzato da **Docker Compose**. Quando lanci `docker-compose up`, Docker cerca questo file per configurare il database e l'applicazione nei container.
*   **`.env` dentro la cartella `final_project/`**: Viene utilizzato per l'**avvio locale** (tramite IntelliJ, VS Code o `./mvnw spring-boot:run`). Spring Boot carica automaticamente questo file per risolvere le variabili definite in `application.yaml`.

### Step 4: Build e Avvio
```bash
mvn clean install
mvn spring-boot:run
```
L'applicazione sarà disponibile all'indirizzo: `http://localhost:8080`

---

<a name="docker-containerizzazione"></a>
## 🐳 Docker & Containerizzazione

Il progetto è completamente containerizzato con Docker e Docker Compose, permettendo di avviare l'intero stack (App + Database) senza installare MySQL o Java localmente.

### Prerequisiti
- **Docker Desktop** installato e avviato
- **Maven 3.8+** (solo per compilare il JAR)

---

### Passo 1 — Configura il file `.env`

Copia `.env.example` in `.env` nella cartella principale (`ProgettoFinale/`) e compila i valori:

```bash
cp .env.example .env
```

> Se non hai una password per MySQL (es. XAMPP su Windows), lascia `DB_PASSWORD=` vuoto — funziona sia con password vuota che con una password impostata.

---

### Passo 2 — Compila il JAR

```bash
cd final_project
./mvnw clean package -DskipTests   # Mac / Linux
mvnw.cmd clean package -DskipTests  # Windows
cd ..
```

---

### Passo 3 — Avvia i container

Dalla cartella `ProgettoFinale/`:

```bash
docker compose up --build -d
```

Docker avvierà prima il database e aspetterà che sia pronto, poi avvierà Spring Boot. Al primo avvio Hibernate crea automaticamente tutte le tabelle.

---

### Passo 4 — Carica i dati iniziali

Dopo il primo avvio (necessario solo la prima volta, o dopo `docker compose down -v`):

**Mac / Linux:**
```bash
docker exec -i mysql_db mysql -uroot final_project < init-data.sql
# se hai impostato una password:
docker exec -i mysql_db mysql -uroot -pTUAPASSWORD final_project < init-data.sql
```

**Windows PowerShell:**
```powershell
Get-Content init-data.sql | docker exec -i mysql_db mysql -uroot final_project
# se hai impostato una password:
Get-Content init-data.sql | docker exec -i mysql_db mysql -uroot -pTUAPASSWORD final_project
```

L'applicazione è ora disponibile su **http://localhost:8080**

Account admin precaricato:
| Campo | Valore |
| :--- | :--- |
| Username | `admin` |
| Password | `miche` |
| Email | `admin@codemarketplace.it` |

---

### Comandi utili

| Comando | Descrizione |
| :--- | :--- |
| `docker compose up -d` | Avvia i container in background |
| `docker compose down` | Ferma i container (i dati rimangono) |
| `docker compose down -v` | Ferma e **cancella** il database |
| `docker compose logs -f app-security` | Segui i log di Spring Boot in tempo reale |
| `docker compose build --no-cache` | Ricostruisce l'immagine da zero |

> **Attenzione**: `docker compose down -v` cancella tutti i dati del database. Dopo questo comando sarà necessario ripetere il Passo 4.

---

### Infrastruttura
- **`mysql_db`**: MySQL 8.0 con healthcheck — Spring Boot aspetta che il DB sia pronto prima di partire.
- **`spring_app`**: Immagine Alpine con JRE 21, porta `8080` esposta sull'host.

---

<a name="configurazione"></a>
## ⚙️ Configurazione

### PayPal Sandbox
Per testare i pagamenti, assicurati di avere un account developer su [PayPal Developer](https://developer.paypal.com/) e configura le chiavi:
```yaml
paypal:
  client-id: YOUR_CLIENT_ID
  client-secret: YOUR_CLIENT_SECRET
  mode: sandbox
```
### Variabili d'Ambiente (.env)
Il progetto utilizza il file `.env` per separare i segreti dal codice. Assicurati che le seguenti chiavi siano configurate:

| Variabile | Descrizione |
| :--- | :--- |
| `DB_URL` | URL di connessione al database MySQL |
| `DB_USERNAME` | Username del database |
| `DB_PASSWORD` | Password del database |
| `MAIL_USERNAME` | Email per l'invio delle notifiche |
| `MAIL_PASSWORD` | App Password (per Gmail) o password SMTP |
| `JWT_SECRET` | Chiave segreta per la firma dei token (Base64) |
| `JWT_EXPIRATION` | Durata del token in millisecondi |
| `PAYPAL_CLIENT_ID` | Client ID ottenuto dalla dashboard PayPal |
| `PAYPAL_CLIENT_SECRET` | Secret ottenuto dalla dashboard PayPal |
| `PAYPAL_MODE` | `sandbox` per test o `live` per produzione |
---

<a name="team-di-sviluppo"></a>
## 👥 Team di Sviluppo

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/DonatoMorra">
        <img src="https://github.com/DonatoMorra.png" width="100" style="border-radius:50%"/><br/>
        <b>Donato Morra</b><br>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Michecosa">
        <img src="https://github.com/Michecosa.png" width="100" style="border-radius:50%"/><br/>
        <b>Michela Della Gatta</b><br>
      </a>
    </td>
  </tr>
</table>

---

## 📄 Licenza
Questo progetto è distribuito sotto Licenza MIT. Consulta il file `LICENSE` per ulteriori dettagli.
