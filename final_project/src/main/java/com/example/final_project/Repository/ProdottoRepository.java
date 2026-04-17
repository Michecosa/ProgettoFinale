package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.Prodotto;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
}
