# CodeShop — Full-Stack E-Commerce for Digital Products

![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Security-JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![PayPal](https://img.shields.io/badge/Payments-PayPal-003087?style=for-the-badge&logo=paypal&logoColor=white)

**CodeShop** è una piattaforma e-commerce full-stack moderna dedicata alla vendita di prodotti digitali come snippet di codice, progetti software e database. Il sistema gestisce l'intero ciclo di vendita: dall'autenticazione sicura al checkout con PayPal, fino alla consegna automatizzata tramite email e area riservata.

---

## 📖 Indice
- [Caratteristiche Principali](#caratteristiche-principali)
- [Tech Stack](#tech-stack)
- [Design Patterns](#design-patterns)
- [Architettura del Progetto](#architettura-del-progetto)
- [Installazione e Setup](#installazione-e-setup)
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
- **Area Riservata**: Storico ordini completo con pulsante "Scarica" per ogni prodotto acquistato.
- **Notifiche Transazionali**: Sistema di notifiche via email per registrazione, ordini e sicurezza account.

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
