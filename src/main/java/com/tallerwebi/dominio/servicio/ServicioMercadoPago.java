package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DatosPago;
import com.tallerwebi.presentacion.DatosPagoRespuesta;

public interface ServicioMercadoPago {
    DatosPagoRespuesta procesarPago(String pack, Usuario usuario);
}
