package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Rol;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMercadoPago;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorMercadoPagoTest {
    private ControladorMercadoPago controladorMercadoPago;
    private ServicioMercadoPago servicioMercadoPago;
    private ServicioUsuario servicioUsuario;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        servicioMercadoPago = mock(ServicioMercadoPago.class);
        servicioUsuario = mock(ServicioUsuario.class);
        controladorMercadoPago = new ControladorMercadoPago(servicioMercadoPago,servicioUsuario);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    public void queSeRedireccioneCorrectamente(){
        String pack = "oro";
        Usuario usuario = new Usuario("Matias","Godoy",0,"prueba@asd.com","1234", Rol.USUARIO,true,"",0.00,0.00,new Date(), new Date());
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        DatosPreferenciaRespuesta response = new DatosPreferenciaRespuesta("1234", "Matias", "Godoy", "0303456", "fecha", "urlMercadoPago");
        when(servicioMercadoPago.crearPreferencia(pack)).thenReturn(response);

        ModelAndView modelAndView = controladorMercadoPago.crearOrden(pack,sessionMock);
        DatosPreferenciaRespuesta responseMock = (DatosPreferenciaRespuesta) modelAndView.getModel().get("responsePago");

        assertEquals("urlMercadoPago", responseMock.urlCheckout);
    }

    @Test
    public void queSiElUsuarioNoEstaLogueadoNoPuedaCrearPreferenciaYSeRedireccioneAlLogin(){
        String pack = "oro";
        DatosPreferenciaRespuesta response = new DatosPreferenciaRespuesta("1234", "Matias", "Godoy", "0303456", "fecha", "urlMercadoPago");
        when(sessionMock.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorMercadoPago.crearOrden(pack,sessionMock);
        DatosPreferenciaRespuesta responsePreferencia = (DatosPreferenciaRespuesta) modelAndView.getModel().get("responsePago");

        assertNull(responsePreferencia);
        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void queSiElUsuarioComproPackBronceTenga20Puntos(){
        final int PUNTOS_ESPERADOS = 20;
        final String pack = "bronce";
        Usuario usuario = new Usuario("Matias","Godoy",0,"prueba@asd.com","1234", Rol.USUARIO,true,"",0.00,0.00,new Date(), new Date());
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        controladorMercadoPago.validarPago("approved",pack,sessionMock);

        assertEquals(PUNTOS_ESPERADOS, usuario.getPuntos());
    }

    @Test
    public void queSiElUsuarioComproPackPlataTenga50Puntos(){
        final int PUNTOS_ESPERADOS = 50;
        final String pack = "plata";
        Usuario usuario = new Usuario("Matias","Godoy",0,"prueba@asd.com","1234", Rol.USUARIO,true,"",0.00,0.00,new Date(), new Date());
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        controladorMercadoPago.validarPago("approved",pack,sessionMock);

        assertEquals(PUNTOS_ESPERADOS, usuario.getPuntos());
    }

    @Test
    public void queSiElUsuarioComproPackOroTenga100Puntos(){
        final int PUNTOS_ESPERADOS = 100;
        final String pack = "oro";
        Usuario usuario = new Usuario("Matias","Godoy",0,"prueba@asd.com","1234", Rol.USUARIO,true,"",0.00,0.00,new Date(), new Date());
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        controladorMercadoPago.validarPago("approved",pack,sessionMock);

        assertEquals(PUNTOS_ESPERADOS, usuario.getPuntos());
    }
    @Test
    public void queSiElPagoDeCualquierPackFallaNoSeSumenPuntos(){
        final int PUNTOS_ESPERADOS = 123;

        Usuario usuario = new Usuario("Matias","Godoy",PUNTOS_ESPERADOS,"prueba@asd.com","1234", Rol.USUARIO,true,"",0.00,0.00,new Date(), new Date());
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);

        controladorMercadoPago.validarPago("pending","bronce",sessionMock);
        controladorMercadoPago.validarPago("rejected","plata",sessionMock);
        controladorMercadoPago.validarPago("cancelled","oro",sessionMock);
        controladorMercadoPago.validarPago("loQueSea","bronce",sessionMock);
        controladorMercadoPago.validarPago("","",sessionMock);

        assertEquals(PUNTOS_ESPERADOS, usuario.getPuntos());
    }
}
