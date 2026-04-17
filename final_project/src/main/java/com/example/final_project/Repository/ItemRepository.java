package com.example.final_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.final_project.Model.ItemQuantity;

// Repository per la gestione degli item nei carrelli e negli ordini, estende JpaRepository per fornire operazioni CRUD
@Repository
public interface ItemRepository extends JpaRepository<ItemQuantity, Long> {
}
