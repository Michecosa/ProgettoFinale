package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.Categoria;

// Repository per la gestione delle categorie, estende JpaRepository per fornire operazioni CRUD
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
