package com.tallerwebi.presentacion;

import com.sun.xml.bind.util.AttributesImpl;
import com.tallerwebi.dominio.Apunte;
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

import java.util.List;

@Controller
public class ControladorResena {

    private ServicioResena servicioResena;

    @Autowired
    public ControladorResena(ServicioResena servicioResena) {
        this.servicioResena = servicioResena;
    }

    @RequestMapping(path = "/formulario-alta-resena", method = RequestMethod.GET)
    public ModelAndView irAFormularioAlta() {
        ModelMap model = new ModelMap();
        model.put("resena", new Resena());
        return new ModelAndView("formulario-alta-resena", model);
    }

    @RequestMapping(path = "/guardarResena", method = RequestMethod.POST)
    public ModelAndView guardarResena(@ModelAttribute("resena") Resena resena) {
        servicioResena.guardar(resena);
        return listarResenas();
    }

    @RequestMapping(path = "/apunte-detalle", method = RequestMethod.GET)
    public ModelAndView listarResenas() {
        ModelMap model = new ModelMap();
        List<Resena> resenas = servicioResena.listar();
        model.put("resenas", resenas);
        return new ModelAndView("apunte-detalle", model);
    }

    @RequestMapping(path = "/borrarResena/{id}", method = RequestMethod.GET)
    public ModelAndView borrar(@PathVariable("id") Long id) {
        ModelMap modelo = new ModelMap();
        try {
            servicioResena.borrar(id);
            modelo.put("mensaje", "Reseña borrada exitosamente");

        } catch (Exception e) {
            modelo.put("error", "Error al intentar borrar la reseña");
        }
        return new ModelAndView("redirect:/apunte-detalle", modelo);
    }

}

