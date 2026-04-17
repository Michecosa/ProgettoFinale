package com.example.final_project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project.Model.Carrello;
import com.example.final_project.Model.ItemQuantity;
import com.example.final_project.Repository.CarrelloRepository;
import com.example.final_project.Repository.ProdottoRepository;
import com.example.final_project.Repository.UtenteRepository;

// Service per la gestione del carrello, contiene la logica di business per aggiungere, rimuovere e visualizzare i prodotti nel carrello
@Service
public class CarrelloService {

    @Autowired
    CarrelloRepository carrelloRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    ProdottoRepository prodottoRepository;

    // Aggiunge un prodotto al carrello dell'utente, se il prodotto è già presente
    // aggiorna la quantità, altrimenti lo aggiunge come nuovo item
    public Carrello aggiungiProdotto(String username, Long idProdotto, int qtn) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null) {
            carrello = new Carrello();
            carrello.setUtente(utenteRepository.findByUsername(username));
        }

        ItemQuantity item = carrello.productAlreadyPresent(prodottoRepository.findById(idProdotto).orElseThrow());
        if (item != null) {
            item.setQtn(item.getQtn() + qtn);
        } else {
            item = new ItemQuantity();
            item.setProdotto(prodottoRepository.findById(idProdotto).orElseThrow());
            item.setQtn(qtn);
            item.setCarrello(carrello);
            carrello.getItems().add(item);
        }
        return carrelloRepository.save(carrello);
    }

    // Rimuove un prodotto dal carrello dell'utente, se presente
    public Carrello rimuoviProdotto(String username, Long idProdotto) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null)
            throw new RuntimeException("Carrello non trovato");
        ItemQuantity item = carrello.productAlreadyPresent(prodottoRepository.findById(idProdotto).orElseThrow());
        if (item != null) {
            carrello.getItems().remove(item);
            return carrelloRepository.save(carrello);
        }
        return carrello;
    }

    // Restituisce la lista degli item presenti nel carrello dell'utente,
    // identificato dallo username
    public List<ItemQuantity> getItemsByCarrelloId(Long idCarrello) {
        Carrello carrello = carrelloRepository.findById(idCarrello).orElseThrow();
        return carrello.getItems();
    }

    // Svuota completamente il carrello dell'utente, rimuovendo tutti gli item
    // presenti
    public Carrello svuotaCarrello(String username) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null)
            throw new RuntimeException("Carrello non trovato");
        carrello.getItems().clear();
        return carrelloRepository.save(carrello);
    }

    // Restituisce il carrello dell'utente identificato dallo username, se esiste
    public Carrello getCarrelloByUsername(String username) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null)
            throw new RuntimeException("Carrello non trovato");
        return carrello;
    }

    // Salva un nuovo carrello nel database, utilizzato principalmente per creare un
    // carrello vuoto per un nuovo utente
    public Carrello salvaCarrello() {
        Carrello carrello = new Carrello();
        return carrelloRepository.save(carrello);
    }

}
