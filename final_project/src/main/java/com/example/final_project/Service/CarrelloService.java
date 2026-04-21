package com.example.final_project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project.Model.Carrello;
import com.example.final_project.Model.ItemQuantity;
import com.example.final_project.Model.Prodotto;
import com.example.final_project.Repository.CarrelloRepository;
import com.example.final_project.Repository.ProdottoRepository;
import com.example.final_project.Repository.UtenteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CarrelloService {

    @Autowired
    CarrelloRepository carrelloRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    ProdottoRepository prodottoRepository;

    // Aggiunge un prodotto al carrello dell'utente.
    // Se il prodotto è già presente aggiorna la quantità, altrimenti lo aggiunge
    // come nuovo item.
    public Carrello aggiungiProdotto(String username, Long idProdotto, int qtn) {
        // FIX: validazione quantità (Allow negative for decrement, but handle 0
        // separately)
        if (qtn == 0) {
            throw new IllegalArgumentException("La quantità non può essere zero");
        }

        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null) {
            carrello = new Carrello();
            carrello.setUtente(utenteRepository.findByUsername(username));
        }

        // FIX: singola query invece di due chiamate duplicate a findById
        Prodotto prodotto = prodottoRepository.findById(idProdotto)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con id: " + idProdotto));

        ItemQuantity item = carrello.productAlreadyPresent(prodotto);
        if (item != null) {
            int newQty = item.getQtn() + qtn;
            if (newQty <= 0) {
                carrello.getItems().remove(item);
            } else {
                item.setQtn(newQty);
            }
        } else if (qtn > 0) {
            item = new ItemQuantity();
            item.setProdotto(prodotto);
            item.setQtn(qtn);
            item.setCarrello(carrello);
            carrello.getItems().add(item);
        }
        return carrelloRepository.save(carrello);
    }

    // Rimuove un prodotto dal carrello dell'utente, se presente.
    public Carrello rimuoviProdotto(String username, Long idProdotto) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null) {
            throw new EntityNotFoundException("Carrello non trovato per l'utente: " + username);
        }

        Prodotto prodotto = prodottoRepository.findById(idProdotto)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con id: " + idProdotto));

        ItemQuantity item = carrello.productAlreadyPresent(prodotto);
        if (item != null) {
            carrello.getItems().remove(item);
            return carrelloRepository.save(carrello);
        }
        return carrello;
    }

    // Restituisce la lista degli item presenti nel carrello dell'utente.
    public List<ItemQuantity> getItemsByUsername(String username) {
        return getCarrelloByUsername(username).getItems();
    }

    // Svuota completamente il carrello dell'utente, rimuovendo tutti gli item
    // presenti.
    public Carrello svuotaCarrello(String username) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null) {
            throw new EntityNotFoundException("Carrello non trovato per l'utente: " + username);
        }
        carrello.getItems().clear();
        return carrelloRepository.save(carrello);
    }

    // Restituisce il carrello dell'utente identificato dallo username, se esiste.
    public Carrello getCarrelloByUsername(String username) {
        Carrello carrello = carrelloRepository.findByUtente_Username(username);
        if (carrello == null) {
            carrello = new Carrello();
            carrello.setUtente(utenteRepository.findByUsername(username));
            carrello = carrelloRepository.save(carrello);
        }
        return carrello;
    }

    // evitando di salvare un carrello orfano senza utente.
    public Carrello salvaCarrello(String username) {
        Carrello carrello = new Carrello();
        carrello.setUtente(utenteRepository.findByUsername(username));
        return carrelloRepository.save(carrello);
    }

}