package com.example.final_project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemQuantity extends BaseEntity {

	private int qtn;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_prodotto")
	private Prodotto prodotto;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_carrello")
	@JsonIgnore
	private Carrello carrello;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_ordine")
	private Ordine ordine;
}
