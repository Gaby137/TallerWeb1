package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorUsuario {
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/miPerfil", method = RequestMethod.GET)
    public ModelAndView perfil(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("miPerfil");

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            modelAndView.addObject("usuario", usuario);
        } else {
            return new ModelAndView("redirect:/login");
        }

        return modelAndView;
    }
}









