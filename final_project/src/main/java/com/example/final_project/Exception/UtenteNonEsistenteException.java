package com.example.final_project.Exception;

// Eccezione personalizzata per segnalare errori quando si tenta di accedere a un utente che non esiste
public class UtenteNonEsistenteException extends RuntimeException {
    public UtenteNonEsistenteException(String message) {
        super(message);
    }
}
