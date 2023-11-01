package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DatosMPResponse;
import com.tallerwebi.presentacion.DatosPago;
import com.tallerwebi.presentacion.DatosPagoRespuesta;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class ServicioMercadoPagoImpl implements ServicioMercadoPago {
    public DatosPagoRespuesta procesarPago(String pack, Usuario usuario){
        DatosPago datosPago = null;
        DatosPagoRespuesta responsePago = null;

        if(pack == "oro"){
            datosPago = new DatosPago(100, pack);
        } else if (pack == "plata"){
            datosPago = new DatosPago(50, pack);
        } else {
            datosPago = new DatosPago(20, pack);
        }

        String url = "https://api.mercadopago.com/v1/payments";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer TEST-4819727839399270-102609-725d10a1fcb0916df6b613a36ef4188d-1524389727");
        headers.set("Content-Type","application/json");

        HttpEntity<String> entity = new HttpEntity<String>(datosPago.toString(), headers);

        DatosMPResponse res = restTemplate.postForObject(url, entity, DatosMPResponse.class);

        if (res != null) {
            responsePago = new DatosPagoRespuesta(res.id, res.status, res.status_detail, res.payment_method_id, res.payment_type_id);
        }

        return responsePago;
    }
}
