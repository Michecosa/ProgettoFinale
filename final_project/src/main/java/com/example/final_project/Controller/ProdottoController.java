package com.example.final_project.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.Prodotto;
import com.example.final_project.Repository.ProdottoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProdottoController {
    private final ProdottoRepository prodottoRepository;

    @GetMapping
    public List<Prodotto> getAll() {
        return prodottoRepository.findAll();
    }
}
