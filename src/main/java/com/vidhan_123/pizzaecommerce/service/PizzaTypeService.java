package com.vidhan_123.pizzaecommerce.service;

import com.mongodb.client.model.Field;
import com.vidhan_123.pizzaecommerce.dao.ProductRepository;
import com.vidhan_123.pizzaecommerce.model.Pizza;
import com.vidhan_123.pizzaecommerce.model.PizzaAggregate;
import com.vidhan_123.pizzaecommerce.model.PizzaLookup;
import com.vidhan_123.pizzaecommerce.model.Product;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.ascending;

@Service
public class PizzaTypeService {

    @Autowired
    private ProductRepository productRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Pizza> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public List<PizzaAggregate> getPizzas() {
        return productRepository.getPizzas();
    }

    public List<PizzaLookup> getPizzaLookup() {
        Bson pipeline = lookup("type", "type", "_id", "type");
        Bson addFields = addFields(new Field<Integer>("quantity", 5));
        Bson in = in("brand", Arrays.asList("Dominos", "Pizza Hut"));
        List<Bson> query = Arrays.asList(pipeline, addFields, match(in), sort(ascending("brand")), limit(3));
        return productRepository.getPizzaLookup(query);
    }

    public List<Product> getAllDominos() {
        logger.info("All pizza from Dominos");
        return productRepository.getAllDominos();
    }

    public Product addPizza(Product pizza) {
        logger.info("Adding pizza into DB");
        return productRepository.addPizza(pizza);
    }

    public void loadData() {
        productRepository.loadData();
    }
}
