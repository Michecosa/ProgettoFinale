package com.example.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
 * Fix applicati:
 * - aggiunto campo "stock" (quantità disponibile in magazzino)
 * - aggiunto campo "disponibile" (flag per nascondere il prodotto senza eliminarlo)
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prodotto extends BaseEntity {

    private String nome;

    private double prezzo;

    /** Quantità disponibile in magazzino */
    @Column(nullable = false)
    private int stock = 0;

    /**
     * Se false il prodotto non è acquistabile (fuori catalogo, esaurito, ecc.)
     * senza dover eliminare il record (che è referenziato dagli ordini storici)
     */
    @Column(nullable = false)
    private boolean disponibile = true;

    /** URL del file zip scaricabile dopo l'acquisto */
    private String linkDownload;

    @ManyToMany
    @JoinTable(
        name = "prodotto_to_categoria",
        joinColumns = @JoinColumn(name = "id_prodotto"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> categorie = new ArrayList<>();
}