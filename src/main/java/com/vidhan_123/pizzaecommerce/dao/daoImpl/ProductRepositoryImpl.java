package com.vidhan_123.pizzaecommerce.dao.daoImpl;

import com.google.gson.Gson;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.vidhan_123.pizzaecommerce.dao.ProductRepository;
import com.vidhan_123.pizzaecommerce.model.Pizza;
import com.vidhan_123.pizzaecommerce.model.PizzaAggregate;
import com.vidhan_123.pizzaecommerce.model.PizzaLookup;
import com.vidhan_123.pizzaecommerce.model.Product;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MongoConverter mongoConverter;

    @Value("${spring.data.mongodb.database}")
    private String dbName;


    @Override
    public List<Pizza> getAllProducts() {
        List<Pizza> products = new ArrayList<>();
        MongoCollection<Document> collection = mongoClient.getDatabase("ecommerce").getCollection("products");
        Document search = new Document("price", new Document("$gt", 24));
        FindIterable<Document> agg = collection.find(search);
        agg.forEach(product -> products.add(mongoConverter.read(Pizza.class, product)));
        return products;
    }

    @Override
    public List<PizzaAggregate> getPizzas() {
        List<PizzaAggregate> pizzas = new ArrayList<>();
        MongoCollection<Document> collection = mongoClient.getDatabase("ecommerce").getCollection("orders");
        List<Bson> search = Arrays.asList(match(eq("size", "medium")),
                group("$name", sum("totalQuantity", "$quantity")),
                project(fields(include("totalQuantity"), computed("name", "$_id"))));
        AggregateIterable<Document> aggregateIterable = collection.aggregate(search);
        MongoCursor<Document> cursor = aggregateIterable.iterator();
        while (cursor.hasNext()){
            Document object = cursor.next();
            Gson gson = new Gson();
            PizzaAggregate pizza = gson.fromJson(object.toJson(), PizzaAggregate.class);
            pizzas.add(pizza);
        }
        return pizzas;
    }

    @Override
    public List<PizzaLookup> getPizzaLookup(List<Bson> query) {
        List<PizzaLookup> pizzaLookups = new ArrayList<>();
        MongoCollection<Document> collection = mongoClient.getDatabase("ecommerce").getCollection("products");
        AggregateIterable<Document> aggregateIterable = collection.aggregate(query);
        MongoCursor<Document> cursor = aggregateIterable.iterator();
        while (cursor.hasNext()){
            Document object = cursor.next();
            Gson gson = new Gson();
            PizzaLookup pizza = gson.fromJson(object.toJson(), PizzaLookup.class);
            pizzaLookups.add(pizza);
        }
        return pizzaLookups;
    }

    private MongoDatabase config(){
        CodecRegistry pojoCodeRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase db = mongoClient.getDatabase(dbName).withCodecRegistry(pojoCodeRegistry);
        return db;
    }

    @Override
    public Product addPizza(Product pizza) {
        MongoDatabase db = config();
        MongoCollection<Product> productCollection = db.getCollection("products", Product.class);
        ClientSession clientSession = mongoClient.startSession();
        try{
            clientSession.startTransaction();
            productCollection.insertOne(clientSession, pizza);
            clientSession.commitTransaction();
        }catch (Exception dbException){
            clientSession.abortTransaction();
            logger.info("Error occured-{}", dbException);
        }
        return pizza;
    }

    @Override
    public List<Product> getAllDominos() {
        Query query = new Query();
        query.addCriteria(Criteria.where("brand").is("Dominos"));
        List<Product> pizzas = mongoTemplate.find(query, Product.class, "products");
        long count = mongoTemplate.count(query, Product.class, "products");
        logger.info("count-{}", count);
        return pizzas;
    }

    @Override
    public void loadData() {

    }
}
