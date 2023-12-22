package com.vidhan_123.pizzaecommerce.controller;

import com.vidhan_123.pizzaecommerce.model.Pizza;
import com.vidhan_123.pizzaecommerce.model.PizzaAggregate;
import com.vidhan_123.pizzaecommerce.model.PizzaLookup;
import com.vidhan_123.pizzaecommerce.model.Product;
import com.vidhan_123.pizzaecommerce.service.PizzaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EcommerceController {
    @Autowired
    private PizzaTypeService pizzaTyeService;

    @GetMapping("/")
    public List<Pizza> products(){
        return pizzaTyeService.getAllProducts();
    }

    @GetMapping("/pizza")
    public List<PizzaAggregate> getSomeproducts(){
        return pizzaTyeService.getPizzas();
    }

    @GetMapping("/lookup")
    public List<PizzaLookup> getPizzaLookup(){
        return pizzaTyeService.getPizzaLookup();
    }

    @GetMapping("/dominos")
    public List<Product> getAllDominosPizza(){
        return pizzaTyeService.getAllDominos();
    }

    @PostMapping("/pizza")
    public Product addPizza(@RequestBody Product pizza){
        return pizzaTyeService.addPizza(pizza);
    }

    @GetMapping("/load")
    public String load1MData(){
        pizzaTyeService.loadData();
        return "1M records loaded";
    }
}
