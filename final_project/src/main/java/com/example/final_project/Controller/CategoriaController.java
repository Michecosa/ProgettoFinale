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

import com.example.final_project.Model.Categoria;
import com.example.final_project.Service.CategoriaService;

import lombok.RequiredArgsConstructor;

// Controller per la gestione delle categorie, con endpoint per operazioni CRUD
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Endpoint per ottenere tutte le categorie
    @GetMapping
    public List<Categoria> getAll() {
        return categoriaService.trovaTutte();
    }

    // Endpoint per ottenere una categoria specifica tramite ID
    @GetMapping("/{id}")
    public Categoria getById(@PathVariable Long id) {
        return categoriaService.trovaPerID(id);
    }

    // Endpoint per creare una nuova categoria
    @PostMapping
    public Categoria create(@RequestBody Categoria categoria) {
        return categoriaService.crea(categoria);
    }

    // Endpoint per aggiornare una categoria esistente tramite ID
    @PutMapping("/{id}")
    public Categoria update(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.aggiorna(id, categoria);
    }

    // Endpoint per eliminare una categoria tramite ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoriaService.elimina(id);
    }
}
