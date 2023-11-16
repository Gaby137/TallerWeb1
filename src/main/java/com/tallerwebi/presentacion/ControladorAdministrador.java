package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.entidad.Rol;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioAdministrador;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ControladorAdministrador {
    private ServicioAdministrador servicioAdministrador;

    @Autowired
    public ControladorAdministrador(ServicioAdministrador servicioAdministrador) {

        this.servicioAdministrador = servicioAdministrador;
    }

    @RequestMapping(path = "/formCarrera", method = RequestMethod.GET)
    public ModelAndView carrera(HttpSession session) {
        ModelMap modelo = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            if (usuario.getRol().equals(Rol.ADMIN)){
                modelo.put("datosCarrera", new DatosCarrera());
                return new ModelAndView("altaCarrera", modelo);
            }else{
                return new ModelAndView("redirect:/home");
            }
        } else {
            return new ModelAndView("redirect:/login");
        }

    }

    @RequestMapping(path = "/subirCarrera", method = RequestMethod.POST)
    public ModelAndView subirCarrera(@Valid DatosCarrera datosCarrera, BindingResult result, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuario");

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("usuario", datosCarrera);
            return new ModelAndView("altaCarrera", model);
        } else {
            try {
                servicioAdministrador.registrarCarrera(datosCarrera);
                return new ModelAndView("redirect:/home");
            } catch (Exception e) {
                ModelMap model = new ModelMap();
                model.put("usuario", datosCarrera);
                model.put("error", e.getMessage());
                return new ModelAndView("altaCarrera", model);
            }
        }

    }



    @RequestMapping(path = "/formMateria", method = RequestMethod.GET)
    public ModelAndView materia(HttpSession session) {
        ModelMap modelo = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            if (usuario.getRol().equals(Rol.ADMIN)){
                List<Carrera> listado = servicioAdministrador.listado();
                modelo.put("listaCarreras", listado);
                modelo.put("datosMateria", new DatosMateria());
                return new ModelAndView("altaMateria", modelo);
            }else{
                return new ModelAndView("redirect:/home");
            }
        } else {
            return new ModelAndView("redirect:/login");
        }

    }

    @RequestMapping(path = "/subirMateria", method = RequestMethod.POST)
    public ModelAndView subirMateria(@Valid DatosMateria datosMateria, BindingResult result, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuario");

        if (result.hasErrors()) {
            ModelMap model = new ModelMap();
            model.put("usuario", datosMateria);
            return new ModelAndView("altaCarrera", model);
        } else {
            try {
                servicioAdministrador.registrarMateria(datosMateria);
                return new ModelAndView("redirect:/home");
            } catch (Exception e) {
                ModelMap model = new ModelMap();
                model.put("usuario", datosMateria);
                model.put("error", e.getMessage());
                return new ModelAndView("altaCarrera", model);
            }
        }

    }


}









