package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Apunte;
import com.tallerwebi.dominio.ServicioApunte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorHome {

    private ServicioApunte servicioApunte;

    public ControladorHome(ServicioApunte servicioApunte) {
        this.servicioApunte = servicioApunte;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHome() {
        List<Apunte> apuntes = servicioApunte.obtenerApuntes();
        ModelMap modelo = new ModelMap();
        modelo.put("apuntes", apuntes);
        return new ModelAndView("home", modelo);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        List<Apunte> apuntes = servicioApunte.obtenerApuntes();
        ModelMap modelo = new ModelMap();
        modelo.put("apuntes", apuntes);
        return new ModelAndView("home", modelo);
    }
}
