package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
        ModelMap modelo = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        modelo.put("usuario", usuario);
        modelo.put("title", "Perfil");

        if (usuario != null) {
            return new ModelAndView("miPerfil", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }

    }
}









