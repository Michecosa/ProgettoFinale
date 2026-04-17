package com.example.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria extends BaseEntity {
	private String nome;

	@ManyToMany(mappedBy = "categorie")
	@com.fasterxml.jackson.annotation.JsonIgnore
	private List<Prodotto> prodotti = new ArrayList<>();

}
