package com.tallerwebi.presentacion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatosPago {
    private List<Item> items;

    public DatosPago(){
    }
    public DatosPago (Number monto, String pack){
        this.items = new ArrayList<Item>();
        Item item = new Item();
        item.title = "Compra de pack " + pack;
        item.quantity = 1;
        item.unit_price = monto;
        this.items.add(item);
    }

    private static class Item {
        private String title;
        private Number quantity;
        private Number unit_price;
    }
}
