package com.example.final_project.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// Entità che rappresenta il carrello di un utente
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carrello extends BaseEntity {
	@OneToOne
	@JoinColumn(name = "id_utente")
	private Utente utente;

	// Relazione OneToMany con ItemQuantity, con cascade per propagare le operazioni
	// e fetch EAGER per caricare gli item insieme al carrello
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "carrello")
	private List<ItemQuantity> items = new ArrayList<>();

	public double getTotale() {
		return items.stream().mapToDouble(i -> i.getQtn() * i.getProdotto().getPrezzo()).sum();
	}

	// Metodo per verificare se un prodotto è già presente nel carrello e restituire
	// l'item corrispondente
	public ItemQuantity productAlreadyPresent(Prodotto p) {

		for (ItemQuantity i : items)
			if (i.getProdotto().equals(p))
				return i;

		return null;

	}
}
