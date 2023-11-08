package com.tallerwebi.presentacion;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMercadoPago;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorMercadoPago {
    private ServicioMercadoPago servicioMercadoPago;

    public ControladorMercadoPago(ServicioMercadoPago servicioMercadoPago){
        this.servicioMercadoPago = servicioMercadoPago;
    }

    @RequestMapping(path = "/crear-orden", method = RequestMethod.POST)
    public ModelAndView misApuntes(@RequestParam("pack") String pack, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("usuario");

        DatosPagoRespuesta responsePago = servicioMercadoPago.procesarPago(pack, usuario);

        model.put("responsePago", responsePago);
        return new ModelAndView("redirect:"+responsePago.urlCheckout, model);
    }
    @RequestMapping(path = "/planes", method = RequestMethod.GET)
    public ModelAndView apunte() {
        ModelMap model = new ModelMap();
        model.put("datosPago", new DatosPago());
        return new ModelAndView("planes", model);
    }
}