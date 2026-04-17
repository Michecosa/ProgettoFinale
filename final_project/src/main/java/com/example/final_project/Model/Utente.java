package com.example.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
 * Fix applicato:
 * - il campo CSV "roles" è stato sostituito con una relazione @OneToMany
 *   verso la tabella utente_ruolo, rispettando la prima forma normale (1NF).
 *   Per recuperare i ruoli come stringhe usare getRuoli().stream()
 *   .map(Ruolo::getNome).toList()
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String mail;

    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String password;

    /** Sostituisce il vecchio campo String roles (CSV). */
    @OneToMany(mappedBy = "utente", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Setter(AccessLevel.NONE)
    private List<Ruolo> ruoli = new ArrayList<>();

    public List<Ruolo> getRuoli() {
        if (ruoli == null) {
            ruoli = new ArrayList<>();
        }
        return ruoli;
    }

    @OneToOne(mappedBy = "utente")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Carrello carrello;

    @OneToMany(mappedBy = "utente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Setter(AccessLevel.NONE)
    private List<Ordine> ordini = new ArrayList<>();

    public List<Ordine> getOrdini() {
        if (ordini == null) {
            ordini = new ArrayList<>();
        }
        return ordini;
    }

    /** Utility: verifica se l'utente ha un determinato ruolo. */
    public boolean hasRuolo(String nomeRuolo) {
        return getRuoli().stream().anyMatch(r -> r.getNome().equals(nomeRuolo));
    }
}