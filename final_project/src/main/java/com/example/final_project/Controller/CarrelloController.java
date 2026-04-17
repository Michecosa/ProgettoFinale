package com.example.final_project.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.Carrello;
import com.example.final_project.Service.CarrelloService;

import lombok.RequiredArgsConstructor;

// Controller per la gestione del carrello, con endpoint per visualizzare il proprio carrello
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CarrelloController {

    private final CarrelloService carrelloService;

    // Endpoint per ottenere il carrello dell'utente autenticato
    @GetMapping("/mine")
    public Carrello getMine(Authentication authentication) {
        return carrelloService.getCarrelloByUsername(authentication.getName());
    }

    // Endpoint per aggiungere un prodotto al carrello dell'utente autenticato
    @PostMapping("/add/{idProdotto}")
    public Carrello addProduct(@PathVariable Long idProdotto,
            @RequestParam(defaultValue = "1") int qtn,
            Authentication authentication) {
        return carrelloService.aggiungiProdotto(authentication.getName(), idProdotto, qtn);
    }

    // Endpoint per rimuovere un prodotto dal carrello dell'utente autenticato
    @DeleteMapping("/remove/{idProdotto}")
    public Carrello removeProduct(@PathVariable Long idProdotto, Authentication authentication) {
        return carrelloService.rimuoviProdotto(authentication.getName(), idProdotto);
    }

    // Endpoint per svuotare completamente il carrello dell'utente autenticato
    @DeleteMapping("/clear")
    public Carrello clearCart(Authentication authentication) {
        return carrelloService.svuotaCarrello(authentication.getName());
    }
}
