package com.example.final_project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final_project.Model.Prodotto;
import com.example.final_project.Repository.ProdottoRepository;

// Service per la gestione dei prodotti, con metodi per operazioni CRUD e validazione dei dati
@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    public List<Prodotto> trovaTutti() {
        return prodottoRepository.findAll();
    }

    public Prodotto trovaPerID(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));
    }

    public Prodotto crea(Prodotto prodotto) {
        if (prodotto.getNome() == null || prodotto.getNome().isBlank()) {
            throw new IllegalArgumentException("Il nome del prodotto è obbligatorio");
        }
        return prodottoRepository.save(prodotto);
    }

    public Prodotto aggiorna(Long id, Prodotto datiAggiornati) {
        Prodotto esistente = trovaPerID(id);
        if (datiAggiornati.getNome() != null && !datiAggiornati.getNome().isBlank()) {
            esistente.setNome(datiAggiornati.getNome());
        }
        if (datiAggiornati.getPrezzo() >= 0) {
            esistente.setPrezzo(datiAggiornati.getPrezzo());
        }
        // Aggiornamento disponibilità
        esistente.setDisponibile(datiAggiornati.isDisponibile());

        if (datiAggiornati.getLinkDownload() != null) {
            esistente.setLinkDownload(datiAggiornati.getLinkDownload());
        }
        if (datiAggiornati.getCategorie() != null) {
            esistente.setCategorie(datiAggiornati.getCategorie());
        }
        return prodottoRepository.save(esistente);
    }

    public void elimina(Long id) {
        if (!prodottoRepository.existsById(id)) {
            throw new RuntimeException("Prodotto non trovato con id: " + id);
        }
        prodottoRepository.deleteById(id);
    }
}
