package com.tallerwebi.presentacion;

public class DatosPagoRespuesta {
    private int id;
    private String estado;
    private String estadoDetalle;
    private String metodoDePago;
    private String tipoDePago;

    public DatosPagoRespuesta(int id, String estado, String estadoDetalle, String metodoDePago, String tipoDePago) {
        this.id = id;
        this.estado = estado;
        this.estadoDetalle = estadoDetalle;
        this.metodoDePago = metodoDePago;
        this.tipoDePago = tipoDePago;
    }
}
