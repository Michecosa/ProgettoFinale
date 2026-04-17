package com.example.final_project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project.Model.ItemQuantity;
import com.example.final_project.Repository.ItemRepository;

// Service per la gestione degli item nel carrello, fornisce metodi per operazioni CRUD e altre logiche di business
@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    // Restituisce la lista di tutti gli item presenti nel database, utilizzato
    // principalmente per visualizzare il carrello
    public List<ItemQuantity> getAllItems() {
        return itemRepository.findAll();
    }

    // Restituisce un item specifico tramite il suo ID, se non trovato lancia
    // un'eccezione
    public ItemQuantity getItemById(Long idItem) {
        return itemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item non trovato con id: " + idItem));
    }

    // Crea un nuovo item, utilizzato principalmente per aggiungere un prodotto al
    // carrello, restituisce l'item creato con l'ID generato
    public ItemQuantity createItem(ItemQuantity item) {
        return itemRepository.save(item);
    }

    // Aggiorna la quantità di un item esistente tramite il suo ID, se non trovato
    // lancia un'eccezione, restituisce l'item aggiornato
    public ItemQuantity updateQtn(Long idItem, int qtn) {
        ItemQuantity item = itemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item non trovato con id: " + idItem));
        item.setQtn(qtn);
        return itemRepository.save(item);
    }

    // Elimina un item tramite il suo ID, se non trovato lancia un'eccezione
    public void deleteItem(Long idItem) {
        itemRepository.deleteById(idItem);
    }
}