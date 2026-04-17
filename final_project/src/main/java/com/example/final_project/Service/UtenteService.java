package com.example.final_project.Service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.final_project.Exception.CreazioneUtenteMalformataException;
import com.example.final_project.Exception.UtenteNonEsistenteException;
import com.example.final_project.Model.Utente;
import com.example.final_project.Repository.UtenteRepository;

// Service per la gestione degli utenti, contiene la logica di business per creare, aggiornare, visualizzare e eliminare gli utenti, implementa UserDetailsService per l'integrazione con Spring Security
@Service
public class UtenteService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private PasswordEncoder passwordEncoder;

    // Implementazione del metodo loadUserByUsername per caricare i dettagli
    // dell'utente durante l'autenticazione, se l'utente non esiste lancia
    // un'eccezione, altrimenti restituisce un oggetto User con username, password e
    // ruoli dell'utente
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }

        List<SimpleGrantedAuthority> authorities = Arrays.stream(utente.getRoles().split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(utente.getUsername(), utente.getPassword(), authorities);
    }

    // Restituisce la lista di tutti gli utenti presenti nel database, utilizzato
    public List<Utente> trovaTutti() {
        return utenteRepository.findAll();
    }

    // Restituisce un utente specifico tramite il suo username, se non trovato
    // lancia
    public Utente trovaPerUsername(String username) {
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            throw new UtenteNonEsistenteException("Nessun utente con username: " + username);
        }
        return utente;
    }

    // Restituisce un utente specifico tramite il suo ID, se non trovato lancia
    // un'eccezione, utilizzato principalmente per operazioni di aggiornamento o
    // eliminazione
    public Utente trovaPerID(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new UtenteNonEsistenteException("Nessun utente con id: " + id));
    }

    // Crea un nuovo utente, verificando che username, mail e password non siano
    public Utente crea(Utente utente) {
        if (utente.getUsername() == null || utente.getUsername().isBlank()
                || utente.getMail() == null || utente.getMail().isBlank()
                || utente.getPassword() == null || utente.getPassword().isBlank()) {
            throw new CreazioneUtenteMalformataException("Username, mail e password sono obbligatori");
        }
        if (utenteRepository.existsByUsername(utente.getUsername())) {
            throw new CreazioneUtenteMalformataException("Username già in uso: " + utente.getUsername());
        }
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        if (utente.getRoles() == null || utente.getRoles().isBlank()) {
            utente.setRoles("ROLE_USER");
        }
        return utenteRepository.save(utente);
    }

    // Aggiorna un utente esistente tramite il suo ID, se non trovato lancia
    // un'eccezione, restituisce l'utente aggiornato
    public Utente aggiorna(Long id, Utente datiAggiornati) {
        Utente esistente = trovaPerID(id);
        if (datiAggiornati.getMail() != null && !datiAggiornati.getMail().isBlank()) {
            esistente.setMail(datiAggiornati.getMail());
        }
        if (datiAggiornati.getPassword() != null && !datiAggiornati.getPassword().isBlank()) {
            esistente.setPassword(passwordEncoder.encode(datiAggiornati.getPassword()));
        }
        return utenteRepository.save(esistente);
    }

    // Elimina un utente tramite il suo ID, se non trovato lancia un'eccezione
    public void elimina(Long id) {
        if (!utenteRepository.existsById(id)) {
            throw new UtenteNonEsistenteException("Nessun utente con id: " + id);
        }
        utenteRepository.deleteById(id);
    }
}
