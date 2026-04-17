package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
	Utente findByUsernameAndPassword(String username, String password);

	Utente findByUsername(String username);

	// exist invece che dare l'utente con quello username, da solo vero o falso
	boolean existsByUsername(String username);

	// @Query("SELECT u FROM Utente u WHERE (u.username=:boh OR u.mail=:boh) and
	// u.password=:password ")
	// Utente trovaUtente(String boh, String password);
}
