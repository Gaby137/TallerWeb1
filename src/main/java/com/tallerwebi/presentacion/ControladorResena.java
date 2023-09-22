package com.tallerwebi.presentacion;

import com.sun.xml.bind.util.AttributesImpl;
import com.tallerwebi.dominio.Resena;
import com.tallerwebi.dominio.ServicioResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping(path = "/guardarResena", method = RequestMethod.POST)
    public String guardarResena(@ModelAttribute("resena") Resena resena) {
        servicioResena.guardar(resena);
        return "redirect:/apunte-detalle";
    }

}

