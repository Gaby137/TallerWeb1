package com.tallerwebi.presentacion;

public class DatosPago {
    private Number monto;
    private String nombreProducto;
    private String descripcionProducto;

    public DatosPago(){
    }
    public DatosPago (Number monto, String pack){
        this.monto = monto;
        this.nombreProducto = "Pack " + pack;
        this.descripcionProducto = "Compra de pack " + pack;
    }

    public Number getMonto() {
        return monto;
    }

    public void setMonto(Number monto) {
        this.monto = monto;
    }
}
