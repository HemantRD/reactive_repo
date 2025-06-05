package com.example.demo.r2dbc;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
public class TacoOrder {
    @Id
    private Long id;
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
    private Set<Long> tacoIds = new LinkedHashSet<>();
    @Transient
    private transient List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        tacos.add(taco);
        if (taco.getId() != null) {
            tacoIds.add(taco.getId());
        }
    }
}