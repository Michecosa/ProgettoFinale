# Obiettivi Giornalieri - 21 Aprile 2026

## 0. Interfaccia & Layout 🎨
- [ ] **Aggiungere un footer**: Implementare un footer coerente in tutte le pagine dell'applicazione per migliorare la navigazione e l'estetica.


## 1. Gestione Magazzino (Stock) 📦
- [ ] **Scalamento Stock**: Implementare la logica in `OrdineService` che diminuisce il campo `stock` di ogni `Prodotto` acquistato
- [ ] **Controllo Disponibilità**: Impedire l'aggiunta al carrello (o il checkout) se la quantità richiesta supera lo `stock` disponibile


## 2. Sistema di Pagamento & Checkout 💳
- [ ] **Mock Payment Service**: Creare un `PagamentoService` che accetti finti dati di una carta di credito e restituisca un esito (successo/fallimento).
- [ ] **Integrazione Checkout**: Modificare `OrdineService.creaOrdine` per includere la chiamata al servizio di pagamento e settare `ordine.setPagato(true)` solo in caso di esito positivo.
- [ ] **DTO per il Checkout**: Creare un `CheckoutRequest` che contenga l'indirizzo di spedizione e i dati (finti) della carta, per non passare tutto come parametri sparsi.
<br>
<br>
# Obiettivi Giornalieri - 22 Aprile 2026

## 3. Esperienza Utente (UX) 🚀
- [ ] **Feedback Errori**: Se il pagamento fallisce, restituire un messaggio chiaro all'utente (es. "Fondi insufficienti" o "Carta scaduta") invece di un errore generico 500



## 4. Sistema di notufiche 🔔
- [ ] **Notifica di aggiornamento password**: Se la password viene cambiata, l'utente viene avvisato tramite email
- [ ] **Notifica di conferma acquisto**: Al completamento di un acquisto, l'utente viene avvisato del corretto completamento