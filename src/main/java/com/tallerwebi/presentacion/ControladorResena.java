package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ControladorResena {

    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;

    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;

    @Autowired
    public ControladorResena(ServicioResena servicioResena, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte, ServicioUsuarioApunte servicioUsuarioApunte, ServicioUsuarioApunteResena servicioUsuarioApunteResena) {
        this.servicioResena = servicioResena;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;
        this.servicioUsuarioApunte = servicioUsuarioApunte;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
    }

    @RequestMapping(path = "/formulario-alta-resena", method = RequestMethod.GET)
    public ModelAndView irAFormularioAlta(HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("usuario");
        model.put("usuario", usuario);

        model.put("resena", new Resena());
        model.put("title", "Nueva Reseña");
        return new ModelAndView("formulario-alta-resena", model);
    }

    @RequestMapping(path = "/guardarResena", method = RequestMethod.POST)
    public ModelAndView guardarResena(@Valid Resena resena, BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            ModelMap modelo = new ModelMap();
            modelo.put("resena", resena);
            return new ModelAndView("formulario-alta-resena", modelo);
        }else{
            ModelMap modelo = new ModelMap();
            Usuario usuario=(Usuario) session.getAttribute("usuario");
            Long id = (Long) session.getAttribute("idApunte");
            Apunte apunte = servicioApunte.obtenerPorId(id);
            modelo.put("id", id);
        if (resena != null) {
            if(servicioUsuarioApunteResena.registrarResena(usuario ,apunte, resena)){
                return new ModelAndView("redirect:/detalleApunte/"+id);
            }else {
                modelo.put("error", "No puede dar mas de una reseña");
                return new ModelAndView("formulario-alta-resena", modelo);
            }


        }
        }

        return new ModelAndView("redirect:/detalleApunte");
    }

    @RequestMapping(path = "/borrarResena/{id}", method = RequestMethod.GET)
    public ModelAndView borrar(@PathVariable("id") Long id, HttpSession session) {
        ModelMap modelo = new ModelMap();
        Long idApunte = (Long) session.getAttribute("idApunte");
        try {
            servicioResena.borrar(id);
            modelo.put("status", "success");

        } catch (Exception e) {
            modelo.put("status", "error");
            modelo.put("error", e);
        }
        return new ModelAndView("redirect:/detalleApunte/"+idApunte, modelo);
    }

}



