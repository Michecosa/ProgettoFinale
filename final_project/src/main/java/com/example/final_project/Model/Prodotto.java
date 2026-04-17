package com.example.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prodotto extends BaseEntity {
	private String nome;
	private double prezzo;

	@ManyToMany
	@JoinTable(name = "prodotto_to_categoria", joinColumns = @JoinColumn(name = "id_prodotto"), inverseJoinColumns = @JoinColumn(name = "id_categoria"))
	private List<Categoria> categorie = new ArrayList<>();
}
