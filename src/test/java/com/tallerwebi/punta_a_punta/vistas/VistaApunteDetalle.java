package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class VistaApunteDetalle extends VistaWeb {

    public VistaApunteDetalle(Page page) {
        super(page);

        page.navigate("localhost:8080/spring/login");
    }

    public String obtenerTextoNombreDelApunte(){
            return this.obtenerTextoDelElemento("h2");
    }

    public boolean obtenerElBotonDeResena(){
        return this.existeElemento("h2");
    }

    public String obtenerMensajeDeError(){
        return this.obtenerTextoDelElemento("p.alert.alert-danger");
    }

    public void escribirEMAIL(String email){
        this.escribirEnElElemento("#email", email);
    }

    public void escribirClave(String clave){
        this.escribirEnElElemento("#password", clave);
    }

    public void darClickEnIniciarSesion(String urlVista){
        this.darClickEnElElemento("button.login100-form-btn");
        page.navigate(urlVista);
    }
}
