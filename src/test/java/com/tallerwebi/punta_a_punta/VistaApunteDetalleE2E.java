package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tallerwebi.punta_a_punta.vistas.VistaApunteDetalle;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VistaApunteDetalleE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaApunteDetalle vistaApunteDetalle;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        //browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));

    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaApunteDetalle = new VistaApunteDetalle(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirElNombreDelApunte() {
        redirigirALaVista("localhost:8080/spring/detalleApunte/23");

        String texto = vistaApunteDetalle.obtenerTextoNombreDelApunte().replaceAll("\\s", "");;
        assertThat("TestPDF", equalToIgnoringCase(texto));
    }

    @Test
    void noDeberiaAgregarElBotonDeResena() {
        redirigirALaVista("localhost:8080/spring/detalleApunte/23");
        boolean elem = vistaApunteDetalle.obtenerElBotonDeResena();
        assertFalse(elem);
    }

    private void redirigirALaVista(String urlVista) {
        vistaApunteDetalle.escribirEMAIL("genrriquezzzsanchez@alumno.unlam.edu.ar");
        vistaApunteDetalle.escribirClave("c");
        vistaApunteDetalle.darClickEnIniciarSesion(urlVista);
    }
}
