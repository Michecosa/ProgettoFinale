package com.example.final_project.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.final_project.Model.Carrello;
import com.example.final_project.Model.ItemQuantity;
import com.example.final_project.Model.Ordine;
import com.example.final_project.Model.Utente;
import com.example.final_project.Repository.CarrelloRepository;
import com.example.final_project.Repository.OrdineRepository;
import com.example.final_project.Repository.UtenteRepository;
import com.example.final_project.Observer.OrderObserver;
import com.example.final_project.Observer.OrderSubject;

// Service per la gestione degli ordini, contiene la logica di business per creare, aggiornare, visualizzare e eliminare gli ordini
@Service
public class OrdineService implements OrderSubject {

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CarrelloService carrelloService;

    // Lista degli osservatori (automaticamente iniettati da Spring se implementano
    // OrderObserver)
    @Autowired
    private List<OrderObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Ordine ordine) {
        for (OrderObserver observer : observers) {
            observer.update(ordine);
        }
    }

    // Restituisce la lista di tutti gli ordini presenti nel database, utilizzato
    // principalmente per visualizzare gli ordini effettuati dagli utenti
    public List<Ordine> trovaTutti() {
        return ordineRepository.findAll();
    }

    // Restituisce la lista di tutti gli ordini effettuati da un utente specifico
    // tramite il suo username, se l'utente non esiste lancia un'eccezione
    public List<Ordine> trovaTuttiPerUtente(String username) {
        return ordineRepository.findByUtente_Username(username);
    }

    // Restituisce un ordine specifico tramite il suo ID, se non trovato lancia
    // un'eccezione
    public Ordine trovaPerID(Long id) {
        return ordineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato con id: " + id));
    }

    // Crea un nuovo ordine per un utente specifico, utilizzando i prodotti presenti
    // nel carrello, se l'utente o il carrello non esistono o
    // se il carrello è vuoto lancia un'eccezione, restituisce l'ordine creato con
    // l'ID generato
    @Transactional
    public Ordine creaOrdine(String username, String indirizzo) {
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null)
            throw new RuntimeException("Utente non trovato: " + username);

        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null || carrello.getItems().isEmpty()) {
            throw new RuntimeException("Il carrello è vuoto");
        }

        Ordine ordine = new Ordine();
        ordine.setUtente(utente);
        ordine.setIndirizzo(indirizzo);
        ordine.setConsegna(LocalDate.now());
        ordine.setData(LocalDate.now());
        ordine.setPagato(false);

        List<ItemQuantity> items = new ArrayList<>();
        for (ItemQuantity cartItem : carrello.getItems()) {
            ItemQuantity orderItem = new ItemQuantity();
            orderItem.setProdotto(cartItem.getProdotto());
            orderItem.setQtn(cartItem.getQtn());
            orderItem.setOrdine(ordine);
            items.add(orderItem);
        }
        ordine.setItems(items);

        Ordine saved = ordineRepository.save(ordine);
        carrelloService.svuotaCarrello(username);
        notifyObservers(saved);
        return saved;
    }

    // Aggiorna un ordine esistente tramite il suo ID, se non trovato lancia
    // un'eccezione,
    // restituisce l'ordine aggiornato
    public Ordine aggiorna(Long id, Ordine datiAggiornati) {
        Ordine esistente = trovaPerID(id);
        if (datiAggiornati.getIndirizzo() != null && !datiAggiornati.getIndirizzo().isBlank()) {
            esistente.setIndirizzo(datiAggiornati.getIndirizzo());
        }
        esistente.setPagato(datiAggiornati.isPagato());
        return ordineRepository.save(esistente);
    }

    // Elimina un ordine tramite il suo ID, se non trovato lancia un'eccezione
    public void elimina(Long id) {
        if (!ordineRepository.existsById(id)) {
            throw new RuntimeException("Ordine non trovato con id: " + id);
        }
        ordineRepository.deleteById(id);
    }
}
