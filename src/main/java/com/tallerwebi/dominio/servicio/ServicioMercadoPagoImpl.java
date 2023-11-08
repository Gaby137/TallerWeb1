package com.tallerwebi.dominio.servicio;

import com.google.gson.Gson;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DatosMPResponse;
import com.tallerwebi.presentacion.DatosPago;
import com.tallerwebi.presentacion.DatosPreferenciaRespuesta;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service("servicioMercadoPago")
@Transactional
public class ServicioMercadoPagoImpl implements ServicioMercadoPago {
    public DatosPreferenciaRespuesta crearPreferencia(String pack){
        DatosPago datosPago = null;
        DatosPreferenciaRespuesta responsePago = null;

        if(pack.equals("oro")){
            datosPago = new DatosPago(100, pack);
        } else if (pack.equals("plata")){
            datosPago = new DatosPago(50, pack);
        } else {
            datosPago = new DatosPago(20, pack);
        }

        String url = "https://api.mercadopago.com/checkout/preferences";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer TEST-4819727839399270-102609-725d10a1fcb0916df6b613a36ef4188d-1524389727");
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
