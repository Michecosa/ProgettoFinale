package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.final_project.Model.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
}
