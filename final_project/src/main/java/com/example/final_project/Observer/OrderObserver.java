package com.example.final_project.Observer;

import com.example.final_project.Model.Ordine;

/**
 * Interfaccia per il pattern Observer.
 * Gli oggetti che vogliono ricevere notifiche sugli ordini devono implementare questa interfaccia.
 */
public interface OrderObserver {
    void update(Ordine ordine);
}
