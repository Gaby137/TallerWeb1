package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioResena;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

@Controller
public class ControladorResena {

    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;

    @Autowired
    public ControladorResena(ServicioResena servicioResena, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte, ServicioUsuarioApunteResena servicioUsuarioApunteResena) {
        this.servicioResena = servicioResena;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;
    }

    @RequestMapping(path = "/formulario-alta-resena", method = RequestMethod.GET)
    public ModelAndView irAFormularioAlta(HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("usuario");
        model.put("usuario", usuario);
        model.put("resena", new Resena());
        return new ModelAndView("formulario-alta-resena", model);
    }

    @RequestMapping(path = "/guardarResena", method = RequestMethod.POST)
    public ModelAndView guardarResena(@ModelAttribute("resena") Resena resena, HttpSession session) {
        ModelMap modelo = new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("usuario");
        Long id = (Long) session.getAttribute("idApunte");
        Apunte apunte = servicioApunte.obtenerPorId(id);
        modelo.put("id", id);

        if (resena != null) {
            if(servicioUsuarioApunteResena.registrar(usuario ,apunte, resena)){
                return new ModelAndView("redirect:/misApuntes");
            }else {
                modelo.put("error", "No puede dar mas de una reseña");
                return new ModelAndView("formulario-alta-resena", modelo);
            }


        }

        return new ModelAndView("redirect:/detalleApunte");
    }

    @RequestMapping(path = "/borrarResena/{id}", method = RequestMethod.GET)
    public ModelAndView borrar(@PathVariable("id") Long id, HttpSession session) {
        ModelMap modelo = new ModelMap();
        Long idApunte = (Long) session.getAttribute("idApunte");
        modelo.put("id", idApunte);
        try {
            servicioResena.borrar(id);
            modelo.put("mensaje", "Reseña borrada exitosamente");

        } catch (Exception e) {
            modelo.put("error", "Error al intentar borrar la reseña");
        }
        return new ModelAndView("redirect:/detalleApunte/{id}", modelo);
    }

}



