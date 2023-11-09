package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DatosPreferenciaRespuesta;

public interface ServicioMercadoPago {
    DatosPreferenciaRespuesta crearPreferencia(String pack);
}
