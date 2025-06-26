package com.example.demo.actuator;

import java.util.List;

//import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import com.example.demo.r2dbc.Ingredient;
import com.example.demo.r2dbc.Taco;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class TacoMetrics /* extends AbstractRepositoryEventListener<Taco>*/ {
    private MeterRegistry meterRegistry;

    public TacoMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    //    @Override
    protected void onAfterCreate(Taco taco) {
//        List<Ingredient> ingredients = taco.getIngredients();
//        for (Ingredient ingredient : ingredients) {
        meterRegistry.counter("tacocloud",
                "ingredient", "sd").increment();
//        }
    }
}