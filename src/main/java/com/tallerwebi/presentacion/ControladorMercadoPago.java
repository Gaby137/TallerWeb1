package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMercadoPago;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorMercadoPago {
    private ServicioMercadoPago servicioMercadoPago;
    private ServicioUsuario servicioUsuario;

    public ControladorMercadoPago(ServicioMercadoPago servicioMercadoPago, ServicioUsuario servicioUsuario){
        this.servicioMercadoPago = servicioMercadoPago;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/crear-orden", method = RequestMethod.POST)
    public ModelAndView crearOrden(@RequestParam("pack") String pack) {
        ModelMap model = new ModelMap();

        DatosPreferenciaRespuesta responsePreferencia = servicioMercadoPago.crearPreferencia(pack);

        model.put("responsePago", responsePreferencia);
        return new ModelAndView("redirect:"+responsePreferencia.urlCheckout, model);
    }
    @RequestMapping(path = "/packs", method = RequestMethod.GET)
    public ModelAndView planes() {
        ModelMap model = new ModelMap();
        model.put("datosPago", new DatosPago());
        return new ModelAndView("packs", model);
    }

    @RequestMapping(path = "/validar-pago", method = RequestMethod.GET)
    public ModelAndView validarPago(@RequestParam("status") String status, @RequestParam("external_reference") String external_reference, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("usuario");
        int puntosComprados = 0;

        switch (external_reference){
            case "oro":
                puntosComprados = 100;
                break;
            case "plata":
                puntosComprados = 50;
                break;
            case "bronce":
                puntosComprados = 20;
                break;
            default:
                puntosComprados = 0;
        }

        model.put("pack",external_reference);

        if(status.equals("approved")){
            model.put("estado","Su pack " + external_reference + " fue comprado con éxito!");

            usuario.setPuntos(usuario.getPuntos() + puntosComprados);
            servicioUsuario.actualizar(usuario);
        }else{
            model.put("error","Hubo un inconveniente al procesar su pago. Por favor, intente de nuevo más tarde.");
            return new ModelAndView("packs", model);
        }

        return new ModelAndView("resultadoCompra", model);
    }
}