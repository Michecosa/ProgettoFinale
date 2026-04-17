package com.example.final_project.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProdottoController {
    private final ProdottoService serv;

    @GetMapping
    public List<ProdottoDto> getAll() {
        return serv.leggiTuttiProdottiComeDto();
    }

}
