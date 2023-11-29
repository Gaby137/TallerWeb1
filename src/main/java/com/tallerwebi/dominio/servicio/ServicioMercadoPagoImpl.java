package com.tallerwebi.dominio.servicio;

import com.google.gson.Gson;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DatosMPResponse;
import com.tallerwebi.presentacion.DatosPago;
import com.tallerwebi.presentacion.DatosPreferenciaRespuesta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;

import javax.transaction.Transactional;

@Service("servicioMercadoPago")
@Transactional
public class ServicioMercadoPagoImpl implements ServicioMercadoPago {

    @Value("${urlMercadopago}")
    private String urlMercadopago;
    @Value("${authMercadopago}")
    private String authMercadopago;
    public DatosPreferenciaRespuesta crearPreferencia(String pack){
        DatosPago datosPago = null;
        DatosPreferenciaRespuesta responsePago = null;

        if(pack.equals("oro")){
            datosPago = new DatosPago(700, pack);
        } else if (pack.equals("plata")){
            datosPago = new DatosPago(400, pack);
        } else {
            datosPago = new DatosPago(200, pack);
        }

        String url = urlMercadopago;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",authMercadopago);
        headers.set("Content-Type","application/json");

        Gson gson = new Gson();
        String jsonDatos = gson.toJson(datosPago);

        HttpEntity<String> entity = new HttpEntity<String>(jsonDatos, headers);
        DatosMPResponse res = restTemplate.postForObject(url, entity, DatosMPResponse.class);

        if (res != null) {
            responsePago = new DatosPreferenciaRespuesta(res.id, res.payer.name, res.payer.surname, res.payer.phone.area_code + " " + res.payer.phone.number, res.payer.date_created, res.init_point);
        }

        return responsePago;
    }
}
