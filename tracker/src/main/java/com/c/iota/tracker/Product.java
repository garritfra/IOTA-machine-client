package com.c.iota.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long time;
    private String id;
    private boolean success;
    private String description;
    private Map<String, String> measurements;
}
