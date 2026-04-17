package com.example.final_project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.Ordine;

// Repository per la gestione degli ordini, estende JpaRepository per fornire operazioni CRUD
@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByUtente_Username(String username);
}
