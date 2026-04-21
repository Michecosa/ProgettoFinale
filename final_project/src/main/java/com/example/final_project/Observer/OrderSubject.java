package com.example.final_project.Observer;

import com.example.final_project.Model.Ordine;

/**
 * Interfaccia Subject per il pattern Observer.
 * Gestisce l'aggiunta, rimozione e notifica degli osservatori.
 */
public interface OrderSubject {
    void addObserver(OrderObserver observer);
    void removeObserver(OrderObserver observer);
    void notifyObservers(Ordine ordine);
}
