package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatosMPResponse{
    public int collector_id;
    public List<Item> items;
    public Number client_id;
    public Payer payer;
    public String id;
    public String init_point;
    public String sandbox_init_point;


    public static class Item {
        public String title;
        public String description;
        public String currency_id;
        public Number quantity;
        public Number unit_price;
    }
    public static class Phone {
        public String area_code;
        public String number;
    }
    public static class Payer {
        public String name;
        public String surname;
        public String email;
        public String date_created;
        public Phone phone;
    }
}
