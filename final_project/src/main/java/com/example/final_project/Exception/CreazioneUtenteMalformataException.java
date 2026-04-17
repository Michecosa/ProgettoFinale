package com.example.final_project.Exception;

// Eccezione personalizzata per segnalare errori durante la creazione di un utente con dati malformati
public class CreazioneUtenteMalformataException extends RuntimeException {
    public CreazioneUtenteMalformataException(String message) {
        super(message);
    }
}
