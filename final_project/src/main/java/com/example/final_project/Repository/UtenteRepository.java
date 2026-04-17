package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.Utente;

// Repository per la gestione degli utenti, estende JpaRepository per fornire operazioni CRUD
@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

	// Metodo che restituisce un utente prendendo in ingresso username e password
	Utente findByUsernameAndPassword(String username, String password);

	// Metodo che restituisce un utente prendendo in ingresso solo lo username
	Utente findByUsername(String username);

	// exist invece che dare l'utente con quello username, da solo vero o falso
	boolean existsByUsername(String username);

}
