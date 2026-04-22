# Obiettivi Giornalieri - 21 Aprile 2026

## 0. Interfaccia & Layout 🎨
- [x] **Aggiungere un footer**: Implementare un footer coerente in tutte le pagine dell'applicazione per migliorare la navigazione e l'estetica.

## 2. Sistema di notifiche 🔔
- [x] **Notifica di aggiornamento password**: Se la password viene cambiata, l'utente viene avvisato tramite email
- [x] **Notifica di conferma acquisto**: Al completamento di un acquisto, l'utente viene avvisato del corretto completamento
- [x] **Notifica di Benvenuto**: In caso di registrazione, l'utente ricererà un email di benvenuto da CodeShop

## 3. Gestione Prodotti Digitali 📦
- [x] **Attributo link sul prodotto**: Aggiunto il campo `downloadLink` (o equivalente) sull'entità `Prodotto` per associare un URL/percorso al file digitale
- [x] **Invio file via email come ZIP**: Al completamento dell'ordine, l'utente riceve via email il prodotto digitale allegato come file `.zip`
- [x] **Bottone "Scarica"**: Nell'area ordini/profilo è presente un pulsante che permette all'utente di scaricare direttamente il file digitale acquistato

<br>
<br>

# Obiettivi Giornalieri - 22 Aprile 2026

## 4. Sistema di Pagamento & Checkout 💳
- [x] **Integrazione PayPal (Sandbox)**: Implementato `PayPalService` per la comunicazione con le REST API di PayPal e `PaymentController` per gestire il flusso di approvazione.
- [x] **Checkout Flow**: Creato il modal di checkout nel frontend che reindirizza l'utente a PayPal e gestisce il ritorno sicuro.
- [x] **Creazione Ordine Post-Pagamento**: L'ordine viene ora creato nel database solo dopo la conferma di pagamento avvenuto, garantendo la coerenza dei dati.


## 5. Esperienza Utente (UX) 🚀
- [x] **Feedback Real-time**: Implementati toast di notifica per segnalare il successo, l'annullamento o eventuali errori durante la transazione PayPal.
- [x] **Automazione Download**: L'integrazione tra il sistema di pagamento e l'Observer garantisce l'invio istantaneo dell'email con i link di download al termine dell'acquisto.
- [ ] **Visualizzazione Stato Pagato**: Mostrare chiaramente lo stato "Pagato" nell'area ordini dell'utente per migliorare la trasparenza.