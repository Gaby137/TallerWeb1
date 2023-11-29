package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorUsuario {
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario, ServicioUsuarioApunteResena servicioUsuarioApunteResena) {

        this.servicioUsuario = servicioUsuario;
        this.servicioUsuarioApunteResena = servicioUsuarioApunteResena;

    }

    @RequestMapping(path = "/miPerfil", method = RequestMethod.GET)
    public ModelAndView perfil(HttpSession session) {
        ModelMap modelo = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Apunte> apuntesCreados = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);
        var cantidadApuntes = apuntesCreados.size();

        modelo.put("usuario", usuario);
        modelo.put("title", "Perfil");
        modelo.put("cantidadApuntesUsuario",cantidadApuntes);

        if (usuario != null) {
            return new ModelAndView("miPerfil", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }

    }
}









