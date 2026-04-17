package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.final_project.Model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
