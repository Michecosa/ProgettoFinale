package com.example.final_project.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.Prodotto;
import com.example.final_project.Service.ProdottoService;

import lombok.RequiredArgsConstructor;

// Controller per la gestione dei prodotti, con endpoint per operazioni CRUD
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    // Endpoint per ottenere tutti i prodotti
    @GetMapping
    public List<Prodotto> getAll() {
        return prodottoService.trovaTutti();
    }

    // Endpoint per ottenere un prodotto specifico tramite ID
    @GetMapping("/{id}")
    public Prodotto getById(@PathVariable Long id) {
        return prodottoService.trovaPerID(id);
    }

    // Endpoint per creare un nuovo prodotto
    @PostMapping
    public Prodotto create(@RequestBody Prodotto prodotto) {
        return prodottoService.crea(prodotto);
    }

    // Endpoint per aggiornare un prodotto esistente tramite ID
    @PutMapping("/{id}")
    public Prodotto update(@PathVariable Long id, @RequestBody Prodotto prodotto) {
        return prodottoService.aggiorna(id, prodotto);
    }

    // Endpoint per eliminare un prodotto tramite ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        prodottoService.elimina(id);
    }
}
