package com.example.final_project.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.Utente;
import com.example.final_project.Service.UtenteService;

import lombok.RequiredArgsConstructor;

// Controller per la gestione degli utenti, con endpoint per visualizzare tutti gli utenti
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    // Endpoint per ottenere tutti gli utenti
    @GetMapping
    public List<Utente> getAll() {
        return utenteService.trovaTutti();
    }

    // Endpoint per ottenere un utente specifico tramite ID
    @GetMapping("/{id}")
    public Utente getById(@PathVariable Long id) {
        return utenteService.trovaPerID(id);
    }

    // Endpoint per aggiornare un utente esistente tramite ID
    @PutMapping("/{id}")
    public Utente update(@PathVariable Long id, @RequestBody Utente utente) {
        return utenteService.aggiorna(id, utente);
    }

    // Endpoint per eliminare un utente tramite ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        utenteService.elimina(id);
    }
}
