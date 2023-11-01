package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.Date;

public class DatosMPResponse{
    public int id;
    public Date date_created;
    public Date date_approved;
    public Date date_last_updated;
    public Date money_release_date;
    public int issuer_id;
    public String payment_method_id;
    public String payment_type_id;
    public String status;
    public String status_detail;
    public String currency_id;
    public String description;
    public int taxes_amount;
    public int shipping_amount;
    public int collector_id;
    public Object payer;
    public Object metadata;
    public Object additional_info;
    public String external_reference;
    public double transaction_amount;
    public int transaction_amount_refunded;
    public int coupon_amount;
    public Object transaction_details;
    public ArrayList<Object> fee_details;
    public String statement_descriptor;
    public int installments;
    public Object card;
    public String notification_url;
    public String processing_mode;
    public Object point_of_interaction;
}
