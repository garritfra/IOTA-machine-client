package com.c.iota.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
//    public Product(String value, long time){
//        this.time = time;
//        this.value = value;
//    }
    private String value;
    private long time;
}
