package com.vidhan_123.pizzaecommerce.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {
    Integer id;
    String username;
    Integer age;
    Date created;

    public User(Integer id, String username, Integer age, Date created) {
        super();
        this.id = id;
        this.username = username;
        this.age = age;
        this.created = created;
    }
}
