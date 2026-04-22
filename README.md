# CodeShop — Full-Stack Spring Boot E-Commerce

![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203.x-brightgreen)
![JWT](https://img.shields.io/badge/Security-JSON%20Web%20Token-blue)
![JPA](https://img.shields.io/badge/Persistence-Spring%20Data%20JPA-orange)
![Spring Mail](https://img.shields.io/badge/Notifications-Spring%20Mail-yellow)

Un sistema e-commerce per prodotti digitali (snippet, progetti, database) con autenticazione JWT, gestione ordini, download automatico dei file acquistati e notifiche email transazionali.


## Sviluppatori

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/DonatoMorra">
        <img src="https://github.com/DonatoMorra.png" width="80" style="border-radius:50%"/><br/>
        <b>Donato Morra</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Michecosa">
        <img src="https://github.com/Michecosa.png" width="80" style="border-radius:50%"/><br/>
        <b>Michela Della Gatta</b>
      </a>
    </td>
  </tr>
</table>

<br>
<br>
<br>

## Indice

- [Tech Stack](#tech-stack)
- [Funzionalità](#funzionalità)
- [Architettura del Database](#architettura-del-database)
- [Sicurezza](#sicurezza)
- [Struttura del Progetto](#struttura-del-progetto)

---

## Tech Stack

| Layer | Tecnologia |
| :--- | :--- |
| Backend | Spring Boot 3.x, Spring MVC |
| Security | Spring Security + JWT (stateless, 24h expiry) |
| Persistence | Spring Data JPA / Hibernate, MySQL |
| Email | Spring Mail (SMTP Gmail, template HTML) |
| Payments | PayPal REST SDK |
| Pattern | Observer (notifiche email) |
| Frontend | HTML/CSS/JS vanilla (SPA) |

---

## Funzionalità

### Autenticazione & Autorizzazione
- Registrazione e login protetti con JWT
- Ruoli utente (`USER` / `ADMIN`)
- Ogni risorsa (carrello, ordini) è isolata per utente

### Catalogo & Carrello
- Navigazione prodotti per categoria (Snippet, Progetti, Database)
- Gestione carrello con calcolo totali lato server
- Flag `disponibile` per nascondere prodotti senza eliminarli

### Gestione Ordini
- Creazione ordine dal carrello con persistenza in `Ordine` + `ItemQuantity`
- Storico ordini per utente
- Stima data di consegna (`LocalDate consegna`)

### Prodotti Digitali
- Campo `linkDownload` sull'entità `Prodotto` per associare il file acquistabile
- Dopo l'acquisto, l'utente riceve via email il link di download diretto
- Pulsante "Scarica" nell'area ordini del profilo utente

### Notifiche Email
Implementate tramite **pattern Observer** (`EmailNotificationObserver`):

| Evento | Email inviata |
| :--- | :--- |
| Registrazione | Benvenuto in CodeShop con riepilogo account |
| Cambio password | Avviso di modifica credenziali |
| Completamento ordine | Conferma acquisto + link di download per ogni prodotto digitale |

### Integrazione Pagamenti (PayPal)
È stato implementato un sistema di checkout sicuro tramite **PayPal REST API**:
- **Checkout Dinamico**: Il backend genera un `approvalUrl` di PayPal in base al totale del carrello.
- **Flusso di Approvazione**: L'utente viene reindirizzato su PayPal per confermare il pagamento e poi riportato sull'applicazione (`success` o `cancel`).
- **Verifica Lato Server**: Al ritorno dall'approvazione, il backend esegue il pagamento tramite il `paymentId` e il `PayerID` per garantirne la validità prima di confermare l'ordine.

| Endpoint | Metodo | Descrizione |
| :--- | :--- | :--- |
| `/payment/create` | `POST` | Inizia il processo di pagamento e restituisce l'URL di approvazione |
| `/payment/success` | `GET` | Endpoint di callback per pagamenti completati con successo |
| `/payment/cancel` | `GET` | Endpoint di callback per pagamenti annullati |

### Interfaccia
- Single Page Application con navigazione dinamica
- Footer con 4 colonne: logo, navigazione, categorie, newsletter
- Layout responsivo con effetti glassomorphism

---

## Architettura del Database

### Entità

| Entità | Attributi Chiave | Note |
| :--- | :--- | :--- |
| **Utente** | `username`, `password`, `email`, `ruolo` | Proprietario di ordini e carrello; autenticato via JWT |
| **Prodotto** | `nome`, `descrizione`, `prezzo`, `stock`, `disponibile`, `linkDownload` | `linkDownload` contiene l'URL del file digitale |
| **Categoria** | `nome`, `descrizione` | `@ManyToMany` con Prodotto |
| **Ordine** | `dataOrdine`, `consegna`, `indirizzo` | Appartiene a un Utente; contiene più `ItemQuantity` |
| **ItemQuantity** | `quantita`, `prezzoUnitario` | Pivot tra `Ordine` e `Prodotto`; preserva il prezzo storico |
| **Carrello** | `sessioneId`, `totale` | Stato temporaneo pre-checkout |

### Relazioni JPA
- `Utente` → `Ordine`: `@OneToMany` / `@ManyToOne`
- `Ordine` → `ItemQuantity` → `Prodotto`: composizione con prezzo salvato al momento dell'acquisto
- `Prodotto` ↔ `Categoria`: `@ManyToMany`

---

## Sicurezza

Spring Security con filtro JWT garantisce che ogni utente acceda esclusivamente ai propri dati (ordini, carrello, profilo). I token scadono dopo 24 ore.

---

## Struttura del Progetto

```
ProgettoFinale/
├── README.md
├── OBIETTIVI.md
└── final_project/                          # Modulo Maven principale
    ├── pom.xml
    ├── mvnw / mvnw.cmd
    └── src/
        ├── main/
        │   ├── java/com/example/final_project/
        │   │   ├── FinalProjectApplication.java
        │   │   ├── Config/                 # Configurazioni (PayPal, ecc.)
        │   │   │   └── PayPalConfig.java
        │   │   ├── Controller/             # REST Controllers
        │   │   │   ├── AuthController.java
        │   │   │   ├── CarrelloController.java
        │   │   │   ├── CategoriaController.java
        │   │   │   ├── ItemController.java
        │   │   │   ├── OrdineController.java
        │   │   │   ├── PaymentController.java      # Gestione redirect e API PayPal
        │   │   │   ├── ProdottoController.java
        │   │   │   └── UtenteController.java
        │   │   ├── Model/                  # Entità JPA
        │   │   │   ├── BaseEntity.java
        │   │   │   ├── Carrello.java
        │   │   │   ├── Categoria.java
        │   │   │   ├── ItemQuantity.java
        │   │   │   ├── Ordine.java
        │   │   │   ├── Prodotto.java
        │   │   │   ├── Ruolo.java
        │   │   │   └── Utente.java
        │   │   ├── Repository/             # Spring Data JPA Repositories
        │   │   │   ├── CarrelloRepository.java
        │   │   │   ├── CategoriaRepository.java
        │   │   │   ├── ItemRepository.java
        │   │   │   ├── OrdineRepository.java
        │   │   │   ├── ProdottoRepository.java
        │   │   │   └── UtenteRepository.java
        │   │   ├── Service/                # Business Logic
        │   │   │   ├── CarrelloService.java
        │   │   │   ├── CategoriaService.java
        │   │   │   ├── ItemService.java
        │   │   │   ├── OrdineService.java
        │   │   │   ├── PayPalService.java          # Logica di creazione ed esecuzione pagamenti
        │   │   │   ├── ProdottoService.java
        │   │   │   └── UtenteService.java
        │   │   ├── Security/               # JWT & Spring Security
        │   │   │   ├── JwtAuthenticationFilter.java
        │   │   │   ├── JwtService.java
        │   │   │   └── SecurityConfig.java
        │   │   ├── Observer/               # Pattern Observer (notifiche email)
        │   │   │   ├── EmailNotificationObserver.java
        │   │   │   ├── OrderObserver.java
        │   │   │   ├── OrderSubject.java
        │   │   │   ├── RegistrationObserver.java
        │   │   │   ├── RegistrationSubject.java
        │   │   │   ├── UserObserver.java
        │   │   │   └── UserSubject.java
        │   │   └── Exception/              # Eccezioni custom
        │   │       ├── CreazioneUtenteMalformataException.java
        │   │       └── UtenteNonEsistenteException.java
        │   └── resources/
        │       ├── application.yaml
        │       ├── seed.sql
        │       ├── seed_mysql.sql
        │       └── static/                 # Frontend (SPA)
        │           ├── index.html
        │           ├── css/
        │           │   └── style.css
        │           ├── js/
        │           │   └── auth.js
        │           └── img/
        │               └── hero.gif
        └── test/
            └── java/com/example/final_project/
                └── FinalProjectApplicationTests.java
```

---

## Configurazione PayPal

Per far funzionare l'integrazione, è necessario configurare le credenziali sandbox/live nel file `application.yaml`:

```yaml
paypal:
  client-id: ${PAYPAL_CLIENT_ID}
  client-secret: ${PAYPAL_CLIENT_SECRET}
  mode: sandbox # o 'live'
  currency: EUR
  return-url: http://localhost:8080/payment/success
  cancel-url: http://localhost:8080/payment/cancel
```

