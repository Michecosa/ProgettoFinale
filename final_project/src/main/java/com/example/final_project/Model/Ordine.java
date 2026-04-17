package com.example.final_project.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ordine extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "id_utente")
	@JsonIgnore
	private Utente utente;

	private boolean pagato;
	private LocalDate consegna;
	private String indirizzo;

	@OneToMany(mappedBy = "ordine", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<ItemQuantity> items = new ArrayList<>();

	public double getTotale() {
		return items.stream().mapToDouble(i -> i.getQtn() * i.getProdotto().getPrezzo()).sum();
	}
}
