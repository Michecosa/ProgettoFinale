package com.example.final_project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utente extends BaseEntity {

	@Column(unique = true)
	private String username;
	@Column(unique = true)
	private String mail;
	private String password;
	private String roles;// csv "ROLE_ADMIN,ROLE_USER"

	@OneToOne(mappedBy = "utente")
	@com.fasterxml.jackson.annotation.JsonIgnore
	private Carrello carrello;

	@OneToMany(mappedBy = "utente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@com.fasterxml.jackson.annotation.JsonIgnore
	private List<Ordine> ordini = new ArrayList<>();
}
