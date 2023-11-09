package com.tallerwebi.presentacion;

public class DatosPreferenciaRespuesta {
    private String id;
    public String nombreCliente;
    private String apellidoCliente;
    private String telefono;
    private String fechaPago;
    public String urlCheckout;

    public DatosPreferenciaRespuesta(String id, String nombreCliente, String apellidoCliente, String telefono, String fechaPago, String urlCheckout) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.telefono = telefono;
        this.fechaPago = fechaPago;
        this.urlCheckout = urlCheckout;
    }
}
