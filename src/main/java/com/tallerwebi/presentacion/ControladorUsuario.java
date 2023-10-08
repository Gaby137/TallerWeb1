package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorUsuario {
    private ServicioUsuario servicioUsuario;
    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario){
        this.servicioUsuario=servicioUsuario;
    }
    @RequestMapping(path = "/miPerfil", method = RequestMethod.GET)
    public ModelAndView perfil(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("miPerfil");

        // Obtén el usuario de la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            // Agrega el usuario al modelo
            modelAndView.addObject("usuario", usuario);
        }else{
            return new ModelAndView("redirect:/login");
        }

        return modelAndView;
    }
}

