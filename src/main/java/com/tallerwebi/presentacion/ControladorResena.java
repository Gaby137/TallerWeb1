package com.tallerwebi.presentacion;

import com.sun.xml.bind.util.AttributesImpl;
import com.tallerwebi.dominio.Resena;
import com.tallerwebi.dominio.ServicioResena;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorResena {

    private ServicioResena servicioResena;

    @Autowired
    public ControladorResena(ServicioResena servicioResena) {
        this.servicioResena = servicioResena;
    }

    @RequestMapping(path = "/formulario-alta-resena", method = RequestMethod.GET)
    public ModelAndView irAHome() {
        ModelMap model = new ModelMap();
        model.put("resena", new Resena());
        return new ModelAndView("formulario-alta-resena", model);
    }
    @RequestMapping(path = "/guardarResena", method = RequestMethod.POST)
    public ModelAndView guardarResena(@ModelAttribute("resena") Resena resena) {
        servicioResena.guardar(resena);
        return new ModelAndView("apunte-detalle");
    }

}

