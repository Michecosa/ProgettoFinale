package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.Carrello;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    // Metodo che restituisce un carrello prendendo il ingresso lo username
    // dell'utente a cui appartiene
    Carrello findByUtente_Username(String username);
}
