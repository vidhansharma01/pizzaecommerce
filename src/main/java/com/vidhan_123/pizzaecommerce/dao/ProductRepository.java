package com.vidhan_123.pizzaecommerce.dao;

import com.vidhan_123.pizzaecommerce.model.Pizza;
import com.vidhan_123.pizzaecommerce.model.PizzaAggregate;
import com.vidhan_123.pizzaecommerce.model.PizzaLookup;
import com.vidhan_123.pizzaecommerce.model.Product;
import org.bson.conversions.Bson;

import java.util.List;

public interface ProductRepository {
    public List<Pizza> getAllProducts();

    public List<PizzaAggregate> getPizzas();

    public List<PizzaLookup> getPizzaLookup(List<Bson> query);

    public Product addPizza(Product pizza);

    public List<Product> getAllDominos();

    public void loadData();
}
