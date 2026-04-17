package com.example.final_project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project.Model.Categoria;
import com.example.final_project.Repository.CategoriaRepository;

// Service per la gestione delle categorie, fornisce metodi per operazioni CRUD e altre logiche di business
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Restituisce la lista di tutte le categorie presenti nel database
    public List<Categoria> trovaTutte() {
        return categoriaRepository.findAll();
    }

    // Restituisce una categoria specifica tramite il suo ID, se non trovata lancia
    // un'eccezione
    public Categoria trovaPerID(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata con id: " + id));
    }

    // Crea una nuova categoria, verificando che il nome non sia nullo o vuoto,
    // altrimenti
    public Categoria crea(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().isBlank()) {
            throw new IllegalArgumentException("Il nome della categoria è obbligatorio");
        }
        return categoriaRepository.save(categoria);
    }

    // Aggiorna una categoria esistente tramite il suo ID, se non trovata lancia
    // un'eccezione,
    public Categoria aggiorna(Long id, Categoria datiAggiornati) {
        Categoria esistente = trovaPerID(id);
        if (datiAggiornati.getNome() != null && !datiAggiornati.getNome().isBlank()) {
            esistente.setNome(datiAggiornati.getNome());
        }
        return categoriaRepository.save(esistente);
    }

    // Elimina una categoria tramite il suo ID, se non trovata lancia un'eccezione
    public void elimina(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria non trovata con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
