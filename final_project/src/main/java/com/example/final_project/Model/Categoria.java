package com.example.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria extends BaseEntity {
	private String nome;

	@ManyToMany(mappedBy = "categorie")
	private List<Prodotto> prodotti = new ArrayList<>();

}
