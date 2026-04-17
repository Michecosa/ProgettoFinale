package com.example.final_project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.ItemQuantity;
import com.example.final_project.Service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    // prendiamo tutti gli item e le loro quantità
    @GetMapping
    public List<ItemQuantity> getAllItems() {
        return itemService.getAllItems();
    }

    // prendiamo un item e la sua quantità tramite id
    @GetMapping("/{id}")
    public ItemQuantity getItemById(Long id) {
        return itemService.getItemById(id);
    }

    // Crea nuovo item
    @PostMapping
    public ItemQuantity createItem(@RequestBody ItemQuantity itemQuantity) {
        return itemService.createItem(itemQuantity);
    }

    // aggiorna la quantità di un item
    @PostMapping("/quantita")
    public ItemQuantity updateItemQuantity(@RequestBody ItemQuantity itemQuantity) {
        return itemService.updateQtn(itemQuantity.getId(), itemQuantity.getQtn());
    }

    // elimina un item tramite id
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

}
