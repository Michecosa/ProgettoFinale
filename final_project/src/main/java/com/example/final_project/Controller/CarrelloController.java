package com.example.final_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.Carrello;
import com.example.final_project.Model.ItemQuantity;
import com.example.final_project.Model.Prodotto;
import com.example.final_project.Repository.CarrelloRepository;
import com.example.final_project.Repository.ItemRepository;
import com.example.final_project.Repository.ProdottoRepository;

@RestController
@RequestMapping("/api/carts")
public class CarrelloController {

    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/mine")
    public Carrello getMine(Authentication authentication) {
        return carrelloRepository.findByUtente_Username(authentication.getName());
    }

    @PostMapping("/add/{idProdotto}")
    public Carrello addProduct(@PathVariable Long idProdotto, Authentication authentication) {
        Carrello carrello = carrelloRepository.findByUtente_Username(authentication.getName());
        Prodotto prodotto = prodottoRepository.findById(idProdotto).orElseThrow();

        ItemQuantity item = carrello.productAlreadyPresent(prodotto);
        if (item != null) {
            item.setQtn(item.getQtn() + 1);
            itemRepository.save(item);
        } else {
            ItemQuantity newItem = new ItemQuantity();
            newItem.setProdotto(prodotto);
            newItem.setCarrello(carrello);
            newItem.setQtn(1);
            itemRepository.save(newItem);
        }

        return carrelloRepository.findByUtente_Username(authentication.getName());
    }
}
